package com.eduworks.decals.ui.client.model;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Represents an collection group
 * 
 * @author Eduworks Corporation
 *
 */
public class CollectionGroup extends Group {
   
   public static final String ACCESS_KEY = "access";
   
   private String access;
   private String collectionId;
   private String locatorKey;
   
public CollectionGroup() {}
   
   /**
    * Constructor that parses out a collection group formatted JSON object 
    * 
    * @param groupInfo JSON info info
    * @param collectionId The parent collection ID
    * @param locatorKey A temporary unique ID...used for the item sorting HTML
    */
   public CollectionGroup(JSONObject groupInfo, String collectionId, String locatorKey) {
      this.collectionId = collectionId;
      this.locatorKey = locatorKey;
      if (groupInfo.containsKey(NAME_KEY)) name = groupInfo.get(NAME_KEY).isString().stringValue();
      if (groupInfo.containsKey(ACCESS_KEY)) access = groupInfo.get(ACCESS_KEY).isString().stringValue();
      if (groupInfo.containsKey(GROUP_ID_KEY)) groupId = groupInfo.get(GROUP_ID_KEY).isString().stringValue();
      if (groupInfo.containsKey(TYPE_KEY)) groupType = groupInfo.get(TYPE_KEY).isString().stringValue();
   }
   
   /**
    * Builds and returns a JSON representation of the collection group.
    * 
    * @return Returns a JSON representation of the collection group.
    */
   public JSONObject toJson() {
      JSONObject jo = new JSONObject();
      jo.put(ACCESS_KEY,new JSONString(access.trim()));
      jo.put(NAME_KEY,new JSONString(name.trim()));
      jo.put(GROUP_ID_KEY,new JSONString(groupId.trim()));
      jo.put(TYPE_KEY,new JSONString(groupType.trim()));
      return jo;
   }
   
   /**
    * {@link CollectionGroup#access}
    */
   public String getAccess() {return access;}
   public void setAccess(String access) {this.access = access;}

   /**
    * {@link CollectionGroup#collectionId}
    */
   public String getCollectionId() {return collectionId;}
   public void setCollectionId(String collectionId) {this.collectionId = collectionId;}
   
   /**
    * {@link CollectionGroup#locatorKey}
    */
   public String getLocatorKey() {return locatorKey;}
   public void setLocatorKey(String locatorKey) {this.locatorKey = locatorKey;}

}
