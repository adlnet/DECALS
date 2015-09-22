package com.eduworks.cruncher.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.InMemoryFile;

public class ResolverDataFromPost extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		boolean soft = optAsBoolean("soft", false, parameters);
		EwList<String> nameField = optAsStrings("name", parameters);
		Integer maxPage = optAsInteger("pages", Integer.MAX_VALUE, parameters);
		if (nameField != null && !nameField.isEmpty())
		{
			try
			{
				StringBuilder result = new StringBuilder();
				for (String name : nameField)
				{
					JSONObject results = new JSONObject();
					getDataFromPost(results, dataStreams, name, maxPage);
					for (String key : EwJson.getKeys(results))
						result.append(results.getString(key));
				}
				return result.toString();
			}
			catch (IOException e)
			{
				if (soft)
					return null;
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		else
		{
			EwList<String> except = optAsStrings("except", parameters);
			if (optAsBoolean("_asObject", false, parameters))
			{
				JSONObject results = new JSONObject();
				if (dataStreams == null)
					return null;
				for (Entry<String, InputStream> entry : dataStreams.entrySet())
				{
					String key = entry.getKey();
					if (except.contains(key))
						continue;
					try
					{
						entry.getValue().reset();
						getDataFromPost(results, dataStreams, key, maxPage);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				if (results.length() == 0)
					return null;
				return results;
			}
			else
			{
				JSONObject results = new JSONObject();
				if (dataStreams == null)
					return null;
				for (Entry<String, InputStream> entry : dataStreams.entrySet())
				{
					String key = entry.getKey();
					if (except.contains(key))
						continue;
					try
					{
						entry.getValue().reset();
						getDataFromPost(results, dataStreams, key, maxPage);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				if (results.length() == 0)
					return null;
				return results;
			}
		}
	}

	private void getDataFromPost(JSONObject results, Map<String, InputStream> dataStreams, String nameField, Integer maxPage) throws IOException,
			JSONException
	{
		if (dataStreams == null)
			return;
		if (!dataStreams.containsKey(nameField))
			return;
		InputStream document = dataStreams.get(nameField);
		document.reset();
		InMemoryFile imf = new InMemoryFile();
		imf.data = IOUtils.toByteArray(document);
		imf.name = nameField;
		InputStream is = imf.getInputStream();
		
		try
		{
			results.put(imf.name, IOUtils.toString(imf.data));
		}
		finally
		{
		}
	}

	@Override
	public String getDescription()
	{
		return "Retreives data from all multipart or simple data streams. " + "Use 'name' to retreive one specifically. "
				+ "Use 'except' to retreive everything except one specifically." + "Use '_asObject' to retreive data as an object.";
	}

	@Override
	public String getReturn()
	{
		return "List";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("?name", "String", "?except", "String", "?_asObject", "Boolean");
	}

}
