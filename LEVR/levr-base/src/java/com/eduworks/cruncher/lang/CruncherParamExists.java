package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherParamExists extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String paramName = getAsString("paramName", c, parameters, dataStreams);
		
		if(parameters.containsKey(paramName)){
			return get("true", c, parameters, dataStreams);
		}else{
			return get("false", c, parameters, dataStreams);
		}
	}

	@Override
	public String getDescription()
	{
		return "Provides branching functionality based on the existence of a parameter, specified by paramName.";
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
		return jo("paramName","String","true","Resolvable","false","Resolvable");
	}
}
