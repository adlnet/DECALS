package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRemoveByParam extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = getAsJsonObject("obj", c, parameters, dataStreams);
		JSONObject result = new JSONObject();
		String param = getAsString("param", c, parameters, dataStreams);
		Iterator<String> keys = jo.keys();
		
		while(keys.hasNext())
		{
			String key = keys.next();
			if (!key.equals(param))
				result.put(key, jo.get(key));
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Removes all instances of 'item' from the array provided by obj.";
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
		return jo("obj","JSONArray","item","String");
	}

}
