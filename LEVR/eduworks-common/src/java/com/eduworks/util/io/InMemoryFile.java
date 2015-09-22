package com.eduworks.util.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

public class InMemoryFile
{
	public String name;
	public String mime;
	public byte[] data;
	public String path;

	public InMemoryFile()
	{
		// TODO Auto-generated constructor stub
	}

	public InMemoryFile(File source) throws IOException
	{
		name = source.getName();
		data = FileUtils.readFileToByteArray(source);
	}

	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(data);
	}

	public File toTemporaryFile()
	{
		try
		{
			File f = null;
			if (name == null)
				f=File.createTempFile("foo", "unk");
			else
				f=File.createTempFile("foo", name);
			FileUtils.writeByteArrayToFile(f, data);
			return f;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
