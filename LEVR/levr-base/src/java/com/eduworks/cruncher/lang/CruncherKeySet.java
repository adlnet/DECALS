package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherKeySet extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = getObjAsJsonObject(c, parameters, dataStreams);
		if (jo == null) return null;
		JSONArray ja = new JSONArray();
		for (String s : EwJson.getKeys(jo))
			ja.put(s);
		return ja;
	}

	@Override
	public String getDescription()
	{
		return "Returns a JSONArray of the keys in a JSONObject";
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
		return jo("obj","JSONObject");
	}
}
