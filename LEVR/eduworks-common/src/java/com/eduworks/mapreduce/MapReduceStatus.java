package com.eduworks.mapreduce;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ConcurrentHashMap;

public class MapReduceStatus
{
	public enum STATE
	{
		OK, FAILED, IN_QUESTION
	};

	public MapReduceTransport transport = null;
	String host;
	private String name;
	private int I;
	private short port;

	static ConcurrentHashMap<String, Integer> hostLoad = new ConcurrentHashMap<String, Integer>();
	static ConcurrentHashMap<String, STATE> hostState = new ConcurrentHashMap<String, STATE>();

	@Override
	public String toString()
	{
		return host + ":" + port + " #" + I + " " + getState();
	}

	public void setState(STATE state)
	{
		hostState.putIfAbsent(host, STATE.IN_QUESTION);
		hostState.put(host, state);
	}

	public void setI(int i)
	{
		this.I = i;
	}

	public STATE getState()
	{
		hostState.putIfAbsent(host, STATE.IN_QUESTION);
		return hostState.get(host);
	}

	public boolean notOK()
	{
		return getState() != STATE.OK;
	}

	public void setHost(String host, short port)
	{
		this.host = host;
		this.port = port;
	}

	public void setServiceName(String name)
	{
		this.name = name;
	}

	public MapReduceTransport getInterface() throws RemoteException, NotBoundException
	{
		if (transport != null && getState() == STATE.OK)
			return transport;
		Registry registry = LocateRegistry.getRegistry(host, port);
		transport = (MapReduceTransport) registry.lookup(name);
		return transport;
	}

	public void incrementWorkload()
	{
		hostLoad.putIfAbsent(host, 0);
		boolean success = false;
		while (!success)
		{
			Integer oldValue = hostLoad.get(host);
			success = hostLoad.replace(host, oldValue, oldValue + 1);
		}
	}

	public void decrementWorkload()
	{
		hostLoad.putIfAbsent(host, 0);
		boolean success = false;
		while (!success)
		{
			Integer oldValue = hostLoad.get(host);
			success = hostLoad.replace(host, oldValue, oldValue - 1);
		}
	}

	public static Integer getWorkload(String host)
	{
		Integer integer = hostLoad.get(host);
		if (integer == null)
			return 0;
		return integer;
	}

}
