package com.eduworks.resolver.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverFloor extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		for (String key : keySet())
		{
			put(key,(int)Math.floor(getAsDouble(key,parameters)));
		}
		if (length() == 0)
			return null;
		if (length() == 1)
			return get(keySet().iterator().next());
		return this;
	}

	@Override
	public String getDescription()
	{
		return "Rounds down all numbers provided. Will return just the number if there is only one result.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject|Number";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>","Number");
	}
	
}
