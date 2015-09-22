package com.eduworks.cruncher.file;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherCreateDirectory extends Cruncher {
	public Object resolve(Context c,
			java.util.Map<String, String[]> parameters, java.util.Map<String, java.io.InputStream> dataStreams)
			throws org.json.JSONException {
		
		String path = getAsString("path", c, parameters, dataStreams);
		if (optAsBoolean("safe",true,c,parameters, dataStreams) && path.contains(".."))
			throw new RuntimeException("Cannot go up in filesystem.");
		
		File f = new File(path);
		
		if(f.exists()){
			throw new RuntimeException("Directory Already Exists");
		}else{
			if(f.mkdir()){
				return true;
			}
			
			throw new RuntimeException("Unable to create directory at "+path);
		}

	}

	@Override
	public String getDescription() {
		return "Checks whether file exists on the filesystem.";
	}

	@Override
	public String getReturn() {
		return "InMemoryFile";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("path","String");
	};
}
