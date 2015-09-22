package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGet extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
			throws JSONException
	{
		Object obje = getObj(c,parameters, dataStreams);
		if (obje == null) return null;
		if (optAsString("soft","false",c,parameters, dataStreams).equals("true") && !(obje instanceof JSONObject))
			return null;
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
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Returns the first non-null value referenced by parameters from obj (values should be empty)";
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
		return jo("obj","Object","<any>","String","?soft","Boolean");
	}
}
