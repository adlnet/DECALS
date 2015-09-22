package com.eduworks.resolver.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverNumberThresholdAccumulator extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		Double total = 0.0;
		JSONObject scoreCard = (JSONObject) get("input", parameters);

		for (String key : keySet())
		{
			if (key.equals("input"))
				continue;
			Double score = (Double) scoreCard.get(key);
			if (score == null)
				continue;
			double limit = getAsDouble(key, parameters);
			if (score > limit)
				total++;
			else
				total--;
		}

		return total;
	}

	@Override
	public String getDescription()
	{
		return "Counts the number of doubles in the object 'input' that are above the number provided as parameters.";
	}

	@Override
	public String getReturn()
	{
		return "Number";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("input","JSONObject","<any>","Number");
	}

}
