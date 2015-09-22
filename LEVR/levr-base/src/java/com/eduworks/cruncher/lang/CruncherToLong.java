package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;


public class CruncherToLong extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		if (obj == null) return null;
		if (obj instanceof Double)
			return ((Double) obj).longValue();
		else if (obj instanceof String)
			return Long.parseLong((String) obj);
		else if (obj instanceof Integer)
			return new Long((Integer) obj);
		else if (obj instanceof Long)
			return obj;
		throw new RuntimeException(new NumberFormatException("Cannot parse object of type " + obj.getClass().getName()));
	}

	@Override
	public String getDescription()
	{
		return "Converts a thing to an long";
	}

	@Override
	public String getReturn()
	{
		return "Long";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Double|String");
	}

}
