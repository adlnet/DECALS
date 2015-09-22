package com.eduworks.cruncher.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherHttpStatus  extends Cruncher {
   
   private static final String REQUEST_METHOD = "GET";
   private static final int CONNECT_TIMOUT = 15000;

   @Override
   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
      String url = getAsString("url", c, parameters, dataStreams);
      if (url == null || url.trim().isEmpty()) return "INVALID URL";
      String ret = null;
      try {
         URL u = new URL(url); 
         HttpURLConnection conn =  (HttpURLConnection)u.openConnection(); 
         conn.setRequestMethod(REQUEST_METHOD); 
         conn.setInstanceFollowRedirects(false);
         conn.setConnectTimeout(CONNECT_TIMOUT);
         conn.setReadTimeout(CONNECT_TIMOUT);
         conn.connect(); 
         String rc = String.valueOf(conn.getResponseCode());
         conn.disconnect();
         ret = rc;
      }
      catch ( java.net.UnknownHostException uhe) {ret = "UNKNOWN HOST";}
      catch (java.net.SocketTimeoutException ste) {ret = "TIMED OUT (" + CONNECT_TIMOUT + "ms)";}
      catch (Exception e) {ret = "CONNECTION EXCEPTION (" + CONNECT_TIMOUT + "ms): " + e.toString();}
      return ret;
   }

   @Override
   public String getDescription() {
      return "Returns the HTTP status of the given URL.";
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
      return jo("url","String");
   }

}
