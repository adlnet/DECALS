package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherJoin extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray jo = getObjAsJsonArray(c, parameters, dataStreams);
		if (jo == null) return "";
		if (jo.length()==0) return "";
		String divider = optAsString("divider", ",", c, parameters, dataStreams);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < jo.length(); i++)
			if (i==0)
				sb.append(jo.getString(i));
			else
				sb.append(divider + jo.getString(i));
		return sb.toString();
	}

	@Override
	public String getDescription()
	{
		return "join a string by some other string.";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String","divider","String");
	}

}
