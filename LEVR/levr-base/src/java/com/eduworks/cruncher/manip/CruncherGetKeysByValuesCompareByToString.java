package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGetKeysByValuesCompareByToString extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject object = getObjAsJsonObject(c, parameters, dataStreams);
		String matches = getAsString("matches", c, parameters, dataStreams);
		JSONArray out = new JSONArray();

		Iterator<String> keys = object.keys();
		while (keys.hasNext())
		{
			String key = keys.next();
			if (isSetting(key))
				continue;
			Object value = object.get(key);
			if (matches.equals(value.toString()))
				out.put(key);
		}
		return out;
	}

	@Override
	public String getDescription()
	{
		return "Obviously a product of insanity.\n" +
				"Takes an object, retreives values into a JSONArray if they match the 'matches' string.\n" +
				"Comparison is done by toString();";
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
		return jo("obj","JSONObject","matches","String");
	}

}
