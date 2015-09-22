package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherToObject extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		if (obj instanceof JSONObject) 
			return obj;
		if (obj == null)
			return null;
		String asString = obj.toString(); 
		if (asString == null) return null;
		if (asString.isEmpty()) return null;
		Object object = c.get(asString);
		JSONObject result=null;
		if (object instanceof JSONObject)
			result = (JSONObject) object;
		
		if (result == null)
			return new JSONObject(asString);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Converts a string into a JSONObject.";
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
		return jo("obj","String");
	}

}
