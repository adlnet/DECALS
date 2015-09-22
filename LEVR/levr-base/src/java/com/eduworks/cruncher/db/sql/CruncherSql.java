package com.eduworks.cruncher.db.sql;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;

public class CruncherSql extends SqlTypeCruncher {
   
   private JSONArray dataSet = new JSONArray();

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
	   try {
	      if (!loadParams(c, parameters, dataStreams)) return null;
	      openDbConnection(c);
	      executeSqlStatement();
	      parseResultSet();
	      return dataSet;
	   }
	   catch (Exception e) {
	      e.printStackTrace();
	      return null;
	   }
	   finally {
	      closeAndClean();
	   }
	}
	
	private void executeSqlStatement() throws Exception {
	   try {
   	   setStmt(getConn().createStatement());
   	   setRs(getStmt().executeQuery(getSqlStatement()));
   	   while(getRs().next()) {
   	      dataSet.put(parseCurrentResultSetRow());	      
   	   }
	   }
	   catch (Exception e) {
	      log.debug("Exception executing sql statement:" + getSqlStatement());
         throw e;
	   }
	}
	
	private void parseResultSet() throws Exception {
	   try {
         while(getRs().next()) {
            dataSet.put(parseCurrentResultSetRow());        
         }
      }
      catch (Exception e) {
         log.debug("Exception parsing result set:" + getSqlStatement());
         throw e;
      }
	}
	
	private JSONObject parseCurrentResultSetRow() throws Exception {
	   JSONObject dataRow = new JSONObject();
	   for (int i = 1; i <= getRs().getMetaData().getColumnCount(); i++) {
         String colName = getRs().getMetaData().getColumnName(i);
         int colType = getRs().getMetaData().getColumnType(i);
         if (colType == java.sql.Types.CHAR)
            dataRow.put(colName, getRs().getString(colName).trim());
         else if (colType == java.sql.Types.VARCHAR)
            dataRow.put(colName, getRs().getString(colName).trim());
         else if (colType == java.sql.Types.INTEGER)
            dataRow.put(colName, getRs().getInt(colName));
         else if (colType == java.sql.Types.DECIMAL)
            dataRow.put(colName, getRs().getBigDecimal(colName));
         else if (colType == java.sql.Types.NUMERIC)
            dataRow.put(colName, getRs().getDouble(colName));
         else if (colType == java.sql.Types.CLOB)
            dataRow.put(colName, clobToString(getRs().getClob(colName)));
      }
	   return dataRow;	   
	}
	
	private String clobToString(Clob c) throws Exception {
	   Reader in = c.getCharacterStream();
	   StringWriter w = new StringWriter();
	   IOUtils.copy(in, w);
	   return w.toString();
	}
	
	@Override
   public String[] getResolverNames() {
      return new String[]{getResolverName(),"sqlQuery"};
   }
	
	@Override
	public String getDescription() {
	   return "Executes the given sql statment (obj) and returns the result set as a JSON array of JSON objects.";
	}

	@Override
	public String getReturn() {
	   return "JSONArray";
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
