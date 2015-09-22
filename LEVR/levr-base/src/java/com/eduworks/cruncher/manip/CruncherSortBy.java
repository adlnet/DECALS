package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.Tuple;

public class CruncherSortBy extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray objAsJsonArray = getObjAsJsonArray(c, parameters, dataStreams);
		EwList<String> ja = new EwList<String>(objAsJsonArray);
		JSONArray sortBy = getAsJsonArray("by", c, parameters, dataStreams);
		final List<Tuple<Object,Double>> map = new EwList<Tuple<Object,Double>>();
		
		JSONArray results = new JSONArray();
		if (optAsBoolean("byNumbers", false, c, parameters, dataStreams))
		{
			for (int i = 0;i < ja.size();i++)
				map.add(new Tuple<Object,Double>(ja.get(i), Double.parseDouble(sortBy.getString(i))));
			Collections.sort(map,new Comparator<Tuple<Object,Double>>(){

				@Override
				public int compare(Tuple<Object, Double> o1, Tuple<Object, Double> o2)
				{
					return Double.compare(o2.getSecond(),o1.getSecond());
				}});
			Collections.reverse(map);
			results = new JSONArray();
			for (Tuple<Object,Double> sd : map)
				results.put(sd.getFirst());
		}
		else
			for (int i = 0;i < sortBy.length();i++)
				if (ja.contains(sortBy.getString(i)))
					results.put(sortBy.getString(i));
		return results;
	}

	@Override
	public String getDescription()
	{
		return "Sorts the incoming array by the array provided by 'by'. All elements must be in 'by'";
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
		return jo("obj","JSONArray","by","JSONArray");
	}

}
