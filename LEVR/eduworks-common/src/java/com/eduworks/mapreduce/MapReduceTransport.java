package com.eduworks.mapreduce;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MapReduceTransport extends Remote
{
	Object go(JobStatus key) throws RemoteException;

	boolean ping() throws RemoteException;
}
