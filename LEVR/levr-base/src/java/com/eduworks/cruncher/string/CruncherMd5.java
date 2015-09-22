package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.eclipse.jetty.util.security.Credential.MD5;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherMd5 extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		return MD5.digest(getObj(c, parameters, dataStreams).toString());
	}

	@Override
	public String getDescription()
	{
		return "Returns MD5 Hash of String.";
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
