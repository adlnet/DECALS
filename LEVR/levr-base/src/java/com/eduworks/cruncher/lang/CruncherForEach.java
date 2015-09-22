package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.lang.EwList;
import com.eduworks.lang.EwMap;
import com.eduworks.lang.EwStringUtil;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyFutureList;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.exception.SoftException;

public class CruncherForEach extends Cruncher
{

	@Override
	public Object resolve(Context c, final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams)
			throws JSONException
	{
		boolean threaded = optAsBoolean("threaded", true, c, parameters, dataStreams);
		Object obj = getObj(c, parameters, dataStreams);
		verifyCloneMode(this);
		final String paramName = optAsString("paramName", "eachId", c, parameters, dataStreams);
		final String prevParamName = optAsString("prevParamName", null, c, parameters, dataStreams);
		final String valueName = getAsString("valueName", c, parameters, dataStreams);
		final String extraParamName = optAsString("extraParamName", null, c, parameters, dataStreams);
		final boolean memorySaver = optAsBoolean("memorySaver", false, c, parameters, dataStreams);
		final int threadCap = optAsInteger("threadCap", Integer.MAX_VALUE, c, parameters, dataStreams);
		final Integer cap = Integer.parseInt(optAsString("cap", "-1", c, parameters, dataStreams));
		if (cap > 0)
			threaded = false;
		final boolean rethrow = optAsBoolean("rethrow", false, c, parameters, dataStreams);
		String extraParam = null;
		if (extraParamName != null)
			extraParam = get("extraParam", c, parameters, dataStreams).toString();

		if (obj instanceof JSONObject)
		{
			return executeJsonObject(c,parameters, dataStreams, threaded, obj, paramName, valueName, prevParamName,
					extraParamName, extraParam, memorySaver, rethrow,cap,threadCap);
		}
		else if (obj instanceof JSONArray)
		{
			return executeJsonArray(c,parameters, dataStreams, threaded, obj, paramName, prevParamName, extraParamName,
					extraParam, memorySaver, rethrow,cap,threadCap);
		}
		else if (obj instanceof EwList)
		{
			obj = new JSONArray((EwList) obj);
			return executeJsonArray(c,parameters, dataStreams, threaded, obj, paramName, prevParamName, extraParamName,
					extraParam, memorySaver, rethrow,cap,threadCap);
		}
		return null;
	}

