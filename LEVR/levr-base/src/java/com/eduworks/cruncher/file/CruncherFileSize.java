package com.eduworks.cruncher.file;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherFileSize extends Cruncher {
   
   @Override
   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
   {
      return String.valueOf(((InMemoryFile)getObj(c, parameters, dataStreams)).data.length);
   }

   @Override
   public String getDescription()
   {
      return "Retrieves the size in bytes of an in-memory file";
   }

   @Override
   public String getReturn()
   {
      return "String";
   }

   @Override
   public String getAttribution()
   {
      return ATTRIB_NONE;
   }

   @Override
   public JSONObject getParameters() throws JSONException
   {
      return jo("obj","InMemoryFile");
   }

}
