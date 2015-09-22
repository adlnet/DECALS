package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.EwMap;
import com.eduworks.lang.EwSet;
import com.eduworks.lang.EwStringUtil;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyFutureList;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolvable;
import com.eduworks.resolver.Resolver;

public class ResolverForEach extends Resolver
{
	protected static final String	AGG_KEY			= "aggregation";
	protected static final String	ARRAY_KEY		= "asArray";
	protected static final String	ITEM_KEY		= "item";
	protected static final String	OP_KEY			= "op";
	protected static final String	PARAM_KEY		= "paramName";
	protected static final String	PREV_PARAM_KEY	= "prevParamName";
	protected static final String	SIM_KEY			= "simultaneous";
	protected static final String	MEM_KEY			= "memorySaver";

	EwJsonArray						sorted			= new EwJsonArray();

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, final Map<String, InputStream> dataStreams)
			throws JSONException
	{
		Object item = null;
		if (opt(ITEM_KEY, parameters) != null)
		{
			resolveAChild(c,parameters, dataStreams, ITEM_KEY);

			item = (optAsBoolean(ARRAY_KEY, false, parameters)) ? EwJson.wrapAsArray(get(ITEM_KEY, parameters)) : get(
					ITEM_KEY, parameters);
		}
		else if (opt("obj", parameters) != null)
		{
			resolveAChild(c,parameters, dataStreams, "obj");

			item = (optAsBoolean(ARRAY_KEY, false, parameters)) ? EwJson.wrapAsArray(get("obj", parameters)) : get(
					"obj", parameters);
		}
		else
			throw new RuntimeException("foreach: Item not found.");

		final EwList<String> strings = objectAsStrings(item);
		final Resolvable operation = (Resolvable) get(OP_KEY);

		String paramName = optAsString(PARAM_KEY, parameters);
		String prevParamName = optAsString(PREV_PARAM_KEY, parameters);
		if (paramName == null || paramName.isEmpty())
			paramName = "eachId";

		final String aggregationMode = optAsString(AGG_KEY, parameters);
		final boolean simultaneous = optAsBoolean(SIM_KEY, false, parameters);

		try
		{
			if (simultaneous)
				resolveSimultaneous(c,parameters, dataStreams, strings, operation, paramName, prevParamName);
			else
				resolveSynchronous(c,parameters, dataStreams, strings, operation, paramName, prevParamName);
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		removeAllSettings();
		remove(ARRAY_KEY);
		remove(ITEM_KEY);
		remove(OP_KEY);
		remove(AGG_KEY);
		remove(SIM_KEY);
		remove(PARAM_KEY);
		remove(PREV_PARAM_KEY);
		remove("obj");

		return aggregate(parameters, aggregationMode);
	}

	protected Object aggregate(Map<String, String[]> parameters, String aggregationMode) throws JSONException
	{
		if (aggregationMode == null)
			return this;
		if (aggregationMode.equals("max"))
		{
			Double max = -999999.0;
			for (String key : keySet())
				max = Math.max(getAsDouble(key, parameters), max);
			return max;
		}
		else if (aggregationMode.equals("append"))
		{
			String delimiter = optAsString("delimiter", parameters);
			if (!this.has("delimiter"))
				delimiter = " ";

			StringBuilder str = new StringBuilder();
			for (int i = 0; i < sorted.length(); i++)
			{
				String key = sorted.getString(i);
				if (has(key) || hasParam(key))
					if (str.length() == 0)
						str.append(getAsString(key, parameters));
					else
						str.append(delimiter).append(getAsString(key, parameters));
			}
			return str;
		}
		else if (aggregationMode.equals("appendArray"))
		{
			EwJsonArray ja = new EwJsonArray();
			for (int i = 0; i < sorted.length(); i++)
			{
				if (keySet().contains(sorted.get(i)))
				{
					Object value = get(sorted.get(i).toString(), parameters);
					if (value instanceof Collection)
					{
						Collection<?> ja2 = (Collection<?>) value;
						Iterator<?> iterator = ja2.iterator();
						while (iterator.hasNext())
							ja.put(iterator.next());
					}
					else if (value instanceof JSONArray)
					{
						JSONArray array = (JSONArray) value;
						for (int j = 0; j < array.length(); j++)
							ja.put(array.get(j));
					}
					else if (value instanceof JSONObject)
					{
						if (((JSONObject) value).length() > 0)
							ja.put(value);
					}
					else if (value instanceof String)
						if (value.toString().length() > 0)
							ja.put(value);
				}
			}
			return ja;
		}
		else if (aggregationMode.equals("keys"))
		{
			List<Object> results = new EwList<Object>();
			for (String key : keySet())
				results.add(key);

			return results;
		}
		else if (aggregationMode.equals("reverse"))
		{
			EwJsonObject ja = new EwJsonObject();
			for (String key : keySet())
			{
				ja.put(get(key), key);
			}
			return ja;
		}
		else if (aggregationMode.equals("values"))
		{
			JSONArray results = new JSONArray();
			for (int i = 0; i < sorted.length(); i++)
			{
				String key = sorted.getString(i);
				if (has(key) || hasParam(key))
					results.put(get(key, parameters));
			}
			return results;
		}
		else if (aggregationMode.equals("valuesSet"))
		{
			Set<Object> results = new EwSet<Object>();
			for (int i = 0; i < sorted.length(); i++)
			{
				String key = sorted.getString(i);
				if (has(key) || hasParam(key))
					results.add(get(key, parameters));
			}
			return results;
		}
		return this;
	}

	protected void resolveSynchronous(Context c,Map<String, String[]> parameters, final Map<String, InputStream> dataStreams,
			EwList<String> strings, Resolvable operation, String paramName, String prevParamName)
			throws CloneNotSupportedException, JSONException
	{
		String prev = null;
		int counter = 0;
		if (optAsBoolean("removeDuplicates", false, parameters))
			strings = strings.removeDuplicates();
		int sequenceI = optAsInteger("sequenceI", -1, parameters);
		int sequenceMod = optAsInteger("sequenceMod", -1, parameters);
		for (final String id : strings)
		{
			if (c.shouldAbort()) return;
			counter++;
			if (sequenceI == -1 || sequenceMod == -1 || counter % sequenceMod == sequenceI)
			{
			final Resolvable thing = (Resolvable) operation.clone();
			final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
			newParams.put(paramName, new String[] { id });
			if (prevParamName != null && !prevParamName.isEmpty())
				newParams.put(prevParamName, new String[] { prev });

			if (counter % 1000 == 0)
				System.out.println("On " + counter + "/" + strings.size());
			put(id, resolveAChild(c,newParams, dataStreams, id, thing));
			sorted.put(id);
			}
			prev = id;
		}
	}

	protected void resolveSimultaneous(final Context c,final Map<String, String[]> parameters,
			final Map<String, InputStream> dataStreams, EwList<String> strings, Resolvable operation, String paramName,
			String prevParamName) throws CloneNotSupportedException, JSONException
	{
		MyFutureList fl = new MyFutureList();
		int counter = 0;
		String prev = null;
		if (optAsBoolean("removeDuplicates", false, parameters))
			strings = strings.removeDuplicates();
		int sequenceI = optAsInteger("sequenceI", -1, parameters);
		int sequenceMod = optAsInteger("sequenceMod", -1, parameters);
		for (final String id : strings)
		{
			if (c.shouldAbort()) return;
			counter++;
			if (sequenceI == -1 || sequenceMod == -1 || counter % sequenceMod == sequenceI)
			{
				final Resolvable thing = (Resolvable) operation.clone();
				final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
				newParams.put(paramName, new String[] { id });
				if (prevParamName != null && !prevParamName.isEmpty())
					newParams.put(prevParamName, new String[] { prev });
				sorted.put(id);
				if (counter % 1000 == 0)
					System.out.println(EwStringUtil.tabs(EwThreading.getThreadLevel()) + ": On " + counter + "/"
							+ strings.size());
				EwThreading.forkAccm(fl, true, new MyRunnable()
				{
					@Override
					public void run()
					{
						try
						{
							put(id, resolveAChild(c,newParams, dataStreams, id, thing));
							if (optAsBoolean(MEM_KEY, false, parameters))
								remove(id);
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
				});
			}
			prev = id;
		}
		fl.nowPause(true);
	}

	@Override
	public String getDescription()
	{
		return "Iterates over a list or array. Has many different modes it can operate in, but performs 'op' over each item in the array." +
				"\nobj - The array or list to iterate over." +
				"\nparamName - The name of the @parameter to place the value into." +
				"\nop - The operation to execute." +
				"\n(Optional) aggregation - The way to aggregate the results. Can be:" +
				"\n\t<default> Return a JSONObject where the keys are the original values (or keys, if obj is a JSONObject) and the values are the results." +
				"\n\tmax - Return the highest numeric value." +
				"\n\tappend - Along with parameter 'delimiter', append the results into a single string." +
				"\n\tappendArray - Append the results into an array. Preserves ordering." +
				"\n\tkeys - Return only the keys (ids). Assumes JSONObject as obj." +
				"\n\tvalues - Return only the values (results) as an array." +
				"\n\tvaluesSet - Return only the values, but do not duplicate any values." +
				"\n(Optional) asArray - Wrap obj in an array if it is not an array." +
				"\n(Optional) prevParamName - Keep track of the last item as well, pass in as this @param." +
				"\n(Optional) simultaneous - Thread the operation. Defaults to false." +
				"\n(Optional) memorySaver - Do not keep track of results. Defaults to false.";
				
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
		return jo("obj","JSONArray|JSONObject","paramName","String","op","Resolvable","?aggregation","String","?asArray","Boolean","?prevParamName","Boolean","?simultaneous","Boolean","?memorySaver","Boolean");
	}

	@Override
	public String[] getResolverNames()
	{
		return new String[] { getResolverName(), "foreach" };
	}

}
