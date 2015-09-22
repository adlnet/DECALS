package com.eduworks.cruncher.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.EwFileSystem;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFilesToZip extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		Map<String, ?> files = null;
		if (obj instanceof Map)
			files = (Map<String, ?>) obj;
		else if (obj instanceof JSONObject)
		{
			JSONObject jo = (JSONObject) obj;
			Map<String, Object> m = new HashMap<String, Object>();
			for (Iterator<String> it = jo.keys(); it.hasNext();)
			{
				String s = it.next();
				m.put(s, jo.get(s));
			}
			files = m;
		}
		else if (obj instanceof JSONArray)
		{
			JSONArray ja = (JSONArray) obj;
			Map<String, Object> m = new HashMap<String, Object>();
			for (int i = 0;i < ja.length();i++)
			{
				Object object = ja.get(i);
				if (object instanceof InMemoryFile)
				{
				InMemoryFile imf = (InMemoryFile) object;
				m.put(imf.name, imf);
				}
				else if (object instanceof File)
				{
					File f = (File) object;
					m.put(f.getName(),f);
				}
			}
			files = m;
		}

		File f;
		try
		{
			f = File.createTempFile("foo", "zip");
		}
		catch (IOException e1)
		{
			throw new RuntimeException("Cannot create temp file.");
		}

		byte[] buf = new byte[4096];

		try
		{
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));

			for (String filename : files.keySet())
			{
				Object value = files.get(filename);
				if (value instanceof InMemoryFile)
				{
					InMemoryFile fi = (InMemoryFile) value;

					if (fi.name == null)
						out.putNextEntry(new ZipEntry(filename));
					else
						out.putNextEntry(new ZipEntry(fi.name));

					out.write(fi.data);
				}else if (value instanceof File)
				{
					File fi = (File) value;

						out.putNextEntry(new ZipEntry(filename));

					out.write(FileUtils.readFileToByteArray(fi));
				}
				else
				{
					String realFilename = filename + getExt(value);

					out.putNextEntry(new ZipEntry(realFilename));

					out.write(toByteArray(value));
				}
				out.closeEntry();
			}

			out.close();
		}
		catch (IOException e)
		{
		}
		InMemoryFile fi = new InMemoryFile();
		try
		{
			fi.data = IOUtils.toByteArray(new FileInputStream(f));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		fi.mime = "application/zip";
		fi.name = getAsString("name", c, parameters, dataStreams);
		EwFileSystem.deleteEventually(f);
		return fi;
	}

	private byte[] toByteArray(Object value)
	{
		if (value instanceof Document)
		{
			try
			{
				/* Create a transformation factory */
				TransformerFactory tf = TransformerFactory.newInstance();

				/* Create and configure a transformation factory */
				Transformer t = tf.newTransformer();
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				t.setOutputProperty(OutputKeys.STANDALONE, "yes");

				ByteArrayOutputStream baos;
				/* Write out and transform the document as we do so */
				StreamResult result = new StreamResult(new OutputStreamWriter(baos = new ByteArrayOutputStream(),
						"utf-8"));

				t.transform(new DOMSource((Document) value), result);
				return baos.toByteArray();
			}
			catch (TransformerException e)
			{
				e.printStackTrace();
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		if (value instanceof String)
		{
			return value.toString().getBytes();
		}
		return new byte[0];
	}

	private String getExt(Object value)
	{
		if (value instanceof Document)
			return ".html";
		return "";
	}

	@Override
	public String getDescription()
	{
		return "Takes a bundle of files specified by paths defined by nested JSON objects and finally ResolverFiles (or Files or Strings) and zips them up.";
	}

	@Override
	public String getReturn()
	{
		return "ResolverFile";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_TRADEM1;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONObject");
	}
}
