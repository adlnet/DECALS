package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRemoveFromArray extends Cruncher 
{

   @Override
   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException 
   {
      Object obj = getObj(c, parameters, dataStreams);
      if (obj == null) return null;
      JSONArray ja = (JSONArray) obj;
      EwJsonArray returnArray = new EwJsonArray();
      String item = getAsString("item", c, parameters, dataStreams);
      if (item == null)
    	  item = getAsString("remove", c, parameters, dataStreams);
      for (int i=0; i < ja.length(); i++) 
      {
         if (!ja.get(i).equals(item)) returnArray.put(ja.get(i));
      }      
      return returnArray;
   }

   @Override
   public String getDescription()
   {
      return "Removes an item from the given array";
   }

   @Override
   public String getReturn()
   {
      return "JSONArray";
   }

   @Override
   public String getAttribution()
   {
      return ATTRIB_NONE;
   }

   @Override
   public JSONObject getParameters() throws JSONException
   {
      return jo("obj","JSONArray","item","String|Number");
   }

}
