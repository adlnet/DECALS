package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGetByParam extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
			throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(c,parameters, dataStreams);
		if (obj == null) return null;
		String key = getAsString("param",c,parameters, dataStreams);
		if (!obj.has(key))
			return null;
		return obj.get(key);
	}

	@Override
	public String getDescription()
	{
		return "Returns a value from the JSONObject (obj) described by the string in param.";
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
		return jo("obj","JSONObject","param","String");
	}

}
