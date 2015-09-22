package com.eduworks.cruncher.cache;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherVariableGet extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		return CruncherVariableSet.store.get(getAsString("key",c,parameters, dataStreams));
	}

	@Override
	public String getDescription()
	{
		return "Retreives a variable from the variable 'cache' (set by #variableGet). Used when simple internal state is absolutely necessary.";
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
		return jo("key","String");
	}

}
