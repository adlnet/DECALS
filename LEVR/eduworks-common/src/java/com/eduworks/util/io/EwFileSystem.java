package com.eduworks.util.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.eduworks.lang.EwSet;
import com.google.common.base.Predicate;

public class EwFileSystem
{
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows()
	{
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac()
	{
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix()
	{
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isSolaris()
	{
		return (OS.indexOf("sunos") >= 0);
	}

	public static String webConfigurationPath = null;

	public static String getWebConfigurationPath()
	{
		try
		{
			getDefaultLocationPath(true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (webConfigurationPath == null)
			if (System.getProperty("eduworks.webapp.config") != null)
				webConfigurationPath = new File(System.getProperty("eduworks.webapp.config")).getParent();
		return webConfigurationPath;
	}

	public static File tryFindFile(String estimatedPath, Class<? extends Object> inThisClassJar, boolean permanantFile, boolean isWebResource)
	{
		try
		{
			return findFile(estimatedPath, inThisClassJar, permanantFile, isWebResource);
		}
		catch (IOException e)
		{
			return null;
		}
	}

	public static File copyPackage(final String pkg, final Class<? extends Object> inThisClassJar) throws IOException
	{
		Collection<URL> urlsForCurrentClasspath = ClasspathHelper.forManifest();// forPackage(pkg,
																			// ClassLoader.getSystemClassLoader());
		EwSet<URL> urls = new EwSet<URL>();
		for (URL url : urlsForCurrentClasspath)
			urls.add(url);

		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(
				new ResourcesScanner().filterResultsBy(new Predicate<String>()
				{
					@Override
					public boolean apply(String input)
					{
						try
						{
							findFile(input, inThisClassJar, true, false);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						return true;
					}
				})));
		Set<String> resources = reflections.getResources(Pattern.compile(".*"));
		for (String s : resources)
			if (s.startsWith(pkg))
				findFile(s, inThisClassJar, true, false);
		return new File(getDefaultLocationPath(false), pkg);
	}

	public static File copyPackageToRoot(final String pkg, final Class<? extends Object> inThisClassJar) throws IOException
	{
		Collection<URL> urlsForCurrentClasspath = ClasspathHelper.forManifest();// forPackage(pkg,
																			// ClassLoader.getSystemClassLoader());
		EwSet<URL> urls = new EwSet<URL>();
		for (URL url : urlsForCurrentClasspath)
			urls.add(url);

		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(
				new ResourcesScanner().filterResultsBy(new Predicate<String>()
				{
					@Override
					public boolean apply(String input)
					{
						try
						{
							findFileIn(input, input, inThisClassJar, true, false);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						return true;
					}
				})));
		Set<String> resources = reflections.getResources(Pattern.compile(".*"));
		for (String s : resources)
			if (s.startsWith(pkg))
				findFileIn(s, s, inThisClassJar, true, false);
		return new File(pkg);
	}

	public static File copyPackageChildrenToRoot(final String pkg, final Class<? extends Object> inThisClassJar) throws IOException
	{
		Collection<URL> urlsForCurrentClasspath = ClasspathHelper.forManifest();// forPackage(pkg,
																			// ClassLoader.getSystemClassLoader());
		EwSet<URL> urls = new EwSet<URL>();
		for (URL url : urlsForCurrentClasspath)
			urls.add(url);

		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(
				new ResourcesScanner().filterResultsBy(new Predicate<String>()
				{
					@Override
					public boolean apply(String input)
					{
						try
						{
							findFileIn(input.replace(pkg + "/", ""), input, inThisClassJar, true, false);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						return true;
					}
				})));
		Set<String> resources = reflections.getResources(Pattern.compile(".*"));
		for (String s : resources)
			if (s.startsWith(pkg))
				findFileIn(s.replace(pkg + "/", ""), s, inThisClassJar, true, false);
		return new File(".");
	}

	/*
	 * Estimated Path should not include '/'.
	 */
	public static File findFileIn(String desiredPath, String estimatedPath, Class<? extends Object> inThisClassJar, boolean permanantFile, boolean isWebResource)
			throws IOException
	{

		File targetPath = new File(desiredPath);
		if (targetPath.exists())
			return targetPath;

		URL possibleFile = null;
		InputStream possibleInputStream = null;
		if (inThisClassJar != null)
		{
			possibleFile = inThisClassJar.getResource(estimatedPath);
			possibleInputStream = inThisClassJar.getResourceAsStream(estimatedPath);
			if (possibleFile == null)
			{
				possibleFile = inThisClassJar.getResource("/" + estimatedPath);
				possibleInputStream = inThisClassJar.getResourceAsStream("/" + estimatedPath);
			}
		}
		if (possibleFile == null)
		{
			possibleFile = EwFileSystem.class.getResource(estimatedPath);
			possibleInputStream = EwFileSystem.class.getResourceAsStream(estimatedPath);
		}
		if (possibleFile == null)
		{
			possibleFile = EwFileSystem.class.getResource("/" + estimatedPath);
			possibleInputStream = EwFileSystem.class.getResourceAsStream("/" + estimatedPath);
		}
		if (possibleFile == null)
		{
			File file = new File(desiredPath);
			if (file.exists())
			{
				possibleFile = file.toURI().toURL();
				possibleInputStream = new FileInputStream(file);
			}
		}
		if (possibleFile == null)
			throw new IOException("Could not find file: " + estimatedPath);

		if (possibleInputStream != null)
		{
			if (targetPath.exists() == false)
			{
				if (targetPath.getParentFile() != null)
					targetPath.getParentFile().mkdirs();
				targetPath.createNewFile();
			}
			FileOutputStream targetOutputStream = new FileOutputStream(targetPath);
			IOUtils.copy(possibleInputStream, targetOutputStream);
			IOUtils.closeQuietly(targetOutputStream);
			IOUtils.closeQuietly(possibleInputStream);
		}
		targetPath.mkdirs();

		if (!permanantFile)
			targetPath.deleteOnExit();

		return targetPath;
	}
	
	public static File findFile(String estimatedPath, Class<? extends Object> inThisClassJar, boolean permanantFile, boolean isWebResource) throws IOException
	{
		String rootPath = null;

		if (isWebResource)
			rootPath = getWebConfigurationPath();

		if (rootPath == null)
		{
			rootPath = getDefaultLocationPath(isWebResource);
		}

		File targetPath = new File(rootPath, estimatedPath);
		if (targetPath.exists())
			return targetPath;

		URL possibleFile = null;
		InputStream possibleInputStream = null;
		if (inThisClassJar != null)
		{
			possibleFile = inThisClassJar.getResource(estimatedPath);
			possibleInputStream = inThisClassJar.getResourceAsStream(estimatedPath);
			if (possibleFile == null)
			{
				possibleFile = inThisClassJar.getResource("/" + estimatedPath);
				possibleInputStream = inThisClassJar.getResourceAsStream("/" + estimatedPath);
			}
		}
		if (possibleFile == null)
		{
			possibleFile = EwFileSystem.class.getResource(estimatedPath);
			possibleInputStream = EwFileSystem.class.getResourceAsStream(estimatedPath);
		}
		if (possibleFile == null)
		{
			possibleFile = EwFileSystem.class.getResource("/" + estimatedPath);
			possibleInputStream = EwFileSystem.class.getResourceAsStream("/" + estimatedPath);
		}
		if (possibleFile == null)
		{
			File file = new File(estimatedPath);
			if (file.exists())
			{
				possibleFile = file.toURI().toURL();
				possibleInputStream = new FileInputStream(file);
			}
		}
		if (possibleFile == null)
			throw new IOException("Could not find file: " + estimatedPath);

		System.out.println(possibleFile);
		
		if (possibleInputStream != null)
		{
			if (targetPath.exists() == false)
			{
				targetPath.getParentFile().mkdirs();
				targetPath.createNewFile();
			}
			FileOutputStream targetOutputStream = new FileOutputStream(targetPath);
			IOUtils.copy(possibleInputStream, targetOutputStream);
			IOUtils.closeQuietly(targetOutputStream);
			IOUtils.closeQuietly(possibleInputStream);
		}
		targetPath.mkdirs();

		// TODO: If this thing is a directory, and it is in a jar, then we want
		// to copy all the files in the directory in the jar into the target
		// location.

		if (!permanantFile)
			targetPath.deleteOnExit();

		return targetPath;
	}

	/**
	 * Close a stream or reader/writer object.
	 */
	public static void closeIt(Object stream)
	{
		try
		{
			if (stream instanceof InputStream)
				((InputStream) stream).close();
			else if (stream instanceof OutputStream)
				((OutputStream) stream).close();
			else if (stream instanceof Reader)
				((Reader) stream).close();
			else if (stream instanceof Writer)
				((Writer) stream).close();
		}
		catch (IOException e)
		{
			// TODO: log this instead?
			e.printStackTrace();
		}
	}

	/**
	 * Download the content of a URL to a specific local file or a temporary
	 * file if no local file is specified. Returns the local file used.
	 * 
	 * @param timeout
	 */
	public static File downloadFile(String path, File localFile, int timeout) throws IOException
	{
		URL uri;
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try
		{
			try
			{
				uri = new URL(path);
			}
			catch (MalformedURLException e)
			{
				uri = new URL(URLDecoder.decode(path));
			}
			URLConnection connection = uri.openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setRequestProperty("Accept", "*/*");
			connection.connect();
			inputStream = connection.getInputStream();
			// If the local file is null, create a temporary file to hold the
			// content
			if (localFile == null)
			{
				String ext = null;
				try
				{
					String headerField = connection.getHeaderField("content-disposition");
					if (headerField != null && headerField.split(";")[0].equals("attachment"))
					{
						String filename = headerField.split(";")[1].split("=")[1];
						if (filename.contains("."))
							ext = filename.split("\\.")[filename.split("\\.").length-1];
					}
				}
				catch (Exception ex)
				{
					System.out.println("Download of File: Could not determine extension appropriately from header.");
					System.out.println(uri);
					ex.printStackTrace();
				}
				if (ext == null)
					ext = uri.getPath().substring(uri.getPath().lastIndexOf("/") + 1);
				if (ext == null || ext.isEmpty())
					if (connection.getContentType() != null)
						if (!connection.getContentType().endsWith("/"))
							ext = connection.getContentType().substring(connection.getContentType().lastIndexOf('/') + 1);
				if (ext.contains("."))
					ext = ext.substring(ext.indexOf("."));
				localFile = File.createTempFile("foo", "." + removeNonazAZStatic(ext));
			}
			fileOutputStream = new FileOutputStream(localFile);
			if (connection.getContentEncoding() != null && connection.getContentEncoding().equals("gzip"))
				inputStream = new GZIPInputStream(inputStream);
			if (connection.getContentEncoding() != null && connection.getContentEncoding().equals("deflate"))
				inputStream = new DeflaterInputStream(inputStream);
			IOUtils.copy(inputStream, fileOutputStream);
			return localFile;
		}
		finally
		{
			closeIt(inputStream);
			closeIt(fileOutputStream);
		}
	}

	private static String removeNonazAZStatic(String _text)
	{
		for (int i = 0; i < _text.length(); i++)
		{
			char k = _text.charAt(i);
			if (!(k >= 'a' && k <= 'z') && !(k >= 'A' && k <= 'Z'))
			{
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < _text.length(); j++)
				{
					char c = _text.charAt(j);
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
						sb.append(_text.charAt(j));
				}
				return sb.toString();
			}
		}
		return _text;
	}

	public static String resolve(URL uri) throws IOException
	{
		ByteArrayOutputStream baos = null;
		InputStream inputStream = null;
		try
		{
			URLConnection connection = uri.openConnection();
			inputStream = connection.getInputStream();
			connection.connect();
			baos = new ByteArrayOutputStream();
			if (connection.getContentEncoding() != null && connection.getContentEncoding().equals("gzip"))
				inputStream = new GZIPInputStream(inputStream);
			if (connection.getContentEncoding() != null && connection.getContentEncoding().equals("deflate"))
				inputStream = new DeflaterInputStream(inputStream);
			IOUtils.copy(inputStream, baos);
			return new String(baos.toByteArray());
		}
		finally
		{
			closeIt(inputStream);
			closeIt(baos);
		}
	}

	public static File downloadFile(String path) throws IOException
	{
		return downloadFile(path, null, 5 * 60 * 1000);
	}

	public static File downloadFile(String path, int timeoutms) throws IOException
	{
		return downloadFile(path, null, timeoutms);
	}

	private static String getDefaultLocationPath(boolean isWebResource) throws IOException
	{
		if (webConfigurationPath != null)
			return webConfigurationPath;
		String rootPath;
		rootPath = createTempDirectory().getName();
		if (isWebResource)
			webConfigurationPath = rootPath;
		return rootPath;
	}

	private static File createTempDirectory() throws IOException
	{
		File createTempFile = File.createTempFile("ewww", "tmp");
		createTempFile.delete();
		createTempFile = new File(createTempFile.getParentFile(), "etc");
		createTempFile.mkdirs();
		createTempFile.mkdir();
		createTempFile.deleteOnExit();
		return createTempFile;
	}

	public static void deleteEventually(File file)
	{
		if (file == null)
			return;
		if (!file.exists())
			return;
		if (!file.delete())
			file.deleteOnExit();
	}

	public static void placeInWorkingDirectoryTemporarily(File findFile, String relativePath)
	{
		File dest = new File(relativePath);
		if (dest.exists())
			return;
		dest.getParentFile().mkdirs();
		FileInputStream input;
		try
		{
			input = new FileInputStream(findFile);
			dest.createNewFile();
			FileOutputStream output = new FileOutputStream(dest);
			IOUtils.copy(input, output);
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		dest.deleteOnExit();
	}

	public static void copyPackage(String path, String string, Class<? extends Object> class1)
	{

	}

}
