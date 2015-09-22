package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherFromDotAndArrayNotationObject extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = getObjAsJsonObject(c, parameters, dataStreams);
		JSONObject result = new JSONObject();
		for (String key : EwJson.getKeys(jo))
		{
			parseInto(result,key,jo.get(key));
		}
		return result;
	}

	private void parseInto(JSONObject result, String key, Object object) throws JSONException
	{
		List<String> parts = new EwList<String>(key.split("(?<=[\\[\\]\\.])|(?=[\\[\\]\\.])"));
		Object o = null;
		JSONObject jo = result;
		JSONArray ja = null;
		int index = 0;
		String last = null;
		while (parts.size() > 0)
		{
			String s = parts.get(0);
			if (s.contains("["))
			{
				if (o instanceof JSONObject)
					jo = (JSONObject) o;
				else if (o instanceof JSONArray)
					ja = (JSONArray) o;
				else {
					if (jo != null)
						jo.put(last,ja = new JSONArray());
					else if (ja != null)
						ja.put(index,ja = new JSONArray());
				}
				jo = null;o = null;
			}
			else if (s.contains("]"))
			{
				
			}
			else if (s.contains("."))
			{
				if (o instanceof JSONObject)
					jo = (JSONObject) o;
				else if (o instanceof JSONArray)
					ja = (JSONArray) o;
				else {
					if (jo != null)
						jo.put(last,jo = new JSONObject());
					else if (ja != null)
						ja.put(index,jo = new JSONObject());
				}
				ja = null;o = null;
			}
			else
			{
				if (jo != null){
					last = s;
					o = jo.opt(s);
				}
				if (ja != null)
				{	o = ja.opt(Integer.parseInt(s));
					index = Integer.parseInt(s);
				}
			}
			parts.remove(0);
		}
		if (jo != null)
			jo.put(last,object);
		if (ja != null)
			ja.put(index,object);
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