	public Object executeJsonObject(final Context c,final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams,
			final boolean threaded, Object obj, final String paramName, final String valueName, final String prevParamName,
			final String extraParamName, final String extraParam, final boolean memorySaver, final boolean rethrow,final Integer cap, final int threadCap)
			throws JSONException
	{
		final JSONObject output = new JSONObject();
		final JSONArray outputArray = new JSONArray();
		MyFutureList fl = new MyFutureList();
		JSONObject json = (JSONObject) obj;
		Iterator<String> keys = json.keys();
		String prevId = null;
		int counter = 0;
		int sequenceI = Integer.parseInt(optAsString("sequenceI", "-1", c, parameters, dataStreams));
		int sequenceMod = Integer.parseInt(optAsString("sequenceMod", "-1", c, parameters, dataStreams));
		while (keys.hasNext() && (cap == -1 || output.length() < cap))
		{
			if (c.shouldAbort()) return null;
			final String key = keys.next();
			final Object value = json.get(key);
			final String prevIdFinal = prevId;

			final int index = counter;
			counter++;
			if (counter % 1000 == 0)
				log.debug(EwStringUtil.tabs(EwThreading.getThreadLevel()) + ": On " + counter + "/" + json.length());
			MyRunnable r = new MyRunnable()
			{
				@Override
				public void run()
				{
					try
					{
						if (threadCap != Integer.MAX_VALUE)
						EwThreading.setThreadCount(threadCap);
						final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
						newParams.put(paramName, new String[] { key });

						if (prevParamName != null)
							newParams.put(prevParamName, new String[] { prevIdFinal });
						String valueString=null;
						if (valueName != null && value != null)
						{
							valueString = value.toString();
							newParams.put(valueName, new String[] { valueString });
							newParams.put("i", new String[] { Integer.toString(index) });
							c.put(valueString, value);
						}
						if (extraParamName != null)
							newParams.put(extraParamName, new String[] { extraParam });
						Object result = resolveAChild("op", c,newParams, dataStreams);
						if (valueString != null)
							c.remove(valueString);
						if (result instanceof EwJsonSerializable)
							result = ((EwJsonSerializable) result).toJsonObject();
						if (!memorySaver)
							synchronized (output)
							{
								output.put(key, result);
								outputArray.put(index, result);
							}
						if (threaded)
							clearThreadCache();
					}
					catch (JSONException e)
					{
						if (rethrow)
							throw new RuntimeException(e);
						else
							e.printStackTrace();
					}
					catch (Exception ex)
					{
						if (rethrow)
							throw new RuntimeException(ex);
						else
							ex.printStackTrace();
					}
				}
			};
			if (sequenceI == -1 || sequenceMod == -1 || counter % sequenceMod == sequenceI)
				if (threaded)
				{
					EwThreading.forkAccm(fl, true, r,threadCap);
				}
				else
				{
					r.run();
				}
			prevId = key;
		}
		fl.nowPause(true);
		if (optAsBoolean("array", false, c, parameters, dataStreams))
		{
			JSONArray ja = new JSONArray();
			for (int i = 0;i < outputArray.length();i++)
				if (!outputArray.isNull(i))
					ja.put(outputArray.get(i));
			return ja;
		}
		if (optAsBoolean("soft", false, c, parameters, dataStreams))
			if (output.length() == 0)
				return null;
		return output;
	}

