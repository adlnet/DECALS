package com.eduworks.lang.threading;

import java.io.IOException;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.log4j.Logger;

import com.eduworks.lang.EwList;

public class EwThreading
{
	static List<ThreadPoolExecutor> tpses = Collections.synchronizedList(new EwList<ThreadPoolExecutor>());
	static Thread watcher = null;
	static Logger log = Logger.getLogger(EwThreading.class);
	public static int threads = Math.min(50, Math.max(5, Runtime.getRuntime().availableProcessors() * 5));

	{
		startThreadPool();
	}

	public static void setThreadCount(int threadBoost)
	{
		for (ThreadPoolExecutor tps : tpses)
		{
			tps.setCorePoolSize(threadBoost);
			tps.setMaximumPoolSize(threadBoost);
		}

	}

	public static long getTaskCount()
	{
		int taskCount = 0;
		try
		{
			for (ThreadPoolExecutor tps : tpses)
				taskCount += tps.getTaskCount() - tps.getCompletedTaskCount();
		}
		catch (ConcurrentModificationException ex)
		{

		}
		return taskCount;
	}

	public static long getTaskCount(int level)
	{
		return tpses.get(level).getTaskCount() - tpses.get(level).getCompletedTaskCount();
	}

	private static ThreadPoolExecutor getTps()
	{
		int level = getThreadLevel();
		if (tpses.size() <= level)
			return null;
		return tpses.get(level);
	}

	public static int getThreadLevel()
	{
		int level = 0;
		if (Thread.currentThread().getName().length() > 5)
			return level;
		try
		{
			level = Integer.parseInt(Thread.currentThread().getName());
		}
		catch (NumberFormatException ex)
		{
		}
		return level;
	}

