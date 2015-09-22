package com.eduworks.resolver.math;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverDivide extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		boolean ignoreZeros = optAsBoolean("_ignoreZeros",false,parameters);
		Double result = getAsDouble("numerator", parameters);
		Iterator<String> i = this.keys();
		while (i.hasNext())
		{
			String key = i.next();
			if (isSetting(key))
				continue;
			if (key.equals("numerator"))
				continue;
			Double value = getAsDouble(key, parameters);
			if (value == 0 && ignoreZeros)
				continue;
			result /= value;
			if ((Double.isNaN(result)||Double.isInfinite(result)) && optAsDouble("NaNresult",null, parameters) != null)
				result = getAsDouble("NaNresult", parameters);
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Divides the number in 'numerator' by all other numbers provided." +
				"\nIf '_NaNresult' is provided, will use that result if NaN is achieved.";
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
		return jo("numerator","Number","<any>","Number","?_NaNresult","Number");
	}
}
