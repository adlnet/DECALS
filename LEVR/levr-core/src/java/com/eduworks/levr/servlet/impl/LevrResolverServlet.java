package com.eduworks.levr.servlet.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.lang.EwList;
import com.eduworks.lang.EwMap;
import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.util.EwCache;
import com.eduworks.lang.util.EwJson;
import com.eduworks.levr.servlet.LevrServlet;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolvable;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.ResolverFactory;
import com.eduworks.resolver.exception.SoftException;
import com.eduworks.resolver.lang.LevrResolverParser;
import com.eduworks.resolver.lang.LevrResolverV2Parser;
import com.eduworks.util.Tuple;
import com.eduworks.util.io.EwFileSystem;
import com.eduworks.util.io.InMemoryFile;

@SuppressWarnings("serial")
public class LevrResolverServlet extends LevrServlet
{
	public static JSONObject config;
	public static JSONObject functions;
	static File configFile;
	public static List<File> codeFiles;
	static long lastModified = 0;
	public static long lastChecked = 0;
	private static String embeddedCode = "";
	public static Object lock = new Object();

	public static void setEmbeddedCode(String code)
	{
		embeddedCode = code;
	}

	static
	{
		ResolverFactory.populateFactorySpecsDynamically();
	}

	public static boolean initConfig(PrintStream pw) throws IOException
	{
		if (lastChecked + 5000 < System.currentTimeMillis())
		{
			lastChecked = System.currentTimeMillis();
			if (config == null || getFilesLastModified(new File(EwFileSystem.getWebConfigurationPath())) != lastModified || codeFiles.size() == 0)
			{
				FileReader input = null;
				try
				{
					synchronized (lock)
					{
						config = new JSONObject();
						functions = new JSONObject();

						codeFiles = new EwList<File>();

						String embeddedName = "commands";
						JSONObject scriptPack = new JSONObject();
						scriptPack.put("function", "javascript");
						scriptPack.put("expression", embeddedCode);
						Map<String, JSONObject> scriptStreams = new EwMap<String, JSONObject>();
						scriptStreams.put(embeddedName, scriptPack);
						mergeInto(config, scriptStreams);

						loadAdditionalConfigFiles(new File(EwFileSystem.getWebConfigurationPath()));

					}
					for (String webService : EwJson.getKeys(functions))
					{
						if (webService.toLowerCase().endsWith("autoexecute"))
						{
							Context c = new Context();
							try
							{
								execute(log, true, webService, c, new HashMap<String, String[]>(), new HashMap<String, InputStream>(), true);
								c.success();
							}
							catch (Exception ex)
							{
								c.failure();
								log.debug("Auto-Execute failed.", ex);
							}
							c.finish();
						}
					}
					return true;
				}
				catch (JSONException e)
				{
					pw.println("Error in config: " + e.getMessage());
					e.printStackTrace();
					return false;
				}
				finally
				{
					IOUtils.closeQuietly(input);
				}
			}
		}
		return true;
	}

