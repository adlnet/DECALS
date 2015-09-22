package com.eduworks.cruncher.refl;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.levr.servlet.impl.LevrResolverServlet;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;


public class CruncherReflectionCommit extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		try
		{
			for (File f : LevrResolverServlet.codeFiles)
			{
				if (!f.getName().equals(getAsString("filename", c, parameters, dataStreams)))
					continue;
				FileUtils.writeStringToFile(f, getObj(c, parameters, dataStreams).toString());
				LevrResolverServlet.lastChecked -= 5000;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Receives a file and commits it to an RS2 location.";
	}

	@Override
	public String getReturn()
	{
		return "null";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return null;
	}

}
