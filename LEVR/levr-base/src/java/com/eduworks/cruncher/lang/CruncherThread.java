package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyFutureList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherThread extends Cruncher
{

	@Override
	public Object resolve(final Context c, final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams)
			throws JSONException
	{
		final JSONObject result = new EwJsonObject();
		Iterator<String> keySet = sortedKeys();
		MyFutureList mfl = new MyFutureList();
		while (keySet.hasNext())
		{
			final String key = keySet.next();


			EwThreading.forkAccm(mfl, true, new EwThreading.MyRunnable()
			{
				@Override
				public void run()
				{
					try
					{
						Object value = null;
						value = get(key, c, parameters, dataStreams);
						if (isSetting(key))
							return;
						if (value == null)
							return;
						try
						{
							result.put(key, value);
						}
						catch (JSONException ex)
						{
							try
							{
								if (value instanceof Double)
									result.put(key, ((Double) value).doubleValue());
								else if (value instanceof Integer)
									result.put(key, ((Integer) value).intValue());
								else if (value instanceof Long)
									result.put(key, ((Long) value).longValue());
								else if (value instanceof Boolean)
									result.put(key, ((Boolean) value).booleanValue());
								else
									result.put(key, EwJson.wrap(value, true));
							}
							catch (JSONException ex2)
							{
								log.error("Cannot put into JSONObject object of type: "
										+ value.getClass().getSimpleName());
								throw ex2;
							}
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			});

		}
		mfl.nowPause(true);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Performs all actions simultaneously (in a threaded fashion) and returns a JSONObject with the results.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>","Object");
	}

}
