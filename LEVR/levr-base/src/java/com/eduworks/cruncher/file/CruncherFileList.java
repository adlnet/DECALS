package com.eduworks.cruncher.file;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherFileList extends Cruncher
{
	public Object resolve(Context c, java.util.Map<String, String[]> parameters, java.util.Map<String, java.io.InputStream> dataStreams)
			throws org.json.JSONException
	{
		String path = getAsString("path", c, parameters, dataStreams);
		if (path.contains(".."))
			throw new RuntimeException("Cannot go up in filesystem.");
		File f = new File(path);
		JSONArray paths = new JSONArray();
		File dir = new File(path);
		if (dir.isDirectory() == false)
			throw new RuntimeException("Path does not refer to a directory.");
		for (String s : dir.list())
			paths.put(s);
		return paths;
	}

	@Override
	public String getDescription()
	{
		return "Returns paths to all files referred to by path.";
	}

	@Override
	public String getReturn()
	{
		return "InMemoryFile";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("path", "String");
	};
}
