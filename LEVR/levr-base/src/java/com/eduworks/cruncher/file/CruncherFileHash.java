package com.eduworks.cruncher.file;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFileHash extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj =  getObj(c, parameters, dataStreams);
		
		if(obj instanceof InMemoryFile){
			InMemoryFile file = (InMemoryFile) obj;
			MessageDigest md = null;
			
			try
			{
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			DigestInputStream dis = new DigestInputStream(file.getInputStream(), md);
			int numRead;
			byte[] buffer = new byte[1024];
			
			try {
				do{
					numRead = dis.read(buffer);
				}while(numRead != -1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return md.digest();
			
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
