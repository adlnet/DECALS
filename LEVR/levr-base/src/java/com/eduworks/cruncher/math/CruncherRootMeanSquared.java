package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.numerics.EwNumerics;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRootMeanSquared extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray array = getObjAsJsonArray(c, parameters, dataStreams);
		return EwNumerics.rootMeanSquared(array);
	}

	@Override
	public String getDescription()
	{
		return "Applies root mean squared to the numbers in the array.";
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
