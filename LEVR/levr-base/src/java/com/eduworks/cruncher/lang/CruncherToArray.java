package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherToArray extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		if (obj == null)
			return null;
		if (obj instanceof String[])
			return new JSONArray(new EwList<String>((String[]) obj));
		if (obj instanceof JSONArray)
			return obj;
		if (obj instanceof List)
			return new JSONArray((List) obj);
		if (obj instanceof String)
		{
			try
			{
				String asString = getAsString("obj", c, parameters, dataStreams);
				Object result = c.get(asString);
				if (result == null)
					return new JSONArray(asString);
				if (asString.startsWith("["))
					return new JSONArray(asString);
				if (!(result instanceof JSONArray))
				{
					JSONArray ja = new JSONArray();
					ja.put(obj);
					return ja;
				}
				return result;
			}
			catch (Exception ex)
			{
				JSONArray ary = new JSONArray();
				ary.put(getAsString("obj", c, parameters, dataStreams));
				return ary;
			}
		}
		if (obj instanceof JSONObject && optAsBoolean("wrap", false, c, parameters, dataStreams))
		{
			JSONArray ja = new JSONArray();
			ja.put(obj);
			return ja;
		}
		if (obj instanceof Double)
		{
			JSONArray ja = new JSONArray();
			ja.put(obj);
			return ja;
		}
		Object o = getObj(c, parameters, dataStreams);
		JSONArray ja = new JSONArray();
		ja.put(o);
		return ja;
	}

	@Override
	public String getDescription()
	{
		return "Converts many things into an array. If wrap = true, will wrap a JSONObject.";
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
		return jo("obj","String|Object|List|JSONArray|Number|String[]|JSONObject","?wrap","Boolean");
	}

}
