package com.eduworks.cruncher.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherSetFilename extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		if (obj instanceof InMemoryFile)
		{
			InMemoryFile inMemoryFile = (InMemoryFile) obj;
			inMemoryFile.name = getAsString("name", c, parameters, dataStreams);
			return inMemoryFile;
		}
		else if (obj instanceof File)
		{
			File f = (File) obj;
			try
			{
				InMemoryFile imf = new InMemoryFile(f);
				imf.name = getAsString("name", c, parameters, dataStreams);
				return imf;

			}
			catch (IOException e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		else
			return null;
	}

	@Override
	public String getDescription()
	{
		return "Sets the filename of an in-memory file";
	}

	@Override
	public String getReturn()
	{
		return "InMemoryfile";
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
