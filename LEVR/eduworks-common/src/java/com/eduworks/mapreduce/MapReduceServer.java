package com.eduworks.mapreduce;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

public class MapReduceServer
{

	public Logger				log			= Logger.getLogger(MapReduceManager.class);
	public MapReduceListener	shipping;
	public boolean				isChecking	= false;
	public boolean				debug		= true;
	public long					lastServiceMs;
	public Short				port;
	public String				name;

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

	public MapReduceServer(String name, Short port, MapReduceListener myListener) throws RemoteException,
			AlreadyBoundException
	{
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
				log.info("Binding to port " + port + " w/name: " + name);
			Registry registry = LocateRegistry.createRegistry(port);
			log.debug(registry.toString());
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
	}

}
