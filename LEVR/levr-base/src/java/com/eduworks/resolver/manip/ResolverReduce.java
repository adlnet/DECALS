package com.eduworks.resolver.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverGetter;

public class ResolverReduce extends ResolverGetter
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		EwJsonArray a = getArray(parameters, true, false);
		if (a != null && a.length() != 0 && a.length() != 1) return a.reduce();

		EwJsonObject o = getObject(parameters, true, false);
		if (o == null)
			return null;
		else if (o.length() == 0)
			return null;
		else if (o.length() == 1)
			return o.get((String)o.keys().next());
		else
			return o;
	}

	@Override
	public String getDescription()
	{
		return "Will reduce a collection down to its sole item if it contains only one item.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject|JSONArray|Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONObject|JSONArray");
	}

}
