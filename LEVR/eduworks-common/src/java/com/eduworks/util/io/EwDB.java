package com.eduworks.util.io;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Fun.Tuple2;
import org.mapdb.HTreeMap;

import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyRunnable;

public class EwDB
{
	public DB				db;
	public Object	handleLock	= new Object();
	public Future<?>   tCleose = null;
	public AtomicInteger		handles = new AtomicInteger(0);
	static Map<String,EwDB> cache = new HashMap<String,EwDB>();
	
	public static synchronized EwDB get(String _baseDirectory, String _databaseName)
	{
		String cacheKey = _baseDirectory + " " + _databaseName;

		EwDB lsh = null;
		lsh = cache.get(cacheKey);
		if (lsh != null)
		{
			synchronized(lsh.handles)
			{
				lsh.handles.incrementAndGet();
			}
			return lsh;
		}
		lsh = new EwDB();
		new File(_baseDirectory).mkdirs();
		new File(_baseDirectory).mkdir();
		File dbLocation = new File(_baseDirectory, _databaseName);
		try {
			lsh.db = DBMaker.newFileDB(dbLocation).closeOnJvmShutdown().make();
		} catch (IOError e) {
			System.out.println(e.getMessage());
			if (e.getMessage().equalsIgnoreCase("java.io.IOException: storage has invalid header") || e.getMessage().equalsIgnoreCase("java.io.IOException: New store format version, please use newer MapDB version")) {
				upgradeDatabase(dbLocation, false);
				lsh.db = DBMaker.newFileDB(dbLocation).closeOnJvmShutdown().make();
			}
		}
		cache.put(cacheKey, lsh);
		lsh.handles.incrementAndGet();
		return lsh;
	}

