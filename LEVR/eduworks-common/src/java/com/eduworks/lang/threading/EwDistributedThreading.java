package com.eduworks.lang.threading;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eduworks.EwVersion;
import com.eduworks.mapreduce.MapReduceClient;
import com.eduworks.mapreduce.MapReduceListener;
import com.eduworks.mapreduce.MapReduceServer;
import com.eduworks.util.Tuple;

public class EwDistributedThreading
{
	static boolean debug = true;
	static DatagramSocket socket = null;
	static MulticastSocket mcsocket = null;
	static Logger log;
	static
	{
		log = Logger.getLogger(EwDistributedThreading.class);
		try
		{
			socket = new DatagramSocket(null);
			socket.bind(new InetSocketAddress(Inet4Address.getByName("0.0.0.0"), 4445));
			log.info("Bound " + Inet4Address.getByName("0.0.0.0") + " Datagram Socket to 4445.");
		}
		catch (SocketException e1)
		{
			e1.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		try
		{
			log.info("Attempting to bind Multicast Socket to 4446.");
			mcsocket = new MulticastSocket(4446);
			InetAddress group = InetAddress.getByName("224.0.121.0");
			mcsocket.joinGroup(group);
			log.info("Success.");
		}
		catch (IOException e)
		{
			log.info("Failed.");
			e.printStackTrace();
		}
		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				DatagramPacket packet;
				try
				{
					mcsocket.setBroadcast(true);
					mcsocket.setSoTimeout(90000);
				}
				catch (SocketException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				for (int i = 0;; i++)
				{
					byte[] buf = new byte[256];
					packet = new DatagramPacket(buf, buf.length);
					try
					{
						mcsocket.receive(packet);
						String received = new String(packet.getData(), Charset.defaultCharset());
						receiveBroadcast(packet.getAddress(), received);
					}
					catch (SocketTimeoutException ex)
					{

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					heartbeat();
				}
			}
		});
		t.start();
	}

	public static class Peer
	{
		long lastHeartbeat;
		long threadsInUse;
		String version;
		String ip;
		public String name;
		public short port;
	}

	static Map<String, Peer> peers = new HashMap<String, Peer>();

	protected static void receiveBroadcast(InetAddress inetAddress, String received)
	{
		String[] split = received.split("\t");
		String ip = inetAddress.getHostAddress();
		String receivedVersion = split[0];
		String receivedLoad = split[1];
		String receivedName = split[2];
		String receivedPort = split[3];

		String key = ip + receivedName + receivedPort;

		// log.debug("Received broadcast from " + ip + ": " + received);
		if (!peers.containsKey(key))
			peers.put(key, new Peer());
		Peer p = peers.get(key);
		p.ip = ip;
		p.lastHeartbeat = System.currentTimeMillis();
		p.version = receivedVersion;
		p.threadsInUse = Long.parseLong(receivedLoad);
		p.name = receivedName;
		p.port = Short.parseShort(receivedPort);
		heartbeat();
	}

	public static void sendBroadcast(String buf) throws IOException
	{
		sendBroadcast(buf.getBytes(Charset.defaultCharset()));
	}

	public static void sendBroadcast(byte[] buf) throws IOException
	{
		InetAddress group;
		group = InetAddress.getByName("224.0.121.0");
		DatagramPacket packet;
		packet = new DatagramPacket(buf, buf.length, group, 4446);
		socket.send(packet);
	}

	static long lastHeartbeat = 0;

	public static void heartbeat()
	{
		if (System.currentTimeMillis() - lastHeartbeat < 5000)
			return;
		// log.debug("Heartbeating.");
		lastHeartbeat = System.currentTimeMillis();
		try
		{
			
			String versionNumber = EwVersion.getVersion();
			String tasks = Long.toString(EwThreading.getTaskCount());
			if (mrs == null)
				mrs = new ArrayList<MapReduceServer>();
			for (MapReduceServer mr : mrs)
			{
				// log.debug("Heartbeating. Sending broadcast packet for " +
				// mr.name);
				sendBroadcast(versionNumber.trim() + "\t" + tasks + "\t" + mr.name + "\t" + mr.port + "\t");
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static MapReduceClient acquireWorkforce(String name) throws Exception
	{
		List<Peer> candidates = new ArrayList<Peer>();
		for (Peer p : peers.values())
			if (System.currentTimeMillis() - p.lastHeartbeat < 120000)
				if (name.equals(p.name))
					if (p.threadsInUse < 5)
						candidates.add(p);
		List<Tuple<String, Short>> hosts = new ArrayList<Tuple<String, Short>>();
		for (Peer p : candidates)
			hosts.add(new Tuple<String, Short>(p.ip, p.port));
		if (hosts.size() == 0)
			throw new Exception("No peers available.");
		MapReduceClient mrc = new MapReduceClient(name, hosts);
		return mrc;
	}

	static List<MapReduceServer> mrs = new ArrayList<MapReduceServer>();

	public static void subscribe(String name, short port, MapReduceListener mapReduceListener)
	{
		if (mrs == null)
			mrs = new ArrayList<MapReduceServer>();
		MapReduceServer mrm;
		try
		{
			log.debug("Subscribing new map reduce listener " + name);
			mrm = new MapReduceServer(name, port, mapReduceListener);
			mrs.add(mrm);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (AlreadyBoundException e)
		{
			e.printStackTrace();
		}
	}

}
