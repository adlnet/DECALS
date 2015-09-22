package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherAdd extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		double result = 0.0;
		StringBuilder resultStr = new StringBuilder();
		Iterator<String> i = this.sortedKeys();
		while (i.hasNext())
		{
			String key = i.next();
			if (isSetting(key))
				continue;
			Object o = get(key,c,parameters, dataStreams);
			if (o == null || o.toString().isEmpty())
				continue;
			if (resultStr.length() == 0 && !optAsBoolean("_string",false,c,parameters, dataStreams))
				try
				{
					Double value = objectToDouble(o);
					result += value;
				}
				catch (NumberFormatException ex)
				{
					if (result != 0.0)
						resultStr.append(result);
					resultStr.append(objectToString(o));
				}
			else
				resultStr.append(objectToString(o));
		}
		if (resultStr.length() > 0)
			return resultStr.toString();
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Adds any number of numbers or strings. Will default to returning a number if all parameters are numbers, will return string if not.";
	}

	@Override
	public String getReturn()
	{
		return "Number|String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>","Number|String");
	}
}
