package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGetIndex extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		if (optAsString("soft", "false", c, parameters, dataStreams).equals("true") && !(getObj(c, parameters, dataStreams) instanceof JSONArray))
			return null;
		Object objx = getObj(c, parameters, dataStreams);
		if (objx instanceof List)
		{
			List obj = (List) objx;
			if (obj == null)
				return null;
			int key = -1;
			if (get("index", c, parameters, dataStreams) instanceof Double)
				key = ((Double) get("index", c, parameters, dataStreams)).intValue();
			else
				try
				{
					key = Integer.parseInt(getAsString("index", c, parameters, dataStreams));
				}
				catch (NumberFormatException ex)
				{
					return null;
				}
			if (obj.size() > key)
				return obj.get(key);
		}
		// if the object was a list and did not return, a cast exception would
		// be thrown...TB 10/30/2014
		else
		{
			JSONArray obj = (JSONArray) objx;
			if (obj == null)
				return null;
			int key = -1;
			if (get("index", c, parameters, dataStreams) instanceof Double)
				key = ((Double) get("index", c, parameters, dataStreams)).intValue();
			else
				try
				{
					key = Integer.parseInt(getAsString("index", c, parameters, dataStreams));
				}
				catch (NumberFormatException ex)
				{
					return null;
				}
			if (obj.length() > key)
				return obj.get(key);
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Indexes into an array and retreives the item at the index designated by 'index'.\nSoft will make it not error out if obj is not a JSONArray";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "JSONArray", "index", "Number", "?soft", "Boolean");
	}
}
