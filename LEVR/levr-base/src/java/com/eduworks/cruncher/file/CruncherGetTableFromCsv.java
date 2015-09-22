package com.eduworks.cruncher.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.csv.CSVParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGetTableFromCsv extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		CSVParser csvp = new CSVParser(new StringReader(getObj(c,parameters, dataStreams).toString()));
		boolean ignoreFirstLine = optAsBoolean("ignoreFirstLine", false, c, parameters, dataStreams);
		
		String[][] allValues;
		try
		{
			allValues = csvp.getAllValues();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		boolean first = true;
		JSONArray results = new JSONArray();
		for (String[] ary : allValues)
		{
			if (first && ignoreFirstLine) {first = false;continue;}
			JSONArray interim = new JSONArray();
			results.put(interim);
			for (String s : ary)
				interim.put(s);
		}
		return results;
	}

	@Override
	public String getDescription()
	{
		return "Converts a CSV document into a JSONArray of JSONArrays.";
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
		return jo("obj","String");
	}

}
