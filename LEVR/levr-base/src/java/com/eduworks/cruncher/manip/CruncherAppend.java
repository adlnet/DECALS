package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherAppend extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c,parameters, dataStreams);
		JSONArray ja = null;
		if (obj instanceof JSONArray)
			ja = (JSONArray) obj;
		else
			if (obj == null || obj.toString().isEmpty())
				ja = new JSONArray();
			else
			{
				ja = new JSONArray();
				ja.put(obj);
			}
		for (String key : keySet())
		{
			if (key.equals("obj"))
				continue;
			ja.put(get(key, c, parameters, dataStreams));
		}
		return ja;
	}

	@Override
	public String getDescription()
	{
		return "Appends to a JSON Array";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONArray","<any>","Object");
	}

}
