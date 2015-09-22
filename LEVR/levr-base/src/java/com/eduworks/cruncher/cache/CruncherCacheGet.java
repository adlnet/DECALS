package com.eduworks.cruncher.cache;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherCacheGet extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		EwCache<Object,Object> cache = EwCache.getCache("CruncherCache", 5000);
		String threadId = parameters.get("threadId")[0];
		String name = getAsString("obj", c, parameters, dataStreams);
		return cache.get(threadId+name);
	}

	@Override
	public String getDescription()
	{
		return "Retreives a cached object under the name specified by obj. Cache the objects using #cache.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

}
