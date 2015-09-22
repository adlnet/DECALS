package com.eduworks.cruncher.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherDeserialize extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c,parameters,dataStreams);
		if (obj instanceof InMemoryFile)
		{
		InMemoryFile imf = (InMemoryFile) obj;
		return SerializationUtils.deserialize(imf.data);
		}
		else if (obj instanceof File)
		{
			FileInputStream openInputStream = null;
			try
			{
				openInputStream = FileUtils.openInputStream((File) obj);
				return SerializationUtils.deserialize(openInputStream);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				IOUtils.closeQuietly(openInputStream);
			}
		}
		return null;
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
