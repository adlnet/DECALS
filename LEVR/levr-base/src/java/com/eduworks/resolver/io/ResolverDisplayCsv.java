package com.eduworks.resolver.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.InMemoryFile;

public class ResolverDisplayCsv extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		StringBuilder accmData = new StringBuilder();

		Object o = get("obj", parameters);
		if (o instanceof JSONArray)
		{
			JSONArray a = (JSONArray) o;
			List<String> keys = EwJson.getKeysUnsorted((JSONObject) a.get(0));
			if (optAsString("fields", parameters) != null)
				keys = new EwList<String>(optAsString("fields", parameters).split(","));
			for (int i = 0; i < a.length(); i++)
			{
				JSONObject jo = (JSONObject) a.get(i);
				for (String s : keys)
				{
					if (!s.equals(keys.get(0)))
						accmData.append(",");
					if (jo.has(s))
						accmData.append(encodeCsv(jo.opt(s).toString()));
				}
				accmData.append("\n");
			}
		}
		if (o instanceof JSONObject)
		{
			JSONObject a = (JSONObject) o;
			if (optAsBoolean("valueList", false, parameters))
				listCentricAccumulate(parameters, accmData, a);
			else if (optAsBoolean("array2d", false, parameters))
				list2dCentricAccumulate(parameters, accmData, a);
			else if (optAsBoolean("simple",false,parameters))
				simpleCentricAccumulate(parameters, accmData, a);
			else
				objectCentricAccumulate(parameters, accmData, a);
		}

		InMemoryFile f = new InMemoryFile();
		f.data = accmData.toString().getBytes();
		f.mime = "text/csv";
		f.name = getAsString("name", parameters);
		return f;
	}

	private void listCentricAccumulate(Map<String, String[]> parameters, StringBuilder accmData, JSONObject a)
			throws JSONException
	{
		List<String> indexKeys = EwJson.getKeys(a);
		for (String key : indexKeys)
		{
			JSONArray ja = null;
			ja = a.getJSONArray(key);
			accmData.append(encodeCsv(key));
			for (int i = 0; i < ja.length(); i++)
			{
				if (ja.isNull(i))
					continue;
				accmData.append(",");
				if (ja.get(i) instanceof Number)
					accmData.append(ja.get(i));
				else
					accmData.append(encodeCsv(ja.get(i).toString()));
			}
			accmData.append("\n");
		}
	}

	private void list2dCentricAccumulate(Map<String, String[]> parameters, StringBuilder accmData, JSONObject a)
			throws JSONException
	{
		// output first row (first order keys)

		List<String> indexKeys = EwJson.getKeysUnsorted(a);
		for (String key1 : indexKeys)
		{
			accmData.append(",");
			accmData.append(encodeCsv(key1.toString()));
		}
		List<String> rowKeys = new ArrayList<String>();
		for (String key : indexKeys)
		{
			for (String rowName : EwJson.getKeysUnsorted(a.getJSONObject(key)))
				if (!rowKeys.contains(rowName))
					rowKeys.add(rowName);
		}
		for (String row : rowKeys)
		{
			accmData.append("\n");
			accmData.append(encodeCsv(row));
			for (String col : indexKeys)
			{
				accmData.append(",");
				if (a.getJSONObject(col).has(row))
				{
					Object object = a.getJSONObject(col).get(row);
					if (object instanceof Number)
						accmData.append(object);
					else
						accmData.append(encodeCsv(object.toString()));
				}
				else
				{
					accmData.append(optAsString("default", parameters));
				}
			}
		}

	}

	public void objectCentricAccumulate(Map<String, String[]> parameters, StringBuilder accmData, JSONObject a)
			throws JSONException
	{
		List<String> indexKeys = EwJson.getKeysUnsorted(a);
		List<String> keys = EwJson.getKeysUnsorted((JSONObject) a.get(indexKeys.get(0)));
		if (optAsString("fields", parameters) != null)
			keys = new EwList<String>(optAsString("fields", parameters).split(","));
		if (optAsString("outputFields",parameters).equals("true"))
		{
			for (String s : keys)
			{
				if (!s.equals(keys.get(0)))
					accmData.append(",");
				accmData.append(encodeCsv(s));
			}
			accmData.append("\n");
		}
		for (String key : indexKeys)
		{
			JSONObject jo = (JSONObject) a.get(key);
			for (String s : keys)
			{
				if (!s.equals(keys.get(0)))
					accmData.append(",");
				if (jo.has(s))
					accmData.append(encodeCsv(jo.opt(s).toString()));
			}
			accmData.append("\n");
		}
	}
	public void simpleCentricAccumulate(Map<String, String[]> parameters, StringBuilder accmData, JSONObject a)
			throws JSONException
	{
		List<String> indexKeys = EwJson.getKeysUnsorted(a);
		for (String key : indexKeys)
		{
			String jo = a.get(key).toString();
			accmData.append(encodeCsv(key)+",");
			accmData.append(encodeCsv(jo));
			accmData.append("\n");
		}
	}

	public static String encodeCsv(String string)
	{
		return "\"" + string.replace("\"", "\"\"") + "\"";
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribution()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
