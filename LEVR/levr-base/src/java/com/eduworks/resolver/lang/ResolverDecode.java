package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverDecode extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		for (String key : keySet())
		{
			Object thing = get(key, parameters);
			if (thing instanceof String)
			{
				try
				{
					JSONArray jsonArray = new JSONArray(thing.toString());
					if (jsonArray.length() > 0)
						put(key, jsonArray);
				}
				catch (Exception ex)
				{

				}
				try
				{
					JSONObject jsonArray = new JSONObject(thing.toString());
					if (jsonArray.length() > 0)
						put(key, jsonArray);
				}
				catch (Exception ex)
				{

				}
			}
		}
		if (length() == 0)
			return null;
		if (length() == 1)
			return get(keySet().iterator().next(),parameters);
		return this;
	}

	@Override
	public String getDescription()
	{
		return "Decode any strings into JSON Objects or Arrays if possible.";
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
