package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRandomString extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		double length = Double.parseDouble(getAsString("length",c,parameters, dataStreams));
		StringBuilder result = new StringBuilder();
		for (int i = 0;i < length;i++)
			result.append((char)((Math.random()*26)+'A' + (Math.random() < .5 ? 32 : 0)));
		return result.toString();
	}

	@Override
	public String getDescription()
	{
		return "Creates a random alphanumeric string.";
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
		return jo("length","Number");
	}

}
