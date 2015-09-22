package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.Fun;
import org.mapdb.Fun.Tuple2;
import org.mapdb.HTreeMap;

import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.lang.EwMap;
import com.eduworks.lang.EwStringUtil;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyFutureList;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.exception.SoftException;
import com.eduworks.util.io.EwDB;

public class CruncherIdxEach extends Cruncher
{

	public Object resolve(final Context c, final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		String dbIndex = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		Integer count = optAsInteger("count", Integer.MAX_VALUE, c, parameters, dataStreams);
		Integer start = optAsInteger("start", 0, c, parameters, dataStreams);
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);

		final String paramName = optAsString("paramName", "eachId", c, parameters, dataStreams);
		final String valueName = getAsString("valueName", c, parameters, dataStreams);
		final boolean rethrow = optAsBoolean("rethrow", false, c, parameters, dataStreams);
		boolean threaded = optAsBoolean("threaded", true, c, parameters, dataStreams);
		final boolean memorySaver = optAsBoolean("memorySaver", true, c, parameters, dataStreams);
		int sequenceI = Integer.parseInt(optAsString("sequenceI", "-1", c, parameters, dataStreams));
		int sequenceMod = Integer.parseInt(optAsString("sequenceMod", "-1", c, parameters, dataStreams));
		final ConcurrentHashMap<String,Object> output = new ConcurrentHashMap<String,Object>();
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);

			if (optCommit)
				ewDB.db.commit();

			final HTreeMap<Object, Object> hashMap = ewDB.db.getHashMap(dbIndex);
			Iterator<Object> it = hashMap.keySet().iterator();
			MyFutureList fl = new MyFutureList();
			int counter = 0;
			while (it.hasNext() && count-- > 0)
			{
				if (c.shouldAbort()) return null;
				final Object t = it.next();
				final int index = counter;
				counter++;
				if (counter % 1000 == 0)
				{
					log.debug(EwStringUtil.tabs(EwThreading.getThreadLevel()) + ": On " + counter + "/" + "?");
				}
				if (!(sequenceI == -1 || sequenceMod == -1 || counter % sequenceMod == sequenceI))
					continue;
				if (start-- > 0)
					continue;
				final String key = (String) t;

				MyRunnable r = new MyRunnable()
				{
					@Override
					public void run()
					{
						try
						{
							final Object value = hashMap.get(t);
							final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
							newParams.put(paramName, new String[] { key });
							String valueString=null;
							if (valueName != null && value != null)
							{
								valueString = value.toString();
								newParams.put(valueName, new String[] { valueString });
								newParams.put("i", new String[] { Integer.toString(index) });
								c.put(valueString, value);
							}
							Object result = resolveAChild("op", c, newParams, dataStreams);
							if (valueString != null)
								c.remove(valueString);
							if (result instanceof EwJsonSerializable)
								result = ((EwJsonSerializable) result).toJsonObject();
							if (!memorySaver)
								output.put(key, result);
						}
						catch (JSONException e)
						{
							if (rethrow)
								throw new RuntimeException(e);
							else
								e.printStackTrace();
						}
						catch (SoftException e)
						{
							EwThreading.sleep(100);
							run();
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
				if (threaded)
				{
					EwThreading.forkAccm(fl, true, r);
				}
				else
				{
					r.run();
				}
			}
			fl.nowPause(true);

			return new JSONObject(output);
		}

		finally
		{
			if (ewDB != null)
				ewDB.close();
		}
	}

	@Override
	public String getDescription()
	{
		return "Returns all keys of a string only on-disk multimap defined by indexDir+databaseName->index->key += value.";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("indexDir", "LocalPathString", "databaseName", "String", "index", "String");
	}

}
