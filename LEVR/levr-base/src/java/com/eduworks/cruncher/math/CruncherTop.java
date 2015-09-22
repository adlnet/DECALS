package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.Tuple;

public class CruncherTop extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		int count = Integer.parseInt(getAsString("top", c, parameters, dataStreams));
		Object obj = getObj(c, parameters, dataStreams);
		if (obj == null)
			return null;
		if (obj instanceof JSONObject)
		{
			JSONObject source = (JSONObject) obj;
			EwList<Tuple<String, Double>> wses = new EwList<Tuple<String, Double>>();
			for (String key : EwJson.getKeys(source))
				wses.add(new Tuple<String, Double>(key, source.getDouble(key)));
			EwList.sort(wses, new Comparator<Tuple<String, Double>>()
			{
				@Override
				public int compare(Tuple<String, Double> o1, Tuple<String, Double> o2)
				{
					if (o2.getSecond() < o1.getSecond())
						return -1;
					else if (o2.getSecond() > o1.getSecond())
						return 1;
					else
						return 0;
				}
			});
			wses = wses.first(count);
			EwList<JSONObject> results = new EwList<JSONObject>();
			
			for (Tuple<String, Double> ws : wses)
			{
				JSONObject jo = new JSONObject();
				jo.put("word", ws.getFirst());
				jo.put("score",ws.getSecond());
				results.add(jo);
			}
			return results;
		}
		else if (obj instanceof JSONArray)
		{
			JSONArray array = (JSONArray) obj;
			JSONArray results = new JSONArray();
			for (int i = 0; i < count && i < array.length(); i++)
			{
				results.put(array.get(i));
			}
			return results;
		}
		else
			throw new RuntimeException(obj + " not understood by CruncherTop");
	}

	@Override
	public String getDescription()
	{
		return "Sorts a JSONObject or JSONArray into an ordered list of the provided numbers. JSONObject will become represented as an ordered array of JSONObjects.";
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
		return jo("obj", "JSONObject|JSONArray");
	}

}
