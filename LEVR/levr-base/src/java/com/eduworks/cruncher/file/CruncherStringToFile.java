package com.eduworks.cruncher.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherStringToFile extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(c, parameters, dataStreams);
		
		InMemoryFile rf = new InMemoryFile();
		rf.name = obj.getString("filename");
		if (obj.has("mimeType"))
			rf.mime = obj.getString("mimeType");
		else
			rf.mime = "text/plain";
		if (obj.get("data") instanceof InMemoryFile)
			rf.data = ((InMemoryFile)obj.get("data")).data;
		else if (obj.get("data") instanceof File) {
			InputStream fis = null;
			try {
				fis = new FileInputStream((File)obj.get("data"));
				rf.data = IOUtils.toByteArray(fis);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				IOUtils.closeQuietly(fis);
			}
		}
		else if (obj.get("data") instanceof String)
			rf.data = obj.getString("data").getBytes();
		else if (obj.get("data") instanceof JSONObject)
			rf.data = obj.getJSONObject("data").toString().getBytes();
		
		return rf;
	}

	@Override
	public String getDescription()
	{
		return "Takes JSONObject that contains a string with a mime type and filename to create a file out of them.";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONObject");
	}

	@Override
	public String getReturn()
	{
		return "File";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
}