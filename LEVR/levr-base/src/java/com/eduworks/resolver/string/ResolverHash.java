package com.eduworks.resolver.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverHash extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		String result = "";
		for (String key : keySet())
		{
			result += getAsString(key,parameters);
		}
		return Math.abs(result.hashCode());
	}

	@Override
	public String getDescription()
	{
		return "Hash one or more strings and return a resultant hashcode (based on Java String hash)";
	}

	@Override
	public String getReturn()
	{
		return "Number";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>","String");
	}

}
