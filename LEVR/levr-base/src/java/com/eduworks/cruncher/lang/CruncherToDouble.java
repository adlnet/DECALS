package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherToDouble extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		if (obj == null) return null;
		if (obj instanceof Double)
			return obj;
		else if (obj instanceof String)
			return Double.parseDouble((String) obj);
		else if (obj instanceof Integer)
			return ((Integer)obj).doubleValue();
		throw new RuntimeException(new NumberFormatException("Cannot parse object of type " + obj.getClass().getName()));
	}

	@Override
	public String getDescription()
	{
		return "Converts a thing to an double";
	}

	@Override
	public String getReturn()
	{
		return "Double";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Integer|String");
	}

}
