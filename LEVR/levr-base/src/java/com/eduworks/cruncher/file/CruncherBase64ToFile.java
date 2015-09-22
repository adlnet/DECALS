package com.eduworks.cruncher.file;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.xerces.impl.dv.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherBase64ToFile extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(c, parameters, dataStreams);
		
		List<InMemoryFile> result = new EwList<InMemoryFile>();
		for (String key : EwJson.getKeys(obj))
		{
			InMemoryFile rf = new InMemoryFile();
			rf.name = key;
			rf.mime = optAsString("mimeType", null, c, parameters, dataStreams);
			rf.data = Base64.decode(obj.getString(key));
			result.add(rf);
		}
		if (result.size() == 0) return null;
		if (result.size() == 1) return result.get(0);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Converts one or more base 64 files to a ResolverFile";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSON Object, {filename:base64}");
	}

	@Override
	public String getReturn()
	{
		return "List of ResolverFile";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
}