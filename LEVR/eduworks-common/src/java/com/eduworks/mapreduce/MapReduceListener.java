package com.eduworks.mapreduce;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class MapReduceListener extends UnicastRemoteObject implements MapReduceTransport
{
	protected MapReduceListener() throws RemoteException
	{
		super();
	}
	
	public boolean ping() throws RemoteException
	{
		return true;
	}
}
