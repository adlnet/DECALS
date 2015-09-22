package com.eduworks.cruncher.db.sql;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;

public class CruncherSqlUpdate extends SqlTypeCruncher {
   
   private int numRowsAffected;

   @Override
   public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
      try {
         if (!loadParams(c, parameters, dataStreams)) return null;
         openDbConnection(c);         
         executeSqlUpdate();
         return numRowsAffected;
      }
      catch (Exception e) {
         e.printStackTrace();
         return -1;
      }
      finally {
         closeAndClean();
      }
   }
   
   private void executeSqlUpdate() {      
      try {
         setStmt(getConn().createStatement());
         numRowsAffected = getStmt().executeUpdate(getSqlStatement());
         commitTransaction();
      }
      catch (Exception e) {
         log.debug("Exception executing sql update: " + getSqlStatement());
         rollbackTransaction();         
      }
   }
   
   @Override
   public String[] getResolverNames() {
      return new String[]{getResolverName(),"sqlInsert","sqlDelete"};
   }

   @Override
   public String getDescription() {
      return "Executes the given sql update, insert, or delete (obj) and returns the number of rows affected.  Returns -1 if an error occurred.";
   }

   @Override
   public String getReturn() {
      return "Number";
   }

   @Override
   public String getAttribution() {
      return ATTRIB_NONE;
   }

   @Override
   public JSONObject getParameters() throws JSONException {
      return jo("obj","String","sqlConnectionString","String","sqlUsername","String","sqlPassword","String","sqlMysql","boolean","sqlSqlServer","boolean","sqlJtds","boolean");
   }

}
