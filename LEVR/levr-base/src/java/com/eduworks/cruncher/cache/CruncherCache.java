package com.eduworks.cruncher.cache;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherCache extends Cruncher
{
	public static Map<String, Object> obj = Collections.synchronizedMap(new HashMap<String, Object>());

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String cacheName = "cruncherCache"+getAsString("name", c, parameters, dataStreams);
		Object result = null;
		Object lock = null;
		if (optAsBoolean("removeAllGlobal", false, c, parameters, dataStreams))
		{
			Resolver.clearCache();
			return null;
		}
		synchronized (getClass())
		{
			lock = obj.get(cacheName);
			if (lock == null)
			{
				obj.put(cacheName, lock = new Object());
			}
		}
		synchronized (lock)
		{
			if (optAsBoolean("remove", false, c, parameters, dataStreams))
			{
				if (optAsBoolean("global", false, c, parameters, dataStreams))
					EwCache.getCache("GlobalCache").remove(cacheName);
				else
					c.remove(cacheName);
			}
			else
			{
				if (optAsBoolean("global", false, c, parameters, dataStreams))
					result = EwCache.getCache("GlobalCache").get(cacheName);
				else
					result = c.get(cacheName);
				if (result == null)
				{
					result = getObj(c, parameters, dataStreams);
					if (optAsBoolean("global", false, c, parameters, dataStreams))
						EwCache.getCache("GlobalCache").put(cacheName, result);
					else
						c.put(cacheName, result);
				}
			}
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Caches a result, and fetches it automatically if it is in cache. Use Name to specify cache key.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "Object", "name", "String");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

}
