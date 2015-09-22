package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherTry extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
			throws JSONException
	{
		Object obje = getObj(c,parameters, dataStreams);
		if (obje == null) return null;
		if (!(obje instanceof JSONObject))
			return obje;
		JSONObject obj = (JSONObject) obje;
		for (String key : keySet())
		{
			if (key.equals("soft"))
				continue;
			if (isSetting(key))
				continue;
			if (isObj(key))
				continue;
			Object result = obj.opt(key);
			if (result != null)
				return result;
		}
		return obje;
	}

	@Override
	public String getDescription()
	{
		return "Will attempt to get a parameter of a JSONObject. If it does not work, it will return the whole object. Useful for optionally nested parameters.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject|Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONObject","<any>","String","?soft","Boolean");
	}
}
