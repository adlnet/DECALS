package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherExtractObjectsFromArray  extends Cruncher {
   
   @Override
   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
     try {
        JSONArray ja = getObjAsJsonArray(c, parameters, dataStreams);
        JSONObject retObj = new JSONObject();        
        for (int i=0;i<ja.length();i++) {
           if (ja.get(i) instanceof JSONObject) addObjToReturnObj(ja.getJSONObject(i),retObj);
        }
        return retObj;
     }
     catch (Exception e) {
        e.printStackTrace();
        return null;
     }     
   }
   
   @SuppressWarnings("unchecked")   
   private void addObjToReturnObj(JSONObject valueObj, JSONObject returnObj) throws Exception {
      Iterator<String> keyIt;
      String key;      
      keyIt = (Iterator<String>)valueObj.keys();
      while (keyIt.hasNext()) {
         key = keyIt.next();
         returnObj.put(key,valueObj.get(key));
      }
   }

   @Override
   public String getDescription() {
      return "Extracts JSONObjects from a JSONArray and returns them as a JSONObject.  " +
             "If the same key is encountered multiple times, then the last one encountered takes precedent.";
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
      return jo("obj","JSONArray");
   }

}