	public static void loadAdditionalConfigFiles(File f) throws JSONException
	{
		if (f.isDirectory())
			for (File f2 : f.listFiles())
				loadAdditionalConfigFiles(f2);
		else if (f.isFile())
		{
			FileInputStream fileHandle = null;
			try
			{
				if (f.getName().endsWith(".rsl"))
				{
					log.debug("Loading: " + f.getPath());
					codeFiles.add(f);
					mergeInto(config, LevrResolverParser.decodeStreams(f));
					lastModified = Math.max(f.lastModified(), lastModified);
				}
				if (f.getName().endsWith(".rs2"))
				{
					log.debug("Loading: " + f.getPath());
					codeFiles.add(f);
					mergeInto(config, functions, LevrResolverV2Parser.decodeStreams(f));
					lastModified = Math.max(f.lastModified(), lastModified);
				}
				JSONObject scriptPack = null;
				Map<String, JSONObject> scriptStreams = null;
				if (f.getName().endsWith(".psl"))
				{
					log.debug("Loading: " + f.getPath());
					codeFiles.add(f);
					fileHandle = new FileInputStream(f);
					String cleanFilename = f.getName().substring(0, f.getName().lastIndexOf("."));
					scriptPack = new JSONObject();
					scriptPack.put("function", "python");
					scriptPack.put("expression", IOUtils.toString(fileHandle));
					scriptStreams = new EwMap<String, JSONObject>();
					scriptStreams.put(cleanFilename, scriptPack);
					mergeInto(config, scriptStreams);
					lastModified = Math.max(f.lastModified(), lastModified);
				}
				if (f.getName().endsWith(".jsl"))
				{
					log.debug("Loading: " + f.getPath());
					codeFiles.add(f);
					fileHandle = new FileInputStream(f);
					String cleanFilename = f.getName().substring(0, f.getName().lastIndexOf("."));
					scriptPack = new JSONObject();
					scriptPack.put("function", "javascript");
					scriptPack.put("expression", IOUtils.toString(fileHandle));
					scriptStreams = new EwMap<String, JSONObject>();
					scriptStreams.put(cleanFilename, scriptPack);
					mergeInto(config, scriptStreams);
					lastModified = Math.max(f.lastModified(), lastModified);
				}
			}
			catch (NullPointerException ex)
			{
				System.out.println("Failed on " + f.getPath());
				ex.printStackTrace();
			}
			catch (IOException e)
			{
				System.out.println("Failed on " + f.getPath());
				e.printStackTrace();
			}
			finally
			{
				if (fileHandle != null)
					IOUtils.closeQuietly(fileHandle);
			}
		}
	}

	private static void mergeInto(JSONObject config2, Map<String, JSONObject> decodeStreams) throws JSONException
	{
		for (Entry<String, JSONObject> entry : decodeStreams.entrySet())
			config2.put((entry.getKey().startsWith("/") ? "" : "/") + entry.getKey(), entry.getValue());
	}

	private static void mergeInto(JSONObject config2, JSONObject functions2, Tuple<Map<String, JSONObject>, Map<String, JSONObject>> decodeStreams)
			throws JSONException
	{
		mergeInto(config2, decodeStreams.getFirst());
		for (Entry<String, JSONObject> entry : decodeStreams.getSecond().entrySet())
			functions2.put(entry.getKey().substring(1), entry.getValue());
	}

	@Override
	public String getServletPath()
	{
		return "/api/custom/*";
	}

	@Override
	public String getServletPathExample()
	{
		return "/api/custom";
	}

	@Override
	public String getServletUsage()
	{
		return "To be written.";
	}

	@Override
	public void go(boolean isPost, HttpServletRequest request, HttpServletResponse response, ServletOutputStream outputStream) throws IOException
	{
		String requestURI = request.getRequestURI();
		String requestString = requestURI.substring(requestURI.indexOf(getServletPathExample()) + getServletPathExample().length());
		Map<String, String[]> parameterMap = Collections.synchronizedMap(new HashMap<String, String[]>(request.getParameterMap()));
		String jsonpSecurityKey = getStringFromParameter(request, "sec", "");
		Map<String, InputStream> dataStreams = null;
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream pw = new PrintStream(outputStream);

		Context c = new Context(request, response, pw);
		
		if (isPost)
			if (ServletFileUpload.isMultipartContent(request))
			{
				try
				{
					dataStreams = decodeMultipartContent(c,request);
				}
				catch (FileUploadException e)
				{
					throw new IOException(e.getMessage());
				}
			}
			else
			{
				try
				{
					dataStreams = decodeSimpleContent(request);
				}
				catch (FileUploadException e)
				{
					throw new IOException(e.getMessage());
				}
			}
		
		if (isPost && !jsonpSecurityKey.isEmpty())
			pw = new PrintStream(os);
		else
			crossDomainFixStart(request, pw);

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "If-Modified-Since, Content-Type, Content-Range, Content-Disposition, Content-Description");

