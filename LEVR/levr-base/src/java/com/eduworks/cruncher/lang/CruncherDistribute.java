package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.threading.EwDistributedThreading;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.mapreduce.JobStatus;
import com.eduworks.mapreduce.MapReduceClient;
import com.eduworks.mapreduce.MapReduceListener;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolvable;
import com.eduworks.resolver.ResolverFactory;
import com.eduworks.resolver.exception.SoftException;

public class CruncherDistribute extends Cruncher
{
	public static class DistributePacket implements Serializable
	{
		public String json;
		public Map<String, String[]> parameters;
	}

	static
	{
		hasInit = false;
		init();
		EwDistributedThreading.heartbeat();
	}

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		EwDistributedThreading.heartbeat();

		Resolvable call = (Resolvable) get("obj");
		int threadBoost = optAsInteger("threadBoost",-1,c,parameters,dataStreams);
		if (threadBoost != -1)
			EwThreading.setThreadCount(threadBoost);

		String serialized = call.toOriginalJson();
		try
		{
			MapReduceClient workforce = EwDistributedThreading.acquireWorkforce("cruncherDistribute");
			DistributePacket dp = new DistributePacket();
			dp.json = serialized;
			dp.parameters = parameters;
			Object result = workforce.mapReduce(dp);

			if (result instanceof List)
			{
				List<Object> mapReduce = (List<Object>) result;
				for (int i = 0; i < mapReduce.size(); i++)
				{
					if (mapReduce.get(i).toString().isEmpty())
						continue;
					if (mapReduce.get(i).toString().startsWith("{"))
						try
						{
							mapReduce.set(i, mapReduce.get(i));
						}
						catch (Exception e)
						{

						}
					if (mapReduce.get(i).toString().startsWith("["))
						try
						{
							mapReduce.set(i, mapReduce.get(i));
						}
						catch (Exception ex)
						{

						}
				}
				return new JSONArray(mapReduce);
			}
			else
				return result;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
finally{}
	}

	public static boolean hasInit = false;

	public static synchronized void init()
	{
		if (hasInit)
			return;
		hasInit = true;
		try
		{
			EwDistributedThreading.subscribe("cruncherDistribute", (short) 13424, new MapReduceListener()
			{

				@Override
				public Object go(JobStatus key) throws RemoteException
				{
					Context c = new Context();
					try
					{
						DistributePacket p = (DistributePacket) key.getObject();
						Resolvable r = (Resolvable) ResolverFactory.create(new JSONObject(p.json));
						Map<String, String[]> params = p.parameters;
						params.put("i", new String[] { Integer.toString(key.getI()) });
						params.put("mod", new String[] { Integer.toString(key.getMod()) });
						Object resolve = r.resolve(c, params, new HashMap<String, InputStream>());
						if (resolve instanceof Resolvable)
							resolve = ((Resolvable) resolve).toJson();
						c.success();
						c.finish();
						return resolve;
					}
					catch (JSONException e)
					{
						c.failure();
						e.printStackTrace();
						throw new SoftException(e.getMessage());
					}
					finally
					{
						c.finish();
					}
				}
			});
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String getDescription()
	{
		return "Performs some gargantuan operation over many machines, using i and mod parameters and remote method invocations. "
				+ "Returns the various slice results from each machine." + "\nslices - Number of pieces to divide the problem into."
				+ "\nobj - Resolver to transmit and invoke.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_PROPRIETARY;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "Resolvable", "slices", "Number");
	}

}
