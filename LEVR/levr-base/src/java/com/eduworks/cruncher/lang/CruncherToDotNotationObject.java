package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.python.antlr.PythonParser.else_clause_return;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherToDotNotationObject extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(c, parameters, dataStreams);
		String[] stopAt = optAsString("stopAt", null, c, parameters, dataStreams).split(",");
		String[] remove = optAsString("remove", null, c, parameters, dataStreams).split(",");
		JSONObject result = new JSONObject();
		recurse("", result, obj, stopAt, remove);
		return result;
	}

	private void recurse(String dot, JSONObject result, JSONObject obj, String[] stopAt, String[] remove) throws JSONException
	{
		Iterator it = obj.keys();
		while (it.hasNext())
		{
			String key = it.next().toString();
			String myDot = dot;
			if (!myDot.endsWith("."+key))
			{
				if (dot.isEmpty() == false)
					myDot += ".";
				myDot += key;
			}
			Object o = obj.get(key);

			put(result, myDot, o, stopAt, remove);
		}
	}

	private void put(JSONObject result, String myDot, Object o, String[] stopAt, String[] remove) throws JSONException
	{
		if (o instanceof JSONObject)
			recurse(myDot, result, (JSONObject) o, stopAt, remove);
		else if (o instanceof JSONArray)
		{
			JSONArray arr = (JSONArray) o;
			for (int i = 0; i < arr.length(); i++)
			{
				Object subo = arr.get(i);
				put(result, myDot, subo, stopAt, remove);
			}
		}
		else
		{
			if (stopAt != null)
			{
				String shortest = null;
				for (String stop : stopAt)
				{
					int indexOf = myDot.indexOf(stop);
					if (indexOf != -1)
					{
						String substring = myDot.substring(0, indexOf);
						if (shortest == null || substring.length() < shortest.length())
							shortest = substring;
					}
					else if (shortest == null || myDot.length() < shortest.length())
						shortest = myDot;
				}
				if (remove != null)
					for (String s : remove)
						shortest = shortest.replace(s, "");
				result.accumulate(shortest, o);
			}
			else
			{
				String myDot2 = myDot;
				if (remove != null)
					for (String s : remove)
						myDot2 = myDot2.replace(s, "");
				result.accumulate(myDot2, o);
			}
		}
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public String getReturn()
	{
		return null;
	}

	@Override
	public String getAttribution()
	{
		return null;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return null;
	}

}
