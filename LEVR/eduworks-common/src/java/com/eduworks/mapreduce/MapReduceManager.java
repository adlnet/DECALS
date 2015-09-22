package com.eduworks.mapreduce;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.eduworks.lang.EwList;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.util.Tuple;

public class MapReduceManager
{
	public Logger					log			= Logger.getLogger(MapReduceManager.class);
	public List<MapReduceStatus>	peers		= Collections.synchronizedList(new EwList<MapReduceStatus>());
	public MapReduceTransport		shipping;
	public boolean					isChecking	= false;
	public boolean					debug		= true;
	public long						lastServiceMs;
	private Short					port;
	private String					name;

	public void cleanup()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry(port);
			try
			{
				registry.unbind(name);
			}
			catch (NotBoundException e)
			{
				e.printStackTrace();
			}
			UnicastRemoteObject.unexportObject(shipping, true);
		}
		catch (Exception e)
		{

		}
	}

	public MapReduceManager(String name, Short port, List<Tuple<String, Short>> hosts, MapReduceListener myListener)
			throws RemoteException, AlreadyBoundException
	{
		peers.clear();
		this.port = port;
		this.name = name;

		try
		{
			myListener = (MapReduceListener) UnicastRemoteObject.exportObject(myListener, 0);
		}
		catch (Exception e)
		{
		}
		this.shipping = myListener;

		try
		{
		if (debug)
			log.info("Binding to port " + port);
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind(name, myListener);
		if (debug)
			log.info("Binding complete.");
		}
		catch (ExportException ex)
		{
			log.info("Already bound.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		int i = 0;
		for (Tuple<String, Short> host : hosts)
		{
			MapReduceStatus status = new MapReduceStatus();
			status.setHost(host.getFirst(), host.getSecond());
			status.setState(MapReduceStatus.STATE.IN_QUESTION);
			status.setI(i++);
			status.setServiceName(name);
			peers.add(status);

			if (debug)
				log.info("Established Peer: " + status);
		}
	}

	private void invokeCheckup()
	{
		// if (isChecking == true)
		// return;
		// isChecking = true;
		// EwThreading.invokeLater(new Runnable()
		// {
		// public void run()
		// {
		checkup();
		// isChecking = false;
		// }
		// });
	}

	public void checkup()
	{
		synchronized (peers)
		{
			for (MapReduceStatus s : peers)
			{
				if (!s.notOK())
					continue;
				try
				{
					s.getInterface().ping();
					s.setState(MapReduceStatus.STATE.OK);
					if (debug)
						log.info("Established connection to: " + s);
				}
				catch (RemoteException e)
				{
					s.setState(MapReduceStatus.STATE.FAILED);
					if (debug)
						log.info("Lost connection with: " + s);
				}
				catch (NotBoundException e)
				{
					s.setState(MapReduceStatus.STATE.FAILED);
					if (debug)
						log.info("Lost connection with: " + s);
				}
			}
		}
	}

	public List<Object> mapReduce(Object bo)
	{
		List<Object> map = map(bo);
		EwList<Object> results = new EwList<Object>();
		for (Object o : map)
		{
			if (o instanceof Collection<?>)
				results.addAll((Collection<?>) o);
		}
		return results;
	}

	public List<Object> map(Object o)
	{
		lastServiceMs = System.currentTimeMillis();
		if (this.peers.size() == 0)
		{
			JobStatus jobStatus = new JobStatus();
			jobStatus.setI(0);
			jobStatus.setMod(1);
			jobStatus.setObject(o);
			jobStatus.setState(JobStatus.STATE.INCOMPLETE);
			try
			{
				List<Object> results = new EwList<Object>();
				results.add(shipping.go(jobStatus));
				lastServiceMs = System.currentTimeMillis()-lastServiceMs;
				return results;
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
				lastServiceMs = System.currentTimeMillis()-lastServiceMs;
				return null;
			}
		}
		else
		{
			invokeCheckup();
			LinkedHashMap<JobStatus, MapReduceStatus> job = new LinkedHashMap<JobStatus, MapReduceStatus>();
			while (!jobsDone(job))
			{
				populateJob(job, o);
				distributeJobs(job);
			}

			List<Object> results = new EwList<Object>();
			for (JobStatus j : job.keySet())
			{
				results.add(j.getObject());
			}
			lastServiceMs = System.currentTimeMillis()-lastServiceMs;
			return results;
		}
	}

	private void distributeJobs(LinkedHashMap<JobStatus, MapReduceStatus> job)
	{
		EwThreading.fork(new EwList<Entry<JobStatus, MapReduceStatus>>(job.entrySet()), new MyRunnable()
		{
			public void run()
			{
				try
				{
					Entry<JobStatus, MapReduceStatus> entry = (Entry<JobStatus, MapReduceStatus>) o;
					if (entry.getKey().isComplete())
						return;
					long ms = System.currentTimeMillis();
					try
					{
						entry.getKey().setObject(entry.getValue().getInterface().go(entry.getKey()));
						if (entry.getKey().getObject() != null)
							entry.getKey().setState(JobStatus.STATE.COMPLETE);
						else
						{
							entry.getValue().setState(MapReduceStatus.STATE.IN_QUESTION);
							entry.getKey().setState(JobStatus.STATE.FAILED);
						}
						ms = System.currentTimeMillis() - ms;
						if (debug)
							log.info("Job " + entry.getKey() + " on " + entry.getValue() + " completed in " + ms
									+ " ms");
					}
					catch (RemoteException e)
					{
						entry.getValue().setState(MapReduceStatus.STATE.IN_QUESTION);
						ms = System.currentTimeMillis() - ms;
						e.printStackTrace();
						if (debug)
							log.info("Job " + entry.getKey() + " on " + entry.getValue() + " failed in " + ms + " ms");
					}
					catch (NotBoundException e)
					{
						entry.getValue().setState(MapReduceStatus.STATE.IN_QUESTION);
						ms = System.currentTimeMillis() - ms;
						if (debug)
							log.info("Job " + entry.getKey() + " on " + entry.getValue() + " failed in " + ms + " ms");
					}
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
		});
	}

	private boolean jobsDone(LinkedHashMap<JobStatus, MapReduceStatus> job)
	{
		if (job.size() == 0)
			return false;
		for (JobStatus j : job.keySet())
			if (!j.isComplete())
			{
				if (debug)
					log.info("!!! Not all tasks completed. Retrying.");
				return false;
			}
		return true;
	}

	private void populateJob(LinkedHashMap<JobStatus, MapReduceStatus> job, Object o)
	{
		int mod = peers.size();
		List<Integer> absent = new EwList<Integer>();
		for (int i = 0; i < peers.size(); i++)
		{
			if (containsCompleteJob(job, i))
			{
				continue;
			}
			if (containsFailedJob(job, i))
			{
				if (debug)
					log.info("Job #" + (i + 1) + " failed last time. Rerouting job.");
				absent.add(i);
				continue;
			}
			removeJob(job, i);
			JobStatus j = new JobStatus();
			j.setI(i);
			j.setObject(o);
			j.setMod(mod);
			j.setState(JobStatus.STATE.INCOMPLETE);
			if (peers.get(i).notOK())
			{
				if (debug)
					log.info("Peer #" + i + " is down. Rerouting job.");
				absent.add(i);
			}
			else
			{
				job.put(j, peers.get(i));

				if (debug)
					log.info("Assigning " + j + " to " + peers.get(i));
			}

		}
		if (job.size() == 0)
			throw new RuntimeException("Could not map reduce, don't have any valid peers.");

		removeFailedJobs(job);
		int i = 0;
		for (Integer absentI : absent)
		{
			while (peers.get(i % peers.size()).notOK())
				i++;
			MapReduceStatus peer = peers.get(i % peers.size());
			JobStatus j = new JobStatus();
			j.setI(absentI);
			j.setObject(o);
			j.setMod(mod);
			j.setState(JobStatus.STATE.INCOMPLETE);
			job.put(j, peer);
			if (debug)
				log.info("Rerouting: Assigning " + j + " to " + peer);
			i++;
		}
	}

	private void removeFailedJobs(LinkedHashMap<JobStatus, MapReduceStatus> job)
	{
		for (JobStatus j : new EwList<JobStatus>(job.keySet()))
			if (j.isFailed())
				job.remove(j);
	}

	private void removeJob(LinkedHashMap<JobStatus, MapReduceStatus> job, int i)
	{
		JobStatus target = null;
		for (JobStatus j : job.keySet())
			if (j.getI() == i)
				target = j;
		if (target != null)
			job.remove(target);
	}

	private boolean containsCompleteJob(LinkedHashMap<JobStatus, MapReduceStatus> job, int i)
	{
		for (JobStatus j : job.keySet())
			if (j.getI() == i && j.isComplete())
				return true;
		return false;
	}

	private boolean containsFailedJob(LinkedHashMap<JobStatus, MapReduceStatus> job, int i)
	{
		for (JobStatus j : job.keySet())
			if (j.getI() == i && j.isFailed())
				return true;
		return false;
	}

}
