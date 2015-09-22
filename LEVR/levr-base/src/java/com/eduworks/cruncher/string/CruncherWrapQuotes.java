package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherWrapQuotes extends Cruncher
{
	  @Override
	   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	   {
	      String str = getAsString("str",c,parameters, dataStreams);
	      
	      return "\"" + str + "\"";
	   }

	   @Override
	   public String getDescription()
	   {
	      return "Returns the index of the where the first character in substr appears in str, or -1 if it doesn't substr is not in str";
	   }

	   @Override
	   public String getReturn()
	   {
	      return "int";
	   }

	   @Override
	   public String getAttribution()
	   {
	      return ATTRIB_NONE;
	   }

	   @Override
	   public JSONObject getParameters() throws JSONException
	   {
	      return jo("str","String","substr","String");
	   }

}
