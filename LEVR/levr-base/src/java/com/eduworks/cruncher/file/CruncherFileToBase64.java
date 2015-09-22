package com.eduworks.cruncher.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFileToBase64 extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);

		JSONObject result = new JSONObject();
		try
		{
			if (obj instanceof File)
				encodeFile(result,(File) obj);
			else if (obj instanceof InMemoryFile)
				encodeFile(result,(InMemoryFile) obj);
			else if (obj instanceof List)
			{
				List files = (List) obj;
				for (Object f : files)
					if (f instanceof File)
						encodeFile(result,(File) f);
					else if (f instanceof InMemoryFile)
						encodeFile(result,(InMemoryFile) f);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	private void encodeFile(JSONObject result, File obj) throws JSONException, IOException
	{
		result.put(obj.getName(), Base64.encode(FileUtils.readFileToByteArray(obj)));
	}

	private void encodeFile(JSONObject result, InMemoryFile obj) throws JSONException, IOException
	{
		result.put(obj.name, Base64.encode(obj.data));
	}

	@Override
	public String getDescription()
	{
		return "Converts one or more files to a JSONObject where the filenames are keys and the Base64 encoded files are the values.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","File|List<File>");
	}

}
