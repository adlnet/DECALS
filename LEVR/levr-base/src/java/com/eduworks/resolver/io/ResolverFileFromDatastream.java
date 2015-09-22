package com.eduworks.resolver.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.InMemoryFile;

public class ResolverFileFromDatastream extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		String nameField = optAsString("name", parameters);
		Boolean useInMemory = optAsBoolean("inMemory", true, parameters);
		Boolean useFileName = optAsBoolean("useFileName", false, parameters);
		if (!nameField.isEmpty())
		{
			try
			{
				if (useInMemory)
					return getRFileFromPost(c,dataStreams, nameField,useFileName);
				else
					return getFileFromPost(c,dataStreams, nameField,useFileName);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				throw new RuntimeException("Could not find name of file in POST.");
			}
		}
		else
		{
			String except = optAsString("except", parameters);
			EwList<Object> results = new EwList<Object>();
			if (dataStreams != null)
			for (Entry<String, InputStream> entry : dataStreams.entrySet())
			{
				String key = entry.getKey();
				if (key.equals(except))
					continue;
				try
				{
					entry.getValue().reset();
					if (useInMemory)
						results.add(getRFileFromPost(c,dataStreams, key,useFileName));
					else
						results.add(getFileFromPost(c,dataStreams, key,useFileName));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			return results;
		}
	}

	private InMemoryFile getRFileFromPost(Context c, Map<String, InputStream> dataStreams, String nameField, boolean useFileName) throws IOException,
			FileNotFoundException
	{
		if (dataStreams == null)
			return null;
		if (!dataStreams.containsKey(nameField))
			return null;
		InputStream document = dataStreams.get(nameField);
		document.reset();
		InMemoryFile createTempFile = new InMemoryFile();
		if (useFileName && c.filenames.containsKey(nameField))
			createTempFile.name = c.filenames.get(nameField);
		if (createTempFile.name == null || createTempFile.name.isEmpty())
			createTempFile.name = nameField;
		createTempFile.data = IOUtils.toByteArray(document);
		return createTempFile;
	}
	
	private File getFileFromPost(Context c, Map<String, InputStream> dataStreams, String nameField, boolean useFileName) throws IOException,
			FileNotFoundException
	{
		if (dataStreams == null)
			return null;
		if (!dataStreams.containsKey(nameField))
			return null;
		InputStream document = dataStreams.get(nameField);
		document.reset();
		String extension = "";
		if (useFileName && c.filenames.containsKey(nameField))
			extension = c.filenames.get(nameField);
		if (extension == null || extension.isEmpty())
			extension = nameField;
		if (extension != null)
			extension = extension.substring(Math.min(0, extension.lastIndexOf('.')));
		if (extension == null || extension.isEmpty())
			extension = "";
		File createTempFile = File.createTempFile("foo", extension);
		FileOutputStream fw = new FileOutputStream(createTempFile);
		try
		{
			IOUtils.copy(document, fw);
			return createTempFile;
		}
		finally
		{
			IOUtils.closeQuietly(fw);
			IOUtils.closeQuietly(document);
//			EwFileSystem.deleteEventually(createTempFile);
		}
	}
	@Override
	public String getDescription()
	{
		return "Retreives the files from the HTTP Multi part post (or simple post) and returns them as a List of InMemoryFiles" +
				"\nOptional Name = Name of MPP file to retreive." +
				"\nOptional Except = Retreive everything except the name provided.";
	}
	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"getFileFromPost"};
	}
	@Override
	public String getReturn()
	{
		return "List";
	}
	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("?name","String","?except","String");
	}

}
