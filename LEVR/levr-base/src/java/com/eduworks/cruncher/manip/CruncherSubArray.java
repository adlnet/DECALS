package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherSubArray extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c,parameters, dataStreams);
		JSONArray ja = null;
		if (obj instanceof JSONArray){
			ja = (JSONArray) obj;
		}else{
			return null;
		}
		
		int start = -1;
		int count = -1;
		for (String key : keySet())
		{
			if (key.equals("start"))
				start = Integer.parseInt(getAsString(key, c, parameters, dataStreams));
			if(key.equals("count"))
				count = Integer.parseInt(getAsString(key, c, parameters, dataStreams));
		}
		
		if(start < 0 || count < 0)
			return null;
			
		JSONArray ret = new JSONArray();
		for(int i = start; i < count; i++)
			ret.put(i-start, ja.get(i));
		return ret;
	}

	@Override
	public String getDescription()
	{
		return "Appends to a JSON Array";
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
		return jo("obj","JSONArray","<any>","Object");
	}

}
