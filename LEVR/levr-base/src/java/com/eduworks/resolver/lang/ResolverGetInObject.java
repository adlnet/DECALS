package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverMatcher;

public class ResolverGetInObject extends ResolverMatcher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		if (optAsString("_param",parameters) != null)
			put(getAsString("_param",parameters),"");
		
		resolveAllChildren(c, parameters, dataStreams);

		// Set criteria for call to matchAll()
		setMatchCriteria(parameters, false);
				
		final EwJsonObject object = getObject(parameters, true, true);
		final Object item = getItem(parameters, true, false);
		final Double scaleDouble = optAsDouble("_scaleDouble", null, parameters);
		final boolean reduceResult = optAsBoolean("reduce", true, parameters);

		return (item != null)
			? super.matchAll(object, item).reduce()
			: getInObject(object, scaleDouble, reduceResult);
	}
	@Override
	public String getDescription()
	{
		return "Retreives a variable out of an object based on the parameters." +
				"\nUse in the following fashion: #object(foo='bar').#get(foo='') to get foo out of the object." +
				"\nWill return null if variable does not exist.";
	}
	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"get"};
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
		return jo("obj","JSONObject","<any>","Empty","?item","JSONObject");
	}

}
