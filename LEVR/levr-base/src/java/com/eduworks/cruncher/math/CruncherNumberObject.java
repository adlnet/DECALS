package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherNumberObject extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		int min = Integer.parseInt(getAsString("min",c,parameters, dataStreams));
		int max = Integer.parseInt(getAsString("max",c,parameters, dataStreams));
		JSONObject results = new JSONObject();
		for (int i = min;i < max;i++)
		{
			results.put(Integer.toString(i),"");
		}
		return results;
	}

	@Override
	public String getDescription()
	{
		return "Generates a JSON Object with Integers from 'min' to 'max' as the keys and empty values.";
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
		return jo("min","Number","max","Number");
	}

}
