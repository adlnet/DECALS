package com.eduworks.cruncher.math;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherSum extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray array = getObjAsJsonArray(c, parameters, dataStreams);
		Double result = 0.0;
		for (int i = 0;i < array.length();i++)
			result += array.getDouble(i);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Sums over an array of numbers";
	}

	@Override
	public String getReturn()
	{
		return "Number";
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
