package com.eduworks.resolver.math;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverSubtract extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		Double result = getAsDouble("operator", parameters);
		Iterator<String> i = this.keys();
		while (i.hasNext())
		{
			String key = i.next();
			if (key.equals("operator"))
				continue;
			Double value = getAsDouble(key, parameters);
			result -= value;
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Subtracts all provided number from 'operator'";
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
		return jo("operator","Number","<any>","Number");
	}

}
