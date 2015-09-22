package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwMap;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherToParameter extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String paramName=getAsString("paramName",c,parameters, dataStreams);
		Object o = getObj(c,parameters, dataStreams);

		final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
		newParams.put(paramName, new String[] { o.toString() });
		Object result = resolveAChild("op",c, newParams, dataStreams);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Converts obj into a @parameter and runs op.";
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
		return jo("obj","Object","paramName","String","op","Resolvable");
	}
}
