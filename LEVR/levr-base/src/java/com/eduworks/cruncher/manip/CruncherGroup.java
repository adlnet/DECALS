package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGroup extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray ja = getObjAsJsonArray(c, parameters, dataStreams);
		if (ja == null) ja = new JSONArray();
		JSONObject result = new JSONObject();
		for (int i = 0;i < ja.length();i++)
		{
			String s = ja.getString(i);
			result.append(s, s);
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Takes an array of values and groups them based on their string equivalent.";
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
		return jo("obj","JSONArray");
	}

}
