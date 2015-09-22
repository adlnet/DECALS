package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherParamsToObject extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (String key : parameters.keySet())
		{
			//Attach only parameters that exist in this parameter list.
			if (has(key))
				for (String value : parameters.get(key))
					jo.accumulate(key, value);
		}
		return jo;
	}

	@Override
	public String getDescription()
	{
		return "Takes all parameters and puts them into a JSON object except the ones defined in the keySet of this object.()";
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
		return jo("<any>","String");
	}

}
