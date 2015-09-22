package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherObject extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject result = new JSONObject();
		Iterator<String> keySet = sortedKeys();
//		if (optAsBoolean("_ordered",false,c,parameters, dataStreams))
//			keySet = sortedKeys();

		while (keySet.hasNext())
		{
			String key = keySet.next();

			final Object value = get(key, c, parameters, dataStreams);

			if (isSetting(key))
				continue;
			if (value == null)
				continue;
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
					log.error("Cannot put into JSONObject object of type: " + value.getClass().getSimpleName());
					throw ex2;
				}
			}
		}

		return result;
	}

	@Override
	public String getDescription()
	{
		return "Returns a JSONObject with the encapsulated data." +
				"\nNOTE: All operations are done in alphabetical order, EXCEPT when _ordered=false, " +
				"in which case they are done in the order specified by the JSON Resolver file.";
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
		return jo("<any>","Object","?_ordered","Boolean");
	}
}
