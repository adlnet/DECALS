package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherToString extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c,parameters, dataStreams);
//		if (obj instanceof JSONArray)
//			return ((JSONArray)obj).toString(5);
//		else if (obj instanceof JSONObject)
//			return ((JSONObject)obj).toString(5);
		return obj.toString();
	}

	@Override
	public String getDescription()
	{
		return "ToStrings the obj.";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Object");
	}

}
