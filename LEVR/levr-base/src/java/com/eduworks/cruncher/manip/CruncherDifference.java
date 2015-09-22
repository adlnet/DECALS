package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherDifference extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray ja = getAsJsonArray("obj", c, parameters, dataStreams);
		JSONArray ja2 = getAsJsonArray("diff", c, parameters, dataStreams);
		JSONArray results = new JSONArray();
		for (int i = 0; i < ja.length(); i++) {
			if (!member(ja.get(i), ja2))
				results.put(ja.get(i));
		}
		return results;
	}

	public boolean member(Object o, JSONArray items) throws JSONException {
		boolean isMember = false;
		for (int i = 0; i < items.length() && !isMember; i++)
			if (items.get(i).equals(o))
				isMember = true;
		return isMember;
	}
	
	@Override
	public String getDescription()
	{
		return "Returns a set that is the difference between set a to b without b to a";
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
		return jo("diff","JSONArray");
	}

}
