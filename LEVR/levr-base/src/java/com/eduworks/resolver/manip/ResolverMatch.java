package com.eduworks.resolver.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverMatcher;

public class ResolverMatch extends ResolverMatcher
{
	private boolean matchContaining = false;

	@Override
	public Object resolve(Context c, Map<String,String[]> parameters, Map<String,InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		setMatchCriteria(parameters, true);

		matchContaining = optAsBoolean("contains", false, parameters);

		final Object rawIn = get("in", parameters);
		final Object rawVal = get("value", parameters);
		final EwJsonCollection jsonIn = EwJson.tryConvert(rawIn);
		final EwJsonCollection jsonVal = EwJson.tryConvert(rawVal);

		if (jsonIn == null && jsonVal == null)
			return (valuesMatch(rawVal, rawIn, true)) ? rawVal : null;

		else if (jsonIn == null)
			return null; // No match if "in" is not json but "val" is

		final boolean unique = optAsBoolean("unique", false, parameters);
		final EwJsonArray result = new EwJsonArray();

		for (Object inKey : jsonIn.keySet())
			if (jsonVal == null)
				if (unique && matches(result, rawVal))
					continue;
				else
					result.put(rawVal);
			else for (Object valKey : jsonVal.keySet())
			{
				final Object val = jsonVal.get(valKey);
				final Object in = jsonIn.get(inKey);

				if (valuesMatch(val, in, false))
					if (!unique || !matches(result, val))
						result.put(val);
			}

		return result.reduce();
	}

	/** Overridden to match strings if either contains the other. */
	@Override
	protected boolean valuesMatch(Object item, Object element, boolean tryEncoding) throws JSONException
	{
		if (item == null || element == null)
			return false;

		if (super.valuesMatch(item, element, tryEncoding))
			return true;

		if (matchContaining && item instanceof String && element instanceof String)
		{
			final String itm = ((String)item).toLowerCase();
			final String ele = ((String)element).toLowerCase();

			return (itm.contains(ele) || ele.contains(itm));
		}

		return false;
	}

	@Override
	public String getDescription()
	{
		return "Deprecated. Filters an array or JSONObject collection by the parameters of this Resolver.";
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
		return jo("obj","JSONArray","?contains","Boolean","in","JSONArray","value","JSONObject","?unique","Boolean");
	}

}
