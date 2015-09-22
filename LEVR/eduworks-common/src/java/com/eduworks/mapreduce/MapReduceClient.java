package com.eduworks.mapreduce;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.eduworks.lang.EwList;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.util.Tuple;

public class MapReduceClient
{
	public Logger log = Logger.getLogger(MapReduceManager.class);
	public List<MapReduceStatus> peers = Collections.synchronizedList(new EwList<MapReduceStatus>());
	public boolean isChecking = false;
	public boolean debug = true;
	public long lastServiceMs;

	public void cleanup()
	{
	}

	public MapReduceClient(String name, List<Tuple<String, Short>> hosts)
	{
		peers.clear();

		int i = 0;
		for (Tuple<String, Short> host : hosts)
		{
			MapReduceStatus status = new MapReduceStatus();
			status.setHost(host.getFirst(), host.getSecond());
			status.setI(i++);
			status.setServiceName(name);
			peers.add(status);

			// if (debug)
			// log.info("Established Peer: " + status);
		}
	}

	public void checkup()
	{
		synchronized (peers)
		{
			for (MapReduceStatus s : peers)
			{
				checkup(s);
			}
		}
	}

	private void checkup(MapReduceStatus s)
	{
//		if (!s.notOK())
//			return;
		if (s.getState() == MapReduceStatus.STATE.OK)
			return;
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
				log.info("RE Lost connection with: " + s + ": " + e.getMessage());
		}
		catch (NotBoundException e)
		{
			s.setState(MapReduceStatus.STATE.FAILED);
			if (debug)
				log.info("NB Lost connection with: " + s + ": " + e.getMessage());
		}
	}

	public Object mapReduce(Object bo)
	{
		List<Object> map = map(bo);
		EwList<Object> results = new EwList<Object>();
		for (Object o : map)
		{
			if (o instanceof Collection<?>)
				results.addAll((Collection<?>) o);
			else
				results.add(o);
		}
		if (results.size() == 1)
			return results.get(0);
		return results;
	}

	public List<Object> map(Object o)
	{
		lastServiceMs = System.currentTimeMillis();
		if (this.peers.size() == 0)
		{
			log.error("No peers detected. Probably network issue.");
			return null;
		}
		else
		{
			LinkedHashMap<JobStatus, MapReduceStatus> job = new LinkedHashMap<JobStatus, MapReduceStatus>();

			while (!jobsDone(job))
			{
				if (o instanceof JSONArray)
					populateJobs(job, o);
				else if (o instanceof List)
					populateJobs(job, o);
				else
					populateJob(job, o);
				distributeJobs(job);
			}

			List<Object> results = new EwList<Object>();
			for (JobStatus j : job.keySet())
			{
				results.add(j.getObject());
			}
			lastServiceMs = System.currentTimeMillis() - lastServiceMs;
			return results;
		}
	}

	private void populateJob(LinkedHashMap<JobStatus, MapReduceStatus> job, Object o)
	{
		int i = 0;

		job.clear();
		while (job.size() == 0)
		{
			while (MapReduceStatus.getWorkload(peers.get(i % peers.size()).host) > 10 || peers.get(i % peers.size()).notOK())
			{
				if (i > peers.size())
					checkup(peers.get(i % peers.size()));
				i++;
				if (i % peers.size() == 0)
					EwThreading.sleep(100);
			}
			MapReduceStatus peer = peers.get(i % peers.size());
			JobStatus j = new JobStatus();
			j.setI(1);
			j.setObject(o);
			j.setMod(1);
			j.setState(JobStatus.STATE.INCOMPLETE);
			job.put(j, peer);
			if (debug)
				log.info("Rerouting: Assigning " + j + " to " + peer);
		}
	}

	private void distributeJobs(LinkedHashMap<JobStatus, MapReduceStatus> job)
	{
		MyRunnable r = new MyRunnable()
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
						try
						{
							entry.getValue().incrementWorkload();
							entry.getKey().setObject(entry.getValue().getInterface().go(entry.getKey()));
						}
						finally
						{
							entry.getValue().decrementWorkload();
						}
						if (entry.getKey().getObject() != null)
							entry.getKey().setState(JobStatus.STATE.COMPLETE);
						else
						{
							entry.getValue().setState(MapReduceStatus.STATE.IN_QUESTION);
							entry.getKey().setState(JobStatus.STATE.FAILED);
						}
						ms = System.currentTimeMillis() - ms;
						if (debug)
							log.info("Job " + entry.getKey() + " on " + entry.getValue() + " completed in " + ms + " ms");
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
		};
		if (job.size() == 1)
		{
			r.o = new EwList<Entry<JobStatus, MapReduceStatus>>(job.entrySet()).get(0);
			r.run();
		}
		else
			EwThreading.fork(new EwList<Entry<JobStatus, MapReduceStatus>>(job.entrySet()), r);
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

	private void populateJobs(LinkedHashMap<JobStatus, MapReduceStatus> job, Object o)
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
			while (MapReduceStatus.getWorkload(peers.get(i % peers.size()).host) > 10 && peers.get(i % peers.size()).notOK())
			{
				i++;
				if (i % peers.size() == 0)
					EwThreading.sleep(100);
			}
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
