package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherCollapse extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);

		String key = getAsString("keyKey", c, parameters, dataStreams);
		String value = getAsString("valueKey", c, parameters, dataStreams);
		Boolean accumulate = optAsBoolean("accumulate", true, c, parameters, dataStreams);

		JSONObject result = new JSONObject();
		if (obj == null)
			return null;
		if (key == null || key.isEmpty())
			return null;
		if (obj instanceof JSONArray)
		{
			JSONArray ja = (JSONArray) obj;
			for (int i = 0; i < ja.length(); i++)
			{
				JSONObject jo = ja.getJSONObject(i);
				if (ja.isNull(i) || ja.get(i) == null)
					continue;
				if (value != null)
				{
					if (!jo.isNull(value) && jo.get(value) != null)
						if (!jo.isNull(key) && jo.get(key) != null)
							if (accumulate)
								result.accumulate(jo.getString(key), jo.get(value));
							else
								result.put(jo.getString(key), jo.get(value));
				}
				else
				{
					if (accumulate)
						result.accumulate(jo.getString(key), jo);
					else
						result.put(jo.getString(key), jo);
					jo.remove(key);
				}
			}
		}
		else if (obj instanceof JSONObject)
		{
			JSONObject jo = (JSONObject) obj;
			for (String objKey : EwJson.getKeys(jo))
			{
				if (jo.isNull(objKey) || jo.get(objKey) == null)
					continue;
				JSONObject jo2 = jo.getJSONObject(objKey);
				if (value != null)
				{
					if (!jo2.isNull(value) && jo2.get(value) != null)
						if (!jo2.isNull(key) && jo2.get(key) != null)
							if (accumulate)
								result.accumulate(jo2.getString(key), jo2.get(value));
							else
								result.put(jo2.getString(key), jo2.get(value));
				}
				else
				{
					if (accumulate)
						result.accumulate(jo2.getString(key), jo2);
					else
						result.put(jo2.getString(key), jo2);
					jo2.remove(key);
				}
			}
		}
		else
			throw new RuntimeException("Collapse: Don't know what I am dealing with.");
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Pivots an array (or object's values) from [#,anykey]:{keyKey:<thing a>,valueKey:<thing b>} to {<thing a>:<thing b>}";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "JSONObject,JSONArray", "keyKey", "String", "valueKey", "String");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

}
