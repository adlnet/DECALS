package com.eduworks.cruncher.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFileToString extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj =  getObj(c, parameters, dataStreams);
		
		if(obj instanceof InMemoryFile){
			InMemoryFile file = (InMemoryFile) obj;
			try
			{
				return IOUtils.toString(file.data);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else if(obj instanceof EwList<?>){
			EwList<InMemoryFile> fileList = (EwList<InMemoryFile>) obj;
			
			EwJsonArray array = new EwJsonArray();
			
			try {
				for(InMemoryFile file : fileList){
					array.put(IOUtils.toString(file.data));
				}
				
				return array;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Converts an in memory file to a string.";
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
