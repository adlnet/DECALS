package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherLeaves extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object o = getObj(c, parameters, dataStreams);
		JSONArray results = new JSONArray();
		recurse(results,o);
		return results;
	}

	private void recurse(JSONArray results, Object o) throws JSONException
	{
		if (o instanceof JSONObject)
		{
			JSONObject obj = (JSONObject) o;
			for (Object value : EwJson.getValues(obj))
				recurse(results,value);
		}
		else if (o instanceof JSONArray)
		{
			JSONArray obj = (JSONArray) o;
			for (int i =0 ;i < obj.length();i++)
				recurse(results,obj.get(i));
		}
		else
			results.put(o);
		
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public String getReturn()
	{
		return null;
	}

	@Override
	public String getAttribution()
	{
		return null;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return null;
	}

}
