package com.eduworks.resolver.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.json.XML;

import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.net.ResolverGetFileFromUrl;
import com.eduworks.util.io.EwFileSystem;


public class ResolverGetXmlFromUrlAsJson extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		int timeout = optAsInteger("timeout", 30000, parameters);
		boolean optAsBoolean2 = optAsBoolean("array", false, parameters);
		remove("timeout");
		remove("array");
		EwList<String> urls = getAsStrings("url", parameters);
		for (String url : urls)
			try
			{
				url = url.trim();
				if (url.isEmpty())
					continue;
				String cacheUrl = this.getClass().getName() + ":" + url + optAsBoolean("raw", false, parameters);
				String cacheKey = cacheUrl+":"+optAsBoolean2;
				Object cacheResult = null;//getCache(cacheKey);
				if (cacheResult != null)
				{
					put(url, cacheResult);
					continue;
				}
				Object text;
				if (optAsBoolean2)
					text = operateArray(c,url, timeout);
				else
					text = operate(c,url, timeout);
				if (text == null)
					continue;
				putCache(c,cacheKey, text);
				put(url, text);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

		remove("url");
		return this;
	}


	public static Object operate(Context c, String url, int timeout)
	{
		FileReader fileReader = null;
		try
		{
			String cacheUrl = ResolverGetXmlFromUrlAsJson.class.getName() + ":" + url;
			Object cacheResult = getCache(c,cacheUrl);
			if (cacheResult != null)
				return (JSONObject) cacheResult;
			File f = ResolverGetFileFromUrl.operate(c,url, timeout);

			{
				fileReader = new FileReader(f);
				String string = IOUtils.toString(fileReader);
				JSONObject result = XML.toJSONObject(string);
				putCache(c,cacheUrl,result);
				return result;
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally
		{
			com.eduworks.util.io.EwFileSystem.closeIt(fileReader);
		}
	}

	public static JSONArray operateArray(Context c,String url, int timeout)
	{
		FileReader fileReader = null;
		try
		{
			File f = ResolverGetFileFromUrl.operate(c,url, timeout);
			fileReader = new FileReader(f);
			return JSONML.toJSONArray(IOUtils.toString(fileReader));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally
		{
			EwFileSystem.closeIt(fileReader);
		}
	}


	@Override
	public String getDescription() {
		return "Retreives an XML document from the web and decodes it to JSON. Deprecated.";
	}


	@Override
	public String getReturn() {
		return "JSONObject";
	}


	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}


	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("url","String");
	}

}
