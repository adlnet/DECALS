package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.numerics.EwNumerics;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherMax extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		return EwNumerics.max(getObjAsJsonArray(c, parameters, dataStreams));
	}

	@Override
	public String getDescription()
	{
		return "Returns the largest number in obj.";
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
