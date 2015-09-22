package com.eduworks.decals.ui.client.model;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Represents an collection user
 * 
 * @author Eduworks Corporation
 *
 */
public class CollectionUser extends AppUser {
   
   public static final String ACCESS_KEY = "access";
   
   private String access;
   private String collectionId;
   private String locatorKey;
   
   public CollectionUser() {}
   
   /**
    * Constructor that parses out a collection user formatted JSON object 
    * 
    * @param userInfo JSON user info
    * @param collectionId The parent collection ID
    * @param locatorKey A temporary unique ID...used for the item sorting HTML
    */
   public CollectionUser(JSONObject userInfo, String collectionId, String locatorKey) {
      this.collectionId = collectionId;
      this.locatorKey = locatorKey;
      if (userInfo.containsKey(FIRST_NAME_KEY)) firstName = userInfo.get(FIRST_NAME_KEY).isString().stringValue();
      if (userInfo.containsKey(LAST_NAME_KEY)) lastName = userInfo.get(LAST_NAME_KEY).isString().stringValue();
      if (userInfo.containsKey(ACCESS_KEY)) access = userInfo.get(ACCESS_KEY).isString().stringValue();
      if (userInfo.containsKey(USERID_KEY)) {
         userId = userInfo.get(USERID_KEY).isString().stringValue();
         emailAddress = userInfo.get(USERID_KEY).isString().stringValue();
      }     
   }
   
   /**
    * Builds and returns a JSON representation of the collection user.
    * 
    * @return Returns a JSON representation of the collection user.
    */
   public JSONObject toJson() {
      JSONObject jo = new JSONObject();
      jo.put(ACCESS_KEY,new JSONString(access.trim()));
      jo.put(FIRST_NAME_KEY,new JSONString(firstName.trim()));
      jo.put(LAST_NAME_KEY,new JSONString(lastName.trim()));
      jo.put(USERID_KEY,new JSONString(userId.trim()));
      return jo;
   }
   
   /**
    * {@link CollectionUser#access}
    */
   public String getAccess() {return access;}
   public void setAccess(String access) {this.access = access;}

   /**
    * {@link CollectionUser#collectionId}
    */
   public String getCollectionId() {return collectionId;}
   public void setCollectionId(String collectionId) {this.collectionId = collectionId;}
   
   /**
    * {@link CollectionUser#locatorKey}
    */
   public String getLocatorKey() {return locatorKey;}
   public void setLocatorKey(String locatorKey) {this.locatorKey = locatorKey;}

}
