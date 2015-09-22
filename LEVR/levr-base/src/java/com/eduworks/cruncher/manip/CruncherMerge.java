package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherMerge extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray ja = getObjAsJsonArray(c, parameters, dataStreams);
		Boolean accumulate = optAsBoolean("accumulate", true, c, parameters, dataStreams);
		JSONObject jo = new JSONObject();
		JSONArray results = new JSONArray();
		if (keySet().size() == 1)
		{
			for (int i = 0;i < ja.length();i++)
			{
				Object o = ja.get(i);
				if (o instanceof JSONArray)
				{
					JSONArray ja2 = (JSONArray) o;
					for (int j = 0;j < ja2.length();j++)
						results.put(ja2.get(j));
				}
				else if (o instanceof JSONObject)
					for (String key : EwJson.getKeys((JSONObject)o))
						if (accumulate)
							jo.accumulate(key, ((JSONObject)o).get(key));
						else
							jo.put(key, ((JSONObject)o).get(key));
				else
					results.put(o);
			}
		}
		else
		{
			for (String key : keySet())
			{
				if (key.equals("obj"))
					continue;
				Object o = get(key, c, parameters, dataStreams);
				if (o instanceof JSONArray)
				{
					JSONArray ja2 = (JSONArray) o;
					for (int j = 0;j < ja2.length();j++)
						results.put(ja2.get(j));
				}
				else if (o instanceof JSONObject)
					for (String kk : EwJson.getKeys((JSONObject)o))
						if (accumulate)
							jo.accumulate(kk, ((JSONObject)o).get(kk));
						else
							jo.put(kk, ((JSONObject)o).get(kk));
				else
					results.put(o);
			}
		}
		if (jo.length() != 0)
			return jo;
		if (results.length() == 0)
			return null;
		return results;
	}

	@Override
	public String getDescription()
	{
		return "Like Union, but without sameness checks.";
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
