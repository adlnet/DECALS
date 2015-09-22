package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverMatcher;
import com.eduworks.resolver.enumeration.ResolverMatchOption;

public class ResolverRemoveFromObject extends ResolverMatcher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		final EwJsonObject object = getObject(parameters, true, true);
		final Object item = getItem(parameters, true, false);

		final EwJsonCollection results;

		if (item != null)
		{
			setMatchCriteria(parameters, ResolverMatchOption.IN_ITEM);

			results = removeFrom(object, item);
		}
		else
		{
			results = new EwJsonObject();

			remove("obj");

			// Leave out keys specified in RSL
			for (String key : object.keySet())
				if (!has(key)) results.put(key, object.get(key));
		}

		if (optAsBoolean("required", false, parameters) && results.length() == object.length())
			throw new RuntimeException("Failed to remove an item, and removal is required.");

		return results;
	}

	@Override
	public String getDescription()
	{
		return "Removes a variable from an object defined by any parameters contained.";
	}

	@Override
	public String[] getResolverNames()
	{
		return new String[] { getResolverName(), "remove" };
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
		return jo("obj","JSONObject","?item","JSONObject","<any>","Object");
	}

}
