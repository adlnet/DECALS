package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolvable;

public class CruncherSort extends Cruncher
{

	@Override
	public Object resolve(final Context c, final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray ja = getObjAsJsonArray(c, parameters, dataStreams);
		if (ja == null)
			return null;
		if (ja.length() < 2)
			return ja;

		final Boolean desc = optAsBoolean("desc", false, c, parameters, dataStreams);

		final String paramName = getAsString("paramName", c, parameters, dataStreams);

		final Resolvable op = (Resolvable) get("op");
		final boolean asString = optAsBoolean("asString", false, c, parameters, dataStreams);

		List<Object> list = new EwList<Object>(ja);
		final Map<Object, Object> cache = new HashMap<Object, Object>();
		for (Object o1 : list)
		{
			String o1Key = o1.toString();
			Map<String, String[]> newParameters = new HashMap<String, String[]>(parameters);
			c.put(o1Key, o1);
			newParameters.put(paramName, new String[] { o1Key });
			Object resolve = null;
			try
			{
				resolve = ((Resolvable) op.clone()).resolve(c, newParameters, dataStreams);
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
			c.remove(o1Key);
			if (resolve != null)
			{
				Object s1 = null;
				if (asString)
					s1 = resolve.toString();
				else
					s1 = (Number) Double.parseDouble(resolve.toString());
				cache.put(o1, s1);
			}
		}
		Collections.sort(list, new Comparator<Object>()
		{

			@Override
			public int compare(Object o1, Object o2)
			{
				Object s1 = cache.get(o1);
				Object s2 = cache.get(o2);

				if (s1 == null ^ s2 == null)
				{
					return (s1 == null) ? -1 : 1;
				}

				if (s1 == null && s2 == null)
				{
					return 0;
				}

				return ((Comparable) s1).compareTo(s2);
			}
		});
		if (desc)
			Collections.reverse(list);

		return new JSONArray(list);
	}

	@Override
	public String getDescription()
	{
		return "Sorts a list based on an operation defined by 'op'";
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
		return jo("obj", "JSONArray", "?desc", "Boolean", "paramName", "String", "op", "Resolvable");
	}

}
