package com.eduworks.cruncher.db.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.ContextEvent;
import com.eduworks.resolver.Cruncher;

public abstract class SqlTypeCruncher extends Cruncher {
   
   protected static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
   protected static final String SQLSERVER_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
   protected static final String JTDS_DRIVER_CLASS = "net.sourceforge.jtds.jdbc.Driver";
   
   private String connectionString;
   private String userName;
   private String password;
   private String sqlStatement;
   
   private boolean isMySql;
   private boolean isSqlServer;
   private boolean isJtds;
   
   private Connection conn;
   private Statement stmt;
   private ResultSet rs;
   
   protected boolean loadParams(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) {
      try {
         connectionString = getAsString("sqlConnectionString", c, parameters, dataStreams);
         userName = getAsString("sqlUsername", c, parameters, dataStreams);
         password = getAsString("sqlPassword", c, parameters, dataStreams);
         isMySql = optAsBoolean("sqlMysql", false, c, parameters, dataStreams);
         isSqlServer = optAsBoolean("sqlSqlServer", false, c, parameters, dataStreams);
         isJtds = optAsBoolean("sqlJtds", false, c, parameters, dataStreams);
         sqlStatement = getObj(c, parameters, dataStreams).toString();
         log.debug("******************SQL STATEMENT: " + sqlStatement);
         return true;
      }
      catch (Exception e) {
         log.debug("Exception parsing parameters");
         e.printStackTrace();
         return false;
      }
   }
   
   protected void openDbConnection(Context c) throws Exception {openDbConnection(c,false);}
   
   protected void openDbConnection(Context c, boolean autoCommit) throws Exception {    
      try {
         if (isMySql) Class.forName(MYSQL_DRIVER_CLASS);
         else if (isSqlServer) Class.forName(SQLSERVER_DRIVER_CLASS);
         else if (isJtds) Class.forName(JTDS_DRIVER_CLASS);
         conn = (Connection) c.get(connectionString);
         if (conn == null) {
            conn = DriverManager.getConnection(connectionString,userName,password);
            final Connection conFinal = conn;
            c.onFinally(new ContextEvent() {
               @Override
               public void go() {
                  if (conFinal != null) {
                     try {
                        conFinal.close();
                     }
                     catch (SQLException e) {
                        e.printStackTrace();
                     }
                  }               
               }
            });
            if (conn != null && !conn.isClosed()) conn.setAutoCommit(autoCommit);
         }          
      }
      catch (Exception e) {
         log.debug("Exception opening database connection: " + sqlStatement);
         throw e;
      }
   }
   
   protected void closeAndClean() {
      try {
         if (rs != null) {
            rs.close();
            rs = null;
         }
         if (stmt != null) {
            stmt.close();
            stmt = null;
         }
      }
      catch (Exception e) {
         log.debug("Exception cleaning and closing:" + sqlStatement);
         e.printStackTrace();
      }
   }
   
   protected void rollbackTransaction() {
      try {
         conn.rollback();
      }
      catch (Exception e) {
         log.debug("Exception during transaction rollback: " + sqlStatement);
         e.printStackTrace();
      }
   }
   
   protected void commitTransaction() throws Exception {
      try {
         conn.commit();
      }
      catch (Exception e) {
         log.debug("Exception during transaction commit: " + sqlStatement);
         throw e;
      }
   }

   public String getConnectionString() {return connectionString;}
   public void setConnectionString(String connectionString) {this.connectionString = connectionString;}

   public String getUserName() {return userName;}
   public void setUserName(String userName) {this.userName = userName;}

   public String getPassword() {return password;}
   public void setPassword(String password) {this.password = password;}

   public String getSqlStatement() {return sqlStatement;}
   public void setSqlStatement(String sqlStatement) {this.sqlStatement = sqlStatement;}

   public boolean getIsMySql() {return isMySql;}
   public void setIsMySql(boolean isMySql) {this.isMySql = isMySql;}

   public boolean getIsSqlServer() {return isSqlServer;}
   public void setIsSqlServer(boolean isSqlServer) {this.isSqlServer = isSqlServer;}

   public boolean getIsJtds() {return isJtds;}
   public void setIsJtds(boolean isJtds) {this.isJtds = isJtds;}

   public Connection getConn() {return conn;}
   public void setConn(Connection conn) {this.conn = conn;}

   public Statement getStmt() {return stmt;}
   public void setStmt(Statement stmt) {this.stmt = stmt;}

   public ResultSet getRs() {return rs;}
   public void setRs(ResultSet rs) {this.rs = rs;}

}
