package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwUri;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherUrlDecode extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		return EwUri.decodeValue(getAsString("obj",c, parameters, dataStreams).toString());
	}

	@Override
	public String getDescription()
	{
		return "Performs URI decoding on a string.";
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
		return jo("obj","String");
	}
	

}
