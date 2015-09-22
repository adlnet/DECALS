package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherReverse extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray array = getObjAsJsonArray(c, parameters, dataStreams);
		JSONArray results = new JSONArray();
		for (int i = 0; i < array.length(); i++)
		{
			results.put(array.get(array.length() - 1 - i));
		}
		return results;
	}

	@Override
	public String getDescription() {
		return "Reverses an array";
	}

	@Override
	public String getReturn() {
		return "JSONArray";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("obj","JSONArray");
	}
}
