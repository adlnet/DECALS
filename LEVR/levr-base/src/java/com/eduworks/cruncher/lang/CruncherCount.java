package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherCount extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		int count = 0;
		for (String key : keySet())
		{
			Object opt = get(key, c, parameters, dataStreams);
			if (opt instanceof JSONArray)
				count += ((JSONArray) opt).length();
			else if (opt instanceof JSONObject)
				count += ((JSONObject) opt).length();
			else if (opt instanceof List)
				count += ((List<?>) opt).size();
			else if (opt instanceof String)
				count += ((String) opt).length();
			else if (opt != null)
				count++;
		}
		return count;
	}

	@Override
	public String getDescription()
	{
		return "Counts the number of items in an array, object, or list (or one, if there is something passed in).";
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
		return jo("<any>","JSONArray|JSONObject|List|Object");
	}

}