	public static void sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
		}
	}

	public static ThreadPoolExecutor getExecutorPool()
	{
		ThreadPoolExecutor tps = getTps();
		if (tps == null)
			tps = startThreadPool();
		return tps;
	}

	static synchronized ThreadPoolExecutor startThreadPool()
	{
		if (getTps() != null)
			return getTps();
		log.info("Using " + threads + " number of threads.");
		while (tpses.size() - 1 < getThreadLevel())
			tpses.add(new ThreadPoolExecutor(threads, threads, 60L, TimeUnit.SECONDS, new ConcurrentBlockingQueue<Runnable>()));
		int level = tpses.size() - 1;
		try
		{
			tpses.get(level).allowCoreThreadTimeOut(true);
			tpses.get(level).setThreadFactory(new ThreadFactory()
			{
				@Override
				public Thread newThread(Runnable arg0)
				{
					Thread thread = new Thread(arg0);
					thread.setPriority(Thread.MIN_PRIORITY);
					return thread;
				}
			});
		}
		catch (Exception e)
		{

		}
		final Thread thisThread = Thread.currentThread();
		// if (watcher == null)
		// {
		// watcher = new Thread(new Runnable()
		// {
		//
		// @Override
		// public void run()
		// {
		// for (int level = 0; level < tpses.size(); level++)
		// {
		// int currentThreads = threads;
		// long completedTaskCount = tpses.get(level).getCompletedTaskCount();
		// int secondsStuck = 0;
		// while (thisThread.isAlive())
		// {
		// try
		// {
		// Thread.sleep(1000);
		// if (tpses.get(level).getActiveCount() > 0 && getTaskCount() > 0)
		// if (tpses.get(level).getCompletedTaskCount() == completedTaskCount)
		// {
		// if (secondsStuck <= 10)
		// secondsStuck++;
		// }
		// else if (secondsStuck > 0)
		// secondsStuck--;
		// else if (secondsStuck > 0)
		// secondsStuck--;
		// if (secondsStuck > 10)
		// {
		// if (getTaskCount(level) > threads && threads < 100)
		// {
		// threads++;
		// tpses.get(level).setCorePoolSize(threads);
		// tpses.get(level).setMaximumPoolSize(threads);
		// log.info("Detect stuck. Scaling thread count. " + getTaskCount(level)
		// + " tasks, " + tpses.get(level).getActiveCount()
		// + " threads, Now at " + threads);
		// }
		// }
		// else if (currentThreads != threads)
		// {
		// threads--;
		// tpses.get(level).setCorePoolSize(threads);
		// tpses.get(level).setMaximumPoolSize(threads);
		// log.info("Stick unstuck, scaling back. " + getTaskCount(level) +
		// " tasks, " + tpses.get(level).getActiveCount()
		// + " threads, Now at " + threads);
		// }
		// completedTaskCount = tpses.get(level).getCompletedTaskCount();
		// }
		// catch (InterruptedException e)
		// {
		// e.printStackTrace();
		// }
		// }
		//
		// if (tpses.get(level) != null)
		// tpses.get(level).shutdown();
		// tpses.remove(0);
		// }
		// }
		// });
		// watcher.setName("Watcher thread.");
		// watcher.start();
		// }
		return tpses.get(level);
	}

	public static void forkAccm(MyFutureList placeToAdd, MyRunnable r)
	{
		placeToAdd.add(fork(r));
	}

	public static void parallel(boolean forkSlowly, MyRunnable... rs)
	{
		MyFutureList list = new MyFutureList();
		for (MyRunnable r : rs)
			forkAccm(list, forkSlowly, r);
		list.nowPause();
	}

	public static void forkAccm(MyFutureList placeToAdd, boolean forkSlowly, MyRunnable r)
	{
		placeToAdd.add(fork(forkSlowly, r, Integer.MAX_VALUE));
	}

	public static void forkAccm(MyFutureList placeToAdd, boolean forkSlowly, MyRunnable r, int forkLimit)
	{
		placeToAdd.add(fork(forkSlowly, r, forkLimit));
		if (forkSlowly && placeToAdd.list.size() % 10000 == 0)
			log.info("So far " + placeToAdd.list.size());

	}

	public static void fork(int min, int lessthan, MyRunnable r)
	{
		fork(min, lessthan, false, r);
	}

	public static Future<?> fork(final MyRunnable r)
	{
		return fork(false, r, Integer.MAX_VALUE);
	}

	public static Future<?> fork(boolean forkSlowly, final MyRunnable r)
	{
		return fork(false, r, Integer.MAX_VALUE);
	}

	public static Future<?> fork(boolean forkSlowly, final MyRunnable r, int forkLimit)
	{
		ThreadPoolExecutor tps = getTps();
		if (tps == null)
			tps = startThreadPool();
		try
		{
			if (forkSlowly || forkLimit != Integer.MAX_VALUE)
				while (getTaskCount(getThreadLevel()) > threads || getTaskCount(getThreadLevel()) >= forkLimit)
					EwThreading.sleep(10);
			final int nextLevel = getThreadLevel() + 1;
			MyRunnable run;
			Future<?> submit = tps.submit(run = new MyRunnable()
			{
				int level = nextLevel;

				@Override
				public void run()
				{
					try
					{
						Thread.currentThread().setName(Integer.toString(level));
						r.run();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
			run.f = submit;
			r.f = submit;
			return submit;
		}
		catch (RejectedExecutionException e)
		{
			tpses.remove(getThreadLevel());
			return fork(r);
			// throw new RuntimeException(e);
		}
	}

	public static class MyFutureList
	{
		private static final long serialVersionUID = -8460295382838816873L;
		long ms = System.currentTimeMillis();
		long zero = System.currentTimeMillis();
		long count = 0;

		List<Future<?>> list = Collections.synchronizedList(new EwList<Future<?>>());

		public void nowPause()
		{
			nowPause(false);
		}

		public void add(Future<?> fork)
		{
			list.add(fork);
			count++;
		}

		public void nowPause(boolean report)
		{
			int pause = 1;
			Date reportNext = new Date();
			while (true)
			{
				if (count == 0)
					return;
				try
				{
					if (System.in.available() >= 1 && System.in.read() == '`')
					{
						log.debug("Aborted.");
						return;
					}
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				boolean go_on = true;
				int i = 0;
				for (Future f : list)
					if (f.isDone() == false)
					{
						go_on = false;
						i++;
					}
				if (go_on)
					break;
				Date d = new Date();
				d.setTime(d.getTime() - 10000);
				if (report && reportNext.before(d))
				{
					long current = System.currentTimeMillis();
					long future = (long) ((((((double) count) / ((double) (count - i))) - 1.0) * (current - zero)) + current);
					String stuff = "Started: " + new Date(zero).toString() + " Estd Done: " + new Date(future).toString();
					log.info("So far " + (count - i) + "/" + count + "(" + ((double) (count - i)) / ((double) count) + ") " + stuff);
					// long duration = System.currentTimeMillis() - ms;
					// double perSecond = (double) (list.size() - i) / ((double)
					// duration / 1000.0);
					// log.debug("Waiting on " + i + " tasks to complete. " +
					// perSecond + " per second.");
					reportNext = new Date();
				}
				try
				{
					Thread.sleep(pause = Math.min(pause *= 2, 100));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		public void nowKill()
		{
			for (Future<?> f : list)
			{
				f.cancel(true);
			}
		}
	}

	public static abstract class MyRunnable implements Runnable, Cloneable
	{
		protected int i;
		public Object o;
		public Future f;
		public boolean cancel = false;

		public Object clone() throws CloneNotSupportedException
		{
			Object o2 = super.clone();
			return o2;
		}
	}

	public static void foreach(List<?> list, MyRunnable r)
	{
		for (Object o : list)
		{
			MyRunnable clone;
			try
			{
				clone = (MyRunnable) r.clone();
				clone.o = o;
				clone.run();
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void fork(List<?> list, MyRunnable r)
	{
		fork(list, false, false, r);
	}

	public static void fork(List<?> list, boolean report, MyRunnable r)
	{
		fork(list, report, false, r);
	}

	public static void fork(List<?> list, boolean report, boolean forkSlowly, MyRunnable r)
	{
		MyFutureList futures = new MyFutureList();
		if (list.size() == 0)
			return;
		if (list.size() == 1)
		{
			MyRunnable clone;
			try
			{
				clone = (MyRunnable) r.clone();
				clone.o = list.get(0);
				clone.run();
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
			return;
		}
		for (int i = 0; i < list.size(); i++)
		{
			if (forkSlowly)
				while (getTaskCount() > threads / 2)
					EwThreading.sleep(10);
			MyRunnable clone;
			try
			{
				clone = (MyRunnable) r.clone();
				clone.o = list.get(i);
				clone.i = i;
				futures.add(fork(clone));
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
		futures.nowPause(report);
	}

	public static void fork(int min, int lessthan, boolean report, MyRunnable r)
	{
		MyFutureList futures = new MyFutureList();
		for (int i = min; i < lessthan; i++)
		{
			MyRunnable clone;
			try
			{
				clone = (MyRunnable) r.clone();
				clone.i = i;
				futures.add(fork(clone));
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
		futures.nowPause(report);
	}

	public static void invokeLater(Runnable runnable)
	{
		ThreadPoolExecutor tps = getTps();
		if (tps == null)
			tps = startThreadPool();
		try
		{
			tps.submit(runnable);
		}
		catch (RejectedExecutionException e)
		{
			invokeLater(runnable);
		}
	}

	public static void execSynchronous(MyFutureList placeToAdd, MyRunnable myRunnable)
	{
		myRunnable.run();
	}

}
