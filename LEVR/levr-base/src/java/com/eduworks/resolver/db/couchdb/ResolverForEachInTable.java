package com.eduworks.resolver.db.couchdb;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ace.product.levr.adapter.DocumentDbInterface;
import com.eduworks.lang.EwList;
import com.eduworks.lang.EwMap;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyFutureList;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolvable;
import com.eduworks.util.Tuple;

public class ResolverForEachInTable extends ResolverDocument
{
	private static final Logger	LOGGER	= Logger.getLogger(ResolverForEachInTable.class);


	@Override
	public Object resolve(final Context c, Map<String, String[]> parameters, final Map<String, InputStream> dataStreams)
			throws JSONException
	{
		final boolean memorySaver = optAsBoolean("memorySaver", false, parameters);
		for (String key : keySet())
			if (isSetting(key) && !key.equals("op") && !key.equals("_op"))
				resolveAChild(c,parameters, dataStreams, key);
		int count = optAsInteger("count", Integer.MAX_VALUE, parameters);
		String start = optAsString("start", parameters);
		int startCount = optAsInteger("startCount", 0, parameters);
		final boolean multiThread = optAsBoolean("threaded", false, parameters);
		if (start == null)
			start = "";
		String paramName = optAsString("paramName", parameters);
		if (paramName == null || paramName.isEmpty())
			paramName = "documentId";
		Tuple<Integer, List<String>> allDocumentIds = DocumentDbInterface.getAllDocumentIds(this, start,
				Math.min(count, 10000), parameters);
		int max = allDocumentIds.getFirst();
		Resolvable operation = (Resolvable) opt("op", parameters);
		MyFutureList mfl = new MyFutureList();
		remove("op");
		final JSONObject me = this;
		int i = 0;
		String lastId = null;
		long zero = System.currentTimeMillis();
		int sequenceI = optAsInteger("sequenceI", -1, parameters);
		int sequenceMod = optAsInteger("sequenceMod", -1, parameters);
		while (i < count && i < max)
		{
			List<String> ids = allDocumentIds.getSecond();
			for (final String id : ids)
			{
				i++;
				if (i % 100 == 0)
				{
					if (i < startCount)
						LOGGER.info("FAST FORWARDING: On " + i + "/" + Math.min(count, max) + "(" + ((double) i)
								/ ((double) Math.min(count, max)) + ") ");
					else
					{
						long current = System.currentTimeMillis();
						long future = (long) ((((((double) Math.min(count-startCount, max)) / ((double) i)) - 1.0) * (current - zero)) + current);
						String stuff = "Started: " + new Date(zero).toString() + " Estd Done: "
								+ new Date(future).toString();
						LOGGER.info("ForEach: On " + i + "/" + Math.min(count, max) + "(" + ((double) i)
								/ ((double) Math.min(count, max)) + ") " + stuff);
					}
				}
				if (id.startsWith("_"))
					continue;
				if (operation == null)
				{
					me.put(id, "ok");
					continue;
				}
				if (sequenceI == -1 || sequenceMod == -1 || i % sequenceMod == sequenceI)
				{
					if (i >= startCount)
						{
							final Resolvable thing = (Resolvable) operation;
							try
							{
								final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
								newParams.put(paramName, new String[] { id });
								while (EwThreading.getTaskCount() > 1000)
									EwThreading.sleep(1000);
								MyRunnable r = new MyRunnable()
								{
									int	attempt	= 0;

									@Override
									public void run()
									{
										try
										{
											Object resolve = ((Resolvable)thing.clone()).resolve(c, newParams, dataStreams);
											if (!memorySaver)
												synchronized(me)
												{
													me.put(id, resolve);
												}
										}
										catch (JSONException e)
										{
											e.printStackTrace();
											if (attempt++ < 30)
												run();
										}
										catch (CloneNotSupportedException e)
										{
											e.printStackTrace();
										}
										if (multiThread)
											clearThreadCache();
									}
								};
								if (multiThread)
								{
									Future<?> f = EwThreading.fork(true, r);
									if (i > Math.min(count, max) - 1000)
										mfl.add(f);
								}
								else
									r.run();
								// "ok");
								// }
							}
							finally
							{
							}
						}
					else
						zero = System.currentTimeMillis();
				}
				lastId = id;
			}
			start += ids.size();
			allDocumentIds = DocumentDbInterface
					.getAllDocumentIds(this, lastId, Math.min(count - i, 10000), parameters);
		}
		if (multiThread)
			mfl.nowPause(true);
		String aggregationMode = optAsString("aggregation", parameters);
		removeAllSettings();
		remove("_id");
		remove("aggregation");
		if (aggregationMode == null)
			return this;
		if (aggregationMode.equals("max"))
		{
			Double maxa = -999999.0;
			for (String key : keySet())
				maxa = Math.max(getAsDouble(key, parameters), maxa);
			return maxa;
		}
		else if (aggregationMode.equals("append"))
		{
			StringBuilder str = new StringBuilder();
			for (String key : keySet())
				if (str.length() == 0)
					str.append(getAsString(key, parameters));
				else
					str.append(" " + getAsString(key, parameters));
			return str;
		}
		else if (aggregationMode.equals("values"))
		{
			List<Object> results = new EwList<Object>();
			for (String key : keySet())
				results.add(get(key, parameters));
			return results;
		}
		else if (aggregationMode.equals("keys"))
		{
			List<Object> results = new EwList<Object>();
			for (String key : keySet())
				results.add(key);
			return results;
		}
		return this;
	}

	@Override
	public String getDescription()
	{
		return "Iterates over a CouchDB document store. Performs 'op' over each item in the database." +
				"\nThe endpoint is defined by _serverHostname, _serverPort, _serverLogin, _serverPassword, _databasePrefix and _databaseName" +
				"\nparamName - The name of the @parameter to place the key of the document in." +
				"\nop - The operation to perform on each document." +
				"\n(Optional)memorySaver - Determines if the results should be saved. Defaults to false." +
				"\n(Optional)count - Stop executing after this amount." +
				"\n(Optional)start - Start executing on the document with this Id." +
				"\n(Optional)startCount - Start executing after this amount." +
				"\n(Optional)threaded - Operate on multiple items simultaneously. Defaults to false." +
				"\n(Optional)sequenceI - Operate on items where i % sequenceMod == sequenceI." +
				"\n(Optional)sequenceMod - See above" +
				"\n(Optional)aggregation - Aggregate the results in different ways. See below:" +
				"\n\tmax - Get the maximum value returned." +
				"\n\tappend - Along with 'delimiter', append the results into a string." +
				"\n\tvalues - Return only the results of op." +
				"\n\tkeys - Return only the keys of the documents affected.";
	}

	@Override
	public String[] getResolverNames()
	{
		return new String[] { getResolverName(), "foreachInTable" };
	}
	
	@Override
	public String getReturn()
	{
		return "JSONObject|JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("_serverHostname","String","_serverPort","Number","_serverLogin","String","_serverPassword","String","?_databasePrefix","String","_databaseName","String","paramName","String","op","Resolvable","?aggregation","String","?memorySaver","Boolean","?count","Number","?start","String","?startCount","Number","?threaded","Boolean","?sequenceI","Number","?sequenceMod","Number");
	}

	
}
