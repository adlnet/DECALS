package com.eduworks;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.eduworks.util.io.EwFileSystem;

/***
 * Helper class to get the version number from the version file.
 * 
 * @author fray
 *
 */
public class EwVersion
{
	/***
	 * Gets the version from the version file.
	 * @return Version String
	 */
	public static String getVersion()
	{
		try
		{
			return FileUtils.readFileToString(EwFileSystem.findFile("build.version", EwVersion.class, false, false)).trim();
		}
		catch (IOException e)
		{
			return "unknown";
		}
	}
}
