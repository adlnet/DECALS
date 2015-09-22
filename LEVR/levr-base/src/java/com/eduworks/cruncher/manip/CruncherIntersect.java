package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherIntersect extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		EwList<Object> ja = new EwList<Object>(getObjAsJsonArray(c, parameters, dataStreams));
		EwList<Object> results = new EwList<Object>();
		for (String key : keySet())
		{
			if (key.equals("obj"))
				continue;
			if (key.equals("contains"))
				continue;
			if (optAsBoolean("contains", false, c, parameters, dataStreams))
			{
				String text = getAsString(key, c, parameters, dataStreams).toLowerCase();
				EwList<Object> janew = new EwList<Object>();
				for (Object s : ja)
					if (s.toString().contains(text))
						results.add(s);
			}
			else
			{
				EwList<Object> organizations = new EwList<Object>(getAsJsonArray(key, c, parameters, dataStreams));
				ja = ja.intersect(organizations);
				results = ja;
			}
		}
		if (results.size() == 0)
			return null;
		return new JSONArray(results);
	}

	@Override
	public String getDescription()
	{
		return "Returns a list that represents the discrete intersection of all lists represented by the parameters of this Resolver.\n Has alternate behavior, if 'contains' is used, will return any items that have a substring of the 'contains' parameter.";
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
		return jo("<any>","JSONArray","?contains","String");
	}

}
