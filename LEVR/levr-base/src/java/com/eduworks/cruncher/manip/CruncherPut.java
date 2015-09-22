package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherPut extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = getObjAsJsonObject(c, parameters, dataStreams);
		if (jo == null)
			jo = new JSONObject();
		else
			jo=new JSONObject(jo.toString());
		for (String key : keySet())
		{
			if (isSetting(key))continue;
			if (key.equals("obj"))continue;
			jo.put(key,get(key,c,parameters, dataStreams));
		}
		String key = optAsString("_key", null, c, parameters, dataStreams);
		if (key != null)
			jo.put(key,get("_value",c,parameters, dataStreams));
		return jo;
	}

	@Override
	public String getDescription()
	{
		return "Puts a value into an object.";
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
		return jo("obj","JSONObject","<any>","Any");
	}

}
