package com.eduworks.cruncher.file;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFilename extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		if (obj instanceof InMemoryFile)
			return ((InMemoryFile) obj).name;
		else if (obj instanceof File)
			return ((File) obj).getName();
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Retreives the filename of an in-memory file";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "InMemoryFile");
	}

}
