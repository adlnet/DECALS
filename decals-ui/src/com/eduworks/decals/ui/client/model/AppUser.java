package com.eduworks.decals.ui.client.model;

import java.util.HashMap;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Represents an application user
 * 
 * @author Eduworks Corporation
 *
 */
public class AppUser implements Comparable<AppUser>{
   
   public static final String FIRST_NAME_KEY = "firstName";
   public static final String LAST_NAME_KEY = "lastName";
   public static final String ROLES_KEY = "roles";
   public static final String EMAIL_KEY = "email";
   public static final String LAST_LOGIN_KEY = "lastLogin";
   public static final String CREATE_DATE_KEY = "dateCreated";
   public static final String USERID_KEY = "userId";
   
   public static final String COMPETENCY_SESSION_ID = "competencySessionId";
   
   public static final String NEVER_LOGGED_IN_STRING = "never";
   
   protected String userId;
   protected String emailAddress;
   protected String firstName;
   protected String lastName;
   private String createDateStr;
   private String lastLoginDateStr;
   private HashMap<String,String> roles = new HashMap<String,String>();
     
   private String competencySessionId;
   
   public AppUser() {}
   
   /**
    * Constructor that parses out a /decalsGetUser formatted JSON object 
    * 
    * @param userInfo
    */
   public AppUser(JSONObject userInfo) {
      clearRoles();
      if (userInfo.containsKey(FIRST_NAME_KEY)) firstName = userInfo.get(FIRST_NAME_KEY).isString().stringValue();
      if (userInfo.containsKey(LAST_NAME_KEY)) lastName = userInfo.get(LAST_NAME_KEY).isString().stringValue();
      if (userInfo.containsKey(EMAIL_KEY)) {
         userId = userInfo.get(EMAIL_KEY).isString().stringValue();
         emailAddress = userInfo.get(EMAIL_KEY).isString().stringValue();
      }
      if (userInfo.containsKey(USERID_KEY)) {
         userId = userInfo.get(USERID_KEY).isString().stringValue();
         emailAddress = userInfo.get(USERID_KEY).isString().stringValue();
      }
      if (userInfo.containsKey(CREATE_DATE_KEY)) createDateStr = DsUtil.getDateFormatLongDate(Long.parseLong(userInfo.get(CREATE_DATE_KEY).isString().stringValue()));
      if (userInfo.containsKey(ROLES_KEY)) parseRoles(userInfo.get(ROLES_KEY).isArray());
      
      if(userInfo.containsKey(COMPETENCY_SESSION_ID)) competencySessionId = userInfo.get(COMPETENCY_SESSION_ID).isString().stringValue();
      
      parseLastLoginDate(userInfo);      
   }
   
   
   //parses the last login date into an appropriate value
   private void parseLastLoginDate(JSONObject userInfo) {
      if (userInfo.containsKey(LAST_LOGIN_KEY)) {
         if (Long.parseLong(userInfo.get(LAST_LOGIN_KEY).isString().stringValue()) == 0) {
            lastLoginDateStr = NEVER_LOGGED_IN_STRING;
         }
         else {
            try {
               lastLoginDateStr = DsUtil.getDateFormatLongDate(Long.parseLong(userInfo.get(LAST_LOGIN_KEY).isString().stringValue()));
            }
            catch (Exception e) {
               lastLoginDateStr = NEVER_LOGGED_IN_STRING;
            }
         }                           
      }
      else {
         lastLoginDateStr = NEVER_LOGGED_IN_STRING;
      }
   }
   
   //parse the user roles
   private void parseRoles(JSONArray ja) {
      clearRoles();
      if (ja == null || ja.size() == 0) return;      
      for (String s:DsUtil.buildStringListFromJsonArray(ja)) roles.put(s.toLowerCase(),null);
   }
   
   /**
    * {@link AppUser#userId}
    */
   public String getUserId() {return userId;}
   public void setUserId(String userId) {this.userId = userId;}

   /**
    * {@link AppUser#emailAddress}
    */
   public String getEmailAddress() {return emailAddress;}
   public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}

   /**
    * {@link AppUser#firstName}
    */
   public String getFirstName() {return firstName;}
   public void setFirstName(String firstName) {this.firstName = firstName;}

   /**
    * {@link AppUser#lastName}
    */
   public String getLastName() {return lastName;}
   public void setLastName(String lastName) {this.lastName = lastName;}
   
   /**
    * {@link AppUser#roles}
    */
   public HashMap<String, String> getRoles() {return roles;}
   public void setRoles(HashMap<String, String> roles) {this.roles = roles;}
   
   /**
    * {@link AppUser#createDateStr}
    */
   public String getCreateDateStr() {return createDateStr;}
   public void setCreateDateStr(String createDateStr) {this.createDateStr = createDateStr;}

   /**
    * {@link AppUser#lastLoginDateStr}
    */
   public String getLastLoginDateStr() {return lastLoginDateStr;}
   public void setLastLoginDateStr(String lastLoginDateStr) {this.lastLoginDateStr = lastLoginDateStr;}
   
   /**
    * {@link AppUser#lastLoginDateStr}
    */
   public String getCompetencySessionId() {return competencySessionId;}
   public void setCompetencySessionId(String competencySessionId) {this.competencySessionId = competencySessionId;}
   
   /**
    * Returns the full name in the format <last name>, <first name>
    * 
    * @return  Returns the full name in the format <last name>, <first name>
    */
   public String getFullName() {return lastName + ", " + firstName;}
      
   /**
    * Clear all user roles for the session.
    */
   public void clearRoles() {roles.clear();}
   
   /**
    * Returns true if the user has the given role.  Returns false otherwise.
    * 
    * @param role The role to check
    * @return Returns true if the user has the given role.  Returns false otherwise.
    */
   public boolean hasRole(String role) {return roles.containsKey(role.toLowerCase());}

   @Override
   public int compareTo(AppUser o) {return this.getFullName().compareTo(o.getFullName());}   
  
}
