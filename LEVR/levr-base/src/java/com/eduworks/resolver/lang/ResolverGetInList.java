package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverMatcher;

public class ResolverGetInList extends ResolverMatcher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		// Set criteria for call to matchAll()
		setMatchCriteria(parameters, false);

		final Object item = getItem(parameters, true, false);

		if (item != null)
		{
			final EwJsonArray array = getDefaultArray(parameters);

			return (optAsBoolean("random", false, parameters))
				? array.get(Math.abs(new Random().nextInt()%array.length()))
				: super.matchAll(array, item).reduce();
		}
		else
		{
			final EwJsonArray converted = getArray(parameters, true, true);		// Dereference converted, reduced "obj" value
			final boolean reduce = optAsBoolean("reduce", true, parameters);	// Reduce by default
			final Double scale = optAsDouble("_scaleDouble", null, parameters);	// Optionally handle doubles

			// Construct an array of values corresponding to keys specified in RSL
			return getInArray(converted, scale, reduce);
		}
	}
	@Override
	public String getDescription()
	{
		return "Retreives an item from an array that matches some criteria." +
				"Deprecated. Use getIndex.";
	}
	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"getFromList"};
	}
	@Override
	public String getReturn()
	{
		return "Object";
	}
	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("array","JSONArray","<any>","Object","?_random","Boolean","_reduce","Boolean","_scaleDouble","Boolean");
	}

}
