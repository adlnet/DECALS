package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.Tuple;

public class CruncherPivotOrdered extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(c, parameters, dataStreams);
		boolean removeZero = Boolean.parseBoolean(getAsString("removeZero", c, parameters, dataStreams));
		int limit = Integer.parseInt(optAsString("limit","500000",c,parameters, dataStreams));
		JSONObject result = new JSONObject();
		
		Map<String,EwList<Tuple<String,Double>>> wses = new HashMap<String,EwList<Tuple<String,Double>>>();
		
		Iterator<String> keys = obj.keys();
		while (keys.hasNext())
		{
			String paragraph = keys.next();
			JSONObject scoreObj = null;
			if (obj.get(paragraph) instanceof JSONObject)
				scoreObj = obj.getJSONObject(paragraph);
			for (String topic: EwJson.getKeys(scoreObj))
			{
				double score = scoreObj.getDouble(topic);
				if (removeZero && score == 0.0)
					continue;
				Tuple<String,Double> ws = new Tuple<String,Double>(paragraph,score);
				if (!wses.containsKey(topic))
					wses.put(topic, new EwList<Tuple<String,Double>>());
				wses.get(topic).add(ws);
			}
		}
		for (String topic : wses.keySet())
		{
			EwList<Tuple<String,Double>> scores = wses.get(topic);
			Collections.sort(scores);
			if (optAsBoolean("includeScores",false,c,parameters,dataStreams))
			{
				JSONObject jas = new JSONObject();
				for (Tuple<String,Double> ws : scores.first(limit))
					jas.put(ws.getFirst(),ws.getSecond());
				result.put(topic, jas);
			}
			else
			{
			JSONArray jas = new JSONArray();
			for (Tuple<String,Double> ws : scores.first(limit))
				jas.put(ws.getFirst());
			result.put(topic, jas);
			}
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Pivots the JSONObject such that the keys become the list values and visa versa, but assumes each list value also has a score associated with it, and orders the results by that score.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONObject","removeZeros","Boolean","limit","Number");
	}

}