	public Object executeJsonArray(final Context c,final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams,
			boolean threaded, Object obj, final String paramName, final String prevParamName,
			final String extraParamName, final String extraParam, final boolean memorySaver, final boolean rethrow,final Integer cap, final int threadCap)
			throws JSONException
	{
		final JSONObject output = new JSONObject();
		final JSONArray outputArray = new JSONArray();
		MyFutureList fl = new MyFutureList();
		JSONArray json = (JSONArray) obj;
		String prevId = null;
		int sequenceI = Integer.parseInt(optAsString("sequenceI", "-1", c, parameters, dataStreams));
		int sequenceMod = Integer.parseInt(optAsString("sequenceMod", "-1", c, parameters, dataStreams));
		for (int i = 0; i < json.length() && (cap == -1 || output.length() < cap); i++)
		{
			if (c.shouldAbort()) return null;
			final Object keyRaw = json.get(i);
			final String key = keyRaw.toString();
			final Object value = json.get(i);
			final String prevIdFinal = prevId;
			final int index = i;
			if (i > 0 && i % 1000 == 0)
				log.debug(EwStringUtil.tabs(EwThreading.getThreadLevel()) + ": On " + i + "/" + json.length());
			if (sequenceI == -1 || sequenceMod == -1 || i % sequenceMod == sequenceI)
				if (threaded)
				{
					EwThreading.forkAccm(fl, false, new MyRunnable()
					{
						@Override
						public void run()
						{
							try
							{
								if (threadCap != Integer.MAX_VALUE)
								EwThreading.setThreadCount(threadCap);
								final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
								newParams.put(paramName, new String[] { key });
								c.put(key, keyRaw);
								if (prevParamName != null)
									newParams.put(prevParamName, new String[] { prevIdFinal });
								if (extraParamName != null)
									newParams.put(extraParamName, new String[] { extraParam });
								newParams.put("i", new String[] { Integer.toString(index) });
								int tryCount = 0;
								boolean keepTrying = true;
								Object result = null;
								while (keepTrying)
									try
									{
										keepTrying = false;
										result = resolveAChild("op", c,newParams, dataStreams);
									}
									catch (SoftException ex)
									{
										tryCount++;
										if (tryCount < 30)
											keepTrying = true;
									}
								c.remove(key);
								if (result instanceof EwJsonSerializable)
									result = ((EwJsonSerializable) result).toJsonObject();
								if (!memorySaver)
									synchronized (output)
									{
										if (optAsString("countInstances", "false", c, parameters, dataStreams).equals(
												"true"))
										{
											if (result instanceof JSONObject)
											{
												int count = 0;
												if (output.has(key))
													count = output.getJSONObject(key).optInt("count", 0);
												((JSONObject) result).put("count", ++count);
											}
										}
										output.put(key, result);
										outputArray.put(index, result);
									}
									clearThreadCache();
							}
							catch (JSONException e)
							{
								if (rethrow)
									throw new RuntimeException(e);
								else
									e.printStackTrace();
							}
							catch (Exception ex)
							{
								if (rethrow)
									throw new RuntimeException(ex);
								else
									ex.printStackTrace();
							}
						}
					},threadCap);
				}
				else
				{
					try
					{
						final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
						newParams.put(paramName, new String[] { key });
						c.put(key, keyRaw);
						if (prevParamName != null)
							newParams.put(prevParamName, new String[] { prevIdFinal });
						if (extraParamName != null)
							newParams.put(extraParamName, new String[] { extraParam });
						newParams.put("i", new String[] { Integer.toString(index) });
						Object result = resolveAChild("op", c,newParams, dataStreams);
						c.remove(key);
						if (!memorySaver)
						{
							if (optAsString("countInstances", "false", c, parameters, dataStreams).equals("true"))
							{
								if (result instanceof JSONObject)
								{
									int count = 0;
									if (output.has(key))
										count = output.getJSONObject(key).optInt("count", 0);
									((JSONObject) result).put("count", ++count);
								}
							}
							output.put(key, result);
							outputArray.put(index, result);
						}
						if (threaded)
							clearThreadCache();
					}
					catch (Exception ex)
					{
						if (rethrow)
							throw new RuntimeException(ex);
						else
							ex.printStackTrace();
					}
				}
			prevId = key;
		}
			fl.nowPause(true);

		if (optAsBoolean("array", false, c, parameters, dataStreams))
		{
			JSONArray results = new JSONArray();
			for (int i = 0; i < outputArray.length(); i++)
				if (!outputArray.isNull(i))
					results.put(outputArray.get(i));
			return results;

		}
		if (optAsBoolean("soft", false, c, parameters, dataStreams))
			if (output.length() == 0)
				return null;
		return output;
	}

	private boolean verifyCloneMode(Object o) throws JSONException
	{
		if (o instanceof Resolver)
		{
			Resolver r = (Resolver) o;
			for (String key : r.keySet())
				verifyCloneMode(r.get(key));
			return true;
		}
		if (o instanceof Cruncher)
		{
			Cruncher c = (Cruncher) o;
			if (c.resolverCompatibilityReplaceMode == true)
				return true;
			boolean replaceMode = false;
			for (String key : c.keySet())
			{
				if (verifyCloneMode(c.get(key)))
					replaceMode = true;
			}
			c.resolverCompatibilityReplaceMode = replaceMode;
			return replaceMode;
		}
		return false;
	}

	@Override
	public String getDescription()
	{
		return "Iterates over a collection.\n"
				+ "If an array or list, places each object into the @parameters (under paramName) and performs 'op'\n"
				+ "If a JSONObject, places the key and value into the @parameters (under paramName and valueName) and performs 'op'\n"
				+ "Other parameters set: i (index)\n" + "Switches: \n"
				+ "array='false' (Returns only the results of 'op')\n"
				+ "threaded='true' (Performs operations simultaneously)\n"
				+ "memorySaver='false' (Discards results of 'op')\n"
				+ "soft='false' (Returns a null value instead of an empty array)\n" + "op= (Operation to perform)\n";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray|JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "JSONArray|JSONObject", "paramName", "String", "valueName", "String", "op", "Resolvable",
				"?array", "Boolean", "?threaded", "Boolean", "?memorySaver", "Boolean", "?soft", "Boolean");
	}
}
