package com.eduworks.resolver.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverRound extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		int places = optAsInteger("_places",0, parameters);
		resolveAllChildren(c, parameters, dataStreams);
		for (String key : keySet())
		{
			if (isSetting(key))
				continue;
			put(key,Math.pow(10,-places)*Math.round(Math.pow(10,places)*getAsDouble(key,parameters)));
		}
		removeAllSettings();
		if (length() == 0)
			return null;
		if (length() == 1)
			return get(keySet().iterator().next());
		return this;
	}

	@Override
	public String getDescription()
	{
		return "Rounds all provided numbers. Will return all numbers in a JSON Object if provided, one number if only one is provided." +
				"\nRounds to _places (default: 0 places, or whole numbers).";
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
		return jo("<any>","Number","?_places","Number");
	}
	

}