	public static synchronized EwDB getNoTransaction(String _baseDirectory, String _databaseName)
	{
		String cacheKey = _baseDirectory + " " + _databaseName;

		EwDB lsh = null;
		lsh = cache.get(cacheKey);
		if (lsh != null)
		{
			synchronized(lsh.handles)
			{
				lsh.handles.incrementAndGet();
			}
			return lsh;
		}
		lsh = new EwDB();
		new File(_baseDirectory).mkdirs();
		new File(_baseDirectory).mkdir();
		File dbLocation = new File(_baseDirectory, _databaseName);
		try {
			lsh.db = DBMaker.newFileDB(dbLocation).transactionDisable().closeOnJvmShutdown().make();
		} catch (IOError e) {
			System.out.println(e.getMessage());
			if (e.getMessage().equalsIgnoreCase("java.io.IOException: storage has invalid header") || e.getMessage().equalsIgnoreCase("java.io.IOException: New store format version, please use newer MapDB version")) {
				upgradeDatabase(dbLocation, false);
				lsh.db = DBMaker.newFileDB(dbLocation).transactionDisable().closeOnJvmShutdown().make();
			}
		}
		cache.put(cacheKey, lsh);
		lsh.handles.incrementAndGet();
		return lsh;
	}
	private static synchronized void upgradeDatabase(File f, boolean compression) {
		try {
			System.out.println("found old db format, upgrading - ");
			final DB db;
			File targetF = new File(f.getAbsolutePath());
			File oldTarget = new File(f.getAbsolutePath()+"Old");
			File newF = new File(oldTarget.getAbsolutePath());
			f.renameTo(newF);
			f = new File(targetF.getAbsolutePath()+".p");
			newF = new File(targetF.getAbsolutePath()+"Old.p");
			f.renameTo(newF);
			f = new File(targetF.getAbsolutePath()+".t");
			newF = new File(targetF.getAbsolutePath()+"Old.t");
			f.renameTo(newF);
			if (!compression)
				db = DBMaker.newFileDB(targetF).transactionDisable().asyncWriteEnable().closeOnJvmShutdown().make();
			else
				db = DBMaker.newFileDB(targetF).transactionDisable().asyncWriteEnable().compressionEnable().closeOnJvmShutdown().make();
			
			LogOutputStream los = new LogOutputStream() {
				boolean hMap = false;
				boolean bMap = false;
				boolean atomic = false;
				org.mapdb.Atomic.Integer idCounter = null;
				HTreeMap hm = null;
				NavigableSet<Tuple2<String, String>> bm = null;
				Object key = null;
				Object value = null;
				
				@Override
				protected void processLine(String line, int logLevel) {

					if (hMap) {
						if (hm==null)
							hm = db.getHashMap(line);
						else {
							if (key!=null&&value!=null) {
								hm.put(key, value);
								key = null;
								value = null;
							}
							
							if (key==null)
								key = line;
							else if (value==null)
								value = line;
						}
					} else if (bMap) {
						if (bm==null)
							bm = db.getTreeSet(line);
						else {
							if (key!=null&&value!=null) {
								bm.add(new Tuple2(key, value));
								key = null;
								value = null;
							}
							
							if (key==null)
								key = line;
							else if (value==null)
								value = line;
						}
					} else if (atomic) {
						if (idCounter==null)
							idCounter = db.getAtomicInteger(line);
						else {
							if (key!=null) {
								idCounter.set(Integer.parseInt(key.toString()));
								key = null;
							}
							
							if (key==null)
								key = line;
						}
					}
					
					if (line.equals("S-HTreeMap"))
						hMap = true;
					else if (line.equals("E-HTreeMap")) {
						hMap = false;
						hm = null;
						key = null;
						value = null;
					} else if (line.equals("S-BTreeMap"))
						bMap = true;
					else if (line.equals("E-BTreeMap")) {
						bMap = false;
						bm = null;
						key = null;
						value = null;
					} else if (line.equals("S-AtomicInteger"))
						atomic = true;
					else if (line.equals("E-AtomicInteger")) {
						atomic = false;
						idCounter = null;
						key = null;
						value = null;
					}
				}
			};
			
			String path = EwFileSystem.findFile("exportDB.jar", EwDB.class, true, false).getAbsolutePath();

			PumpStreamHandler psh = new PumpStreamHandler(los);
			CommandLine cl = CommandLine.parse("java -cp " + path + " -jar " + path + " " + oldTarget.getAbsolutePath());
			DefaultExecutor exec = new DefaultExecutor();
			exec.setStreamHandler(psh);
			exec.execute(cl);	
			db.commit();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (ExecuteException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
	
	public void commit() {
		db.commit();
	}

	public static synchronized EwDB getCompressed(String _baseDirectory, String _databaseName)
	{
		String cacheKey = _baseDirectory + " " + _databaseName+"Compressed";

		EwDB lsh = null;
		lsh = (EwDB) cache.get(cacheKey);
		if (lsh != null)
		{
			synchronized(lsh.handles)
			{
				lsh.handles.incrementAndGet();
			}
			return lsh;
		}
		lsh = new EwDB();
		new File(_baseDirectory).mkdirs();
		new File(_baseDirectory).mkdir();
		File dbLocation = new File(_baseDirectory, _databaseName);
		try {
			lsh.db = DBMaker.newFileDB(dbLocation).compressionEnable().closeOnJvmShutdown().make();
		} catch (IOError e) {
			System.out.println(e.getMessage());
			if (e.getMessage().equalsIgnoreCase("java.io.IOException: storage has invalid header") || e.getMessage().equalsIgnoreCase("java.io.IOException: New store format version, please use newer MapDB version")) {
				upgradeDatabase(dbLocation, true);
				lsh.db = DBMaker.newFileDB(dbLocation).compressionEnable().closeOnJvmShutdown().make();
			}
		}
		cache.put(cacheKey, lsh);
		lsh.handles.incrementAndGet();
		return lsh;
	}
	
    protected final ReentrantReadWriteLock commitLock = new ReentrantReadWriteLock();
	public boolean	compact = false;
	public AtomicInteger writeCount = new AtomicInteger(0);
    
	public synchronized void close()
	{
		if (handles.decrementAndGet() == 0)
			if (tCleose == null || tCleose.isDone())
				tCleose = EwThreading.fork(new MyRunnable(){
	
					@Override
					public void run()
					{
						while (handles.get() != 0)
							EwThreading.sleep(5000);
						synchronized(handles)
						{
							while (handles.get() != 0)
								EwThreading.sleep(5000);
							if (compact || writeCount.get() > 0)
							{
//								System.out.println("Committing.");
								db.commit();
//								System.out.println("Committed.");
							}
							if (compact)
							{
								compact = false;
//								System.out.println("Compacting.");
//								db.compact();
//								System.out.println("Compacted.");
							}
						}
					}
				});
	}

	public void compact()
	{
		db.compact();
	}

}