		if (!isPost && !jsonpSecurityKey.isEmpty())
			try
			{
				SoftReference<byte[]> softReference = holdingCache.get(jsonpSecurityKey);
				if (softReference == null)
					throw new Exception("Cannot find your data payload. Sorry.");
				byte[] payload = softReference.get();
				if (payload == null)
					throw new Exception("Lost your data payload. Please try again.");
				holdingCache.remove(jsonpSecurityKey);
				pw.write(payload);
			}
			catch (Exception e)
			{
				pw.print("{\"error\":\"" + e.toString() + "\"}");
			}
		else
		{
			try
			{
				execute(log, request, response, requestString, c, parameterMap, pw, dataStreams);
				c.success();
			}
			catch (JSONException e)
			{
				c.failure();
				if (response != null)
					response.setContentType("text/plain");
				pw.print("{\"error\":\"" + e.toString() + "\"}");
			}
			finally
			{
				c.finish();
			}
		}

		pw.flush();

		if (isPost && !jsonpSecurityKey.isEmpty())
			storePost(jsonpSecurityKey, os.toByteArray());
		else
			crossDomainFixEnd(request, pw);
		pw.flush();
	}

	static HashMap<String, SoftReference<byte[]>> holdingCache = new HashMap<String, SoftReference<byte[]>>();

	private void storePost(String sec, byte[] resultsAsString)
	{
		if (sec == null || sec.isEmpty())
			return;
		holdingCache.put(sec, new SoftReference<byte[]>(resultsAsString));
	}

	@SuppressWarnings("unchecked")
	private Map<String, InputStream> decodeMultipartContent(Context c, HttpServletRequest request) throws FileUploadException, IOException
	{
		LinkedHashMap<String, InputStream> results = new LinkedHashMap<String, InputStream>();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> parseRequest = upload.parseRequest(request);

		for (FileItem item : parseRequest)
		{
			c.filenames.put(item.getFieldName(),item.getName());
			results.put(item.getFieldName(), new ByteArrayInputStream(IOUtils.toByteArray(item.getInputStream())));
		}
		log.debug("Decoded " + results.size() + " multi part mime inputs.");
		return results;
	}

	@SuppressWarnings("unchecked")
	private Map<String, InputStream> decodeSimpleContent(HttpServletRequest request) throws FileUploadException, IOException
	{
		LinkedHashMap<String, InputStream> results = new LinkedHashMap<String, InputStream>();

		results.put("simple", new ByteArrayInputStream(IOUtils.toByteArray(request.getInputStream())));
		log.debug("Decoded " + results.size() + " raw input.");
		return results;
	}

	public static void execute(Logger log, HttpServletRequest request, HttpServletResponse response, String requestString, Context c,
			Map<String, String[]> parameterMap, PrintStream pw, Map<String, InputStream> dataStreams) throws IOException, JSONException
	{
		if (!initConfig(pw))
			return;

		final boolean flushCache = getParameter("flushCache", parameterMap);
		final boolean flushAllCache = getParameter("flushAllCache", parameterMap);
		final boolean inline = getParameter("inline", parameterMap);

		if (flushCache)
			Resolver.clearCache();
		if (flushAllCache)
		{
			EwCache.clearAll();
			Resolver.clearCache();
		}

		try
		{
			String ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.isEmpty())
				ip = request.getRemoteAddr();
			parameterMap.put("ip", new String[] { ip });
			parameterMap.put("threadId", new String[] { Thread.currentThread().getName() });

			ResolverFactory.populateFactorySpecsDynamically();
			Object result = execute(log, false, requestString, c, parameterMap, dataStreams, true);

			response.setHeader("cache-control", "private, no-cache, no-store");
			if (result instanceof String)
			{
				final EwJsonCollection json = EwJson.tryConvert(result);

				if (response != null && request != null && !response.isCommitted())
				{
					if (((String) result).startsWith("<html>"))
						response.setContentType("text/html");
					else if (getStringFromParameter(request, "callback", null) != null)
						response.setContentType("text/javascript");
					else if (json != null)
						response.setContentType("application/json");
					else
						response.setContentType("text/plain");
				}

				pw.println(result.toString());
			}
			else if (result instanceof Number)
			{
				if (response != null && !response.isCommitted())
					response.setContentType("text/plain");
				pw.println(result.toString());
			}
			else if (result instanceof EwJsonSerializable)
			{
				if (response != null && !response.isCommitted())
					response.setContentType("text/plain");
				pw.println(((EwJsonSerializable) result).toJsonObject());
			}
			else if (result instanceof InMemoryFile)
			{
				InMemoryFile f = (InMemoryFile) result;

				if (response != null && !response.isCommitted())
				{
					response.setContentType(((InMemoryFile) result).mime);
					if (f.name != null && !f.name.isEmpty())
						if (inline)
						{
							response.setHeader("cache-control", "public, max-age=3600");
							response.setHeader("content-disposition", "filename=" + f.name);
						}
						else
							response.setHeader("content-disposition", "attachment; filename=" + f.name);
				}

				pw.write(f.data);
				pw.flush();
				pw.close();
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (SoftException ex)
		{
			Resolver.clearThreadCache();
			execute(log, request, response, requestString, c, parameterMap, pw, dataStreams);
		}
		finally
		{
			Resolver.clearThreadCache();
		}
	}

	public static Object execute(Logger log, boolean useFunctions, String requestString, Context c, Map<String, String[]> parameterMap,
			Map<String, InputStream> dataStreams, boolean noisy) throws JSONException
	{
		Resolvable resolver = requestStringBackoff(requestString, useFunctions, parameterMap);
		if (noisy)
			log.info("Request: " + requestString + toString(parameterMap));
		long ms = System.currentTimeMillis();
		Object result = resolver.resolve(c, parameterMap, dataStreams);
		if (noisy)
			log.info("Response (" + (System.currentTimeMillis() - ms) + "ms): " + requestString + toString(parameterMap));
		return result;
	}

	public static Resolvable requestStringBackoff(String requestString, boolean useFunctions, Map<String, String[]> parameterMap) throws JSONException
	{
		// Try full string, then back off a directory at a time until it works.
		// If it works, then add the remaining part to a parameter.
		String oldRequestString = requestString;
		String paramString = "";
		while (requestString.contains("/") && requestString.length() > 0 && config.has(requestString) == false
				&& (!useFunctions || functions.has(requestString)))
		{
			paramString = requestString.substring(requestString.lastIndexOf("/"))+paramString;
			requestString = requestString.substring(0, requestString.lastIndexOf("/"));
		}
		if (requestString.equals(""))
			requestString = oldRequestString;
		parameterMap.put("urlRemainder", new String[] { paramString });
		JSONObject jsonObject=null;
		synchronized (lock)
		{
			jsonObject = config.optJSONObject(requestString);
			if (useFunctions && jsonObject == null)
			{
				jsonObject = functions.getJSONObject(requestString);
			}
		}
		if (jsonObject == null)
			throw new RuntimeException("Service does not exist: " + requestString);
		return (Resolvable) ResolverFactory.create(jsonObject);
	}

	private static long getFilesLastModified(File dir)
	{
		long lmodified = 0;
		for (File f : dir.listFiles())
		{
			if (f.isDirectory())
				lmodified = Math.max(lmodified, getFilesLastModified(f));
			else if (f.isFile())
			{
				if (f.getName().endsWith(".rsl"))
				{
					lmodified = Math.max(f.lastModified(), lmodified);
				}
				if (f.getName().endsWith(".rs2"))
				{
					lmodified = Math.max(f.lastModified(), lmodified);
				}
				if (f.getName().endsWith(".psl"))
				{
					lmodified = Math.max(f.lastModified(), lmodified);
				}
				if (f.getName().endsWith(".jsl"))
				{
					lmodified = Math.max(f.lastModified(), lmodified);
				}
			}
		}
		return lmodified;
	}

	private static boolean getParameter(String name, Map<String, String[]> parameterMap)
	{
		boolean flushCache = false;
		if (parameterMap.containsKey(name))
			flushCache = Boolean.parseBoolean(parameterMap.get(name)[0]);
		return flushCache;
	}

	private static String toString(Map<String, String[]> parameterMap)
	{
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String[]> e : parameterMap.entrySet())
		{
			sb.append("~" + e.getKey());
			if (e.getValue() != null)
				for (int i = 0; i < e.getValue().length; i++)
					sb.append(":" + e.getValue()[i]);
		}
		return sb.toString();
	}

}
