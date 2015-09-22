package com.eduworks.cruncher.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherZipToFiles extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
		Object obj = getObj(c, parameters, dataStreams);
		EwJsonArray files = new EwJsonArray();
		InMemoryFile f = null;
		ZipInputStream in = null;
		JSONObject filters = getAsJsonObject("filter", c, parameters, dataStreams);
		try
		{
			if (obj instanceof File)
				f = new InMemoryFile((File)obj);
			else if (obj instanceof InMemoryFile)
				f = (InMemoryFile)obj;
				
			byte[] buf = new byte[4096];

			in = new ZipInputStream(new ByteArrayInputStream(f.data));
			ZipEntry ze;
			
			while ((ze=in.getNextEntry())!=null) {
				if (!ze.isDirectory()) {
					InMemoryFile zipEntry = new InMemoryFile();
					if (ze.getName().lastIndexOf("/")!=-1)
						zipEntry.name=ze.getName().substring(ze.getName().lastIndexOf("/")+1);
					else
						zipEntry.name= ze.getName();
					zipEntry.path=ze.getName();
					ByteArrayOutputStream zipEntryStream = new ByteArrayOutputStream();
					int readLength = 0;
					while ((readLength=in.read(buf))>0) {
						zipEntryStream.write(buf, 0, readLength);
					}
					zipEntry.data=zipEntryStream.toByteArray();
					IOUtils.closeQuietly(zipEntryStream);
					if (filters!=null&&checkExtension(filters, zipEntry, zipEntryStream.toByteArray().length))
						files.put(zipEntry);
					else if (filters==null)
						files.put(zipEntry);
				}
			}
		}
		catch (IOException e) {} 
		finally {
			IOUtils.closeQuietly(in);
		}
		return files;
	}

	private Boolean checkExtension(JSONObject filters, InMemoryFile file, Integer fs) {
		Boolean valid = false;
		long filesize = Math.round(fs / 1024.0);
		String fileExtension = "";
			if (file.name.lastIndexOf(".")!=-1)
				fileExtension = file.name.substring(file.name.lastIndexOf(".")+1);
		
		for (Iterator<String> filterPointer = filters.keys(); filterPointer.hasNext() && !valid;) {
			String filterKey = filterPointer.next();
			Long filterSize = Long.parseLong(filterKey);
			if (filesize>filterSize) {
				JSONArray extensions;
				try {
					extensions = filters.getJSONArray(filterKey);
					for (int extensionIndex=0; extensionIndex < extensions.length() && !valid; extensionIndex++)
						if (extensions.getString(extensionIndex).equalsIgnoreCase(fileExtension))
							valid = true;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return valid;
	}
	
	@Override
	public String getDescription()
	{
		return "Takes a File(or InMemoryFile) that represents a zip and returns an array of the files filtered by filter object ({\"<filesizeKThreshold>\" : [<extensions>],...} contained within the zip";
	}

	@Override
	public String getReturn()
	{
		return "Array of InMemoryFile";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "File|InMemoryFile", "?filter", "JSONObject");
	}
}
