package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherUnion extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		EwList<Object> ja = new EwList<Object>(getObjAsJsonArray(c, parameters, dataStreams));
		Boolean accumulate = optAsBoolean("accumulate", true, c, parameters, dataStreams);
		boolean unique = optAsBoolean("unique",true,c,parameters,dataStreams);
		JSONObject jo = new JSONObject();
		int keys = 0;
		for (String key : keySet())
		{
			if (key.equals("accumulate")) continue;
			if (key.equals("unique")) continue;
			keys++;
		}
		if (keys == 1)
		{
			EwList<Object> results = new EwList<Object>();
			for (Object o : ja)
			{
				if (o instanceof JSONArray)
					if (unique)
					results=results.union(new EwList<Object>(o));
					else
						results.addAll(new EwList<Object>(o));
				else if (o instanceof JSONObject)
					for (String key : EwJson.getKeys((JSONObject)o))
						if (accumulate)
						jo.accumulate(key, ((JSONObject)o).get(key));
						else
							jo.put(key, ((JSONObject)o).get(key));
				else
					if (!results.contains(o))
						results.add(o);
			}
			ja = results;
		}
		else
		{
			for (String key : keySet())
			{
				if (key.equals("obj"))
					continue;
				EwList<Object> organizations = new EwList<Object>(getAsJsonArray(key, c, parameters, dataStreams));
				if (unique)
				ja = ja.union(organizations);
				else
					ja.addAll(organizations);
			}
		}
		if (jo.length() != 0)
			return jo;
		if (ja.size() == 0)
			return null;
		return new JSONArray(ja);
	}

	@Override
	public String getDescription()
	{
		return "Creates the discrete union of all objects (or arrays), or arrays in the obj.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject|JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>","JSONObject|JSONArray","?obj","JSONObject|JSONArray");
	}

}
