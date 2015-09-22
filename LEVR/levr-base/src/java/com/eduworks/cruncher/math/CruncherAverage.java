package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.numerics.EwNumerics;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherAverage extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray array = getObjAsJsonArray(c, parameters, dataStreams);
		return EwNumerics.average(array);
	}

	@Override
	public String getDescription()
	{
		return "Computes the average across an input set of numbers.";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSON Array of Numbers");
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

}
