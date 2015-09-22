package com.eduworks.cruncher.uuid;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGenerateUUID extends Cruncher {

   @Override
   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
      return UUID.randomUUID().toString();
   }

   @Override
   public String getDescription() {
      return "Generates and returns a random UUID using the java.util.UUID.randomUUID method.";
   }

   @Override
   public String getReturn() {
      return "String";
   }

   @Override
   public String getAttribution() {
      return ATTRIB_NONE;
   }

   @Override
   public JSONObject getParameters() throws JSONException {
      return null;
   }

}
