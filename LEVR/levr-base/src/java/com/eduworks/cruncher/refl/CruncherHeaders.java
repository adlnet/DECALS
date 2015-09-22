package com.eduworks.cruncher.refl;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherHeaders extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject result = new JSONObject();
		Enumeration<String> headerNames = c.request.getHeaderNames();
		while (headerNames.hasMoreElements())
		{
			String s = headerNames.nextElement();
			result.put(s,c.request.getHeader(s));
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribution()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
