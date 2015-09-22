package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.google.gwt.dev.json.JsonArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Represents a collection
 * 
 * @author Eduworks Corporation
 *
 */
public class Collection implements Comparable<Collection>{
   
   private static final String COLLECTION_ID_KEY = "collectionId";
   private static final String DESC_KEY = "description";
   private static final String NAME_KEY = "name";
   private static final String CREATOR_KEY = "creator";   
   private static final String CREATE_DATE_KEY = "createdDate";
   private static final String UPDATE_DATE_KEY = "updatedDate";
   private static final String USERS_KEY = "users";
   private static final String ITEMS_KEY = "items";
   private static final String GROUPS_KEY = "groups";   
   
   private static final String METADATA_KEY = "metadata";
   
   private static final String KEYWORD_KEY = "keywords";
   private static final String OBJECTIVES_KEY = "objectives";
   private static final String OBJECTIVE_TITLE_KEY = "objTitle";
   private static final String OBJECTIVE_DESC_KEY = "objDesc";
   
   private static final String COVERAGE_KEY = "coverage";
   private static final String ENVIRONMENT_KEY = "environment";
   
   public static final String ITEM_LOCATOR_KEY_PREFIX = "colItem-";
   public static final String USER_LOCATOR_KEY_PREFIX = "colUser-";
   public static final String GROUP_LOCATOR_KEY_PREFIX = "colGroup-";
   
   private String collectionId;
   private String description;
   private String name;
   private String creator;
   private long updatedDateRaw;
   private String updatedDateStr;
   private long createDateRaw;
   private String createDateStr;
   
   private String sessionUserAccess = CollectionAccess.VIEW_ACCESS;
   
   private boolean hasChanged = false;
   private boolean metadataBeingChanged = false;
   
   private ArrayList<CollectionUser> collectionUsers = new ArrayList<CollectionUser>();
   private ArrayList<CollectionItem> collectionItems = new ArrayList<CollectionItem>();
   private ArrayList<CollectionGroup> collectionGroups = new ArrayList<CollectionGroup>();
   private HashMap<String,String> userAccessMap = new HashMap<String,String>();
   private HashMap<String,String> groupAccessMap = new HashMap<String,String>();
   
   private ArrayList<String> keywords = new ArrayList<String>();
   private ArrayList<DarResourceObjective> objectives = new ArrayList<DarResourceObjective>();
   private String coverage = new String();
   private String environment = new String();
   
   public Collection() {}
   
   /**
    * Constructor that parses out a collection formatted JSON object 
    * 
    * @param itemInfo
    */
   public Collection(JSONObject collectionInfo) {
      if (collectionInfo.containsKey(COLLECTION_ID_KEY)) collectionId = collectionInfo.get(COLLECTION_ID_KEY).isString().stringValue();
      if (collectionInfo.containsKey(DESC_KEY)) description = collectionInfo.get(DESC_KEY).isString().stringValue();
      if (collectionInfo.containsKey(NAME_KEY)) name = collectionInfo.get(NAME_KEY).isString().stringValue();
      if (collectionInfo.containsKey(CREATOR_KEY)) creator = collectionInfo.get(CREATOR_KEY).isString().stringValue();
      if (collectionInfo.containsKey(CREATE_DATE_KEY)) {
         createDateRaw = Long.parseLong(collectionInfo.get(CREATE_DATE_KEY).isString().stringValue());
         createDateStr = DsUtil.getDateFormatLongDate(Long.parseLong(collectionInfo.get(CREATE_DATE_KEY).isString().stringValue()));
      }
      if (collectionInfo.containsKey(UPDATE_DATE_KEY)) {
         updatedDateRaw = Long.parseLong(collectionInfo.get(UPDATE_DATE_KEY).isString().stringValue());
         updatedDateStr = DsUtil.getDateFormatLongDate(Long.parseLong(collectionInfo.get(UPDATE_DATE_KEY).isString().stringValue()));
      }
      if (collectionInfo.containsKey(USERS_KEY)) parseCollectionUsers(collectionInfo.get(USERS_KEY).isArray());
      if (collectionInfo.containsKey(ITEMS_KEY)) parseCollectionItems(collectionInfo.get(ITEMS_KEY).isArray());
      if (collectionInfo.containsKey(GROUPS_KEY)) parseCollectionGroups(collectionInfo.get(GROUPS_KEY).isArray());
      
      if (collectionInfo.containsKey(METADATA_KEY)) parseMetadata(collectionInfo.get(METADATA_KEY).isObject());
   }
   
   /**
    * Returns the number of items in the collection.
    * 
    * @return  Returns the number of items in the collection.
    */
   public long getNumberofItems() {return collectionItems.size();}
   
   /**
    * Returns the number of users in the collection.
    * 
    * @return  Returns the number of users in the collection.
    */
   public long getNumberofUsers() {return collectionUsers.size();}
   
   /**
    * Returns the number of groups in the collection.
    * 
    * @return  Returns the number of groups in the collection.
    */
   public long getNumberofGroups() {return collectionGroups.size();}
   
   /**
    * Returns true if the session user can modify the collection.  Returns false otherwise.
    * 
    * @return  Returns true if the session user can modify the collection.  Returns false otherwise.
    */
   public boolean sessionUserCanModify() {
      if (CollectionAccess.MODIFY_ACCESS.equalsIgnoreCase(sessionUserAccess)) return true;
      return false;
   }
   
   
   private void parseMetadata(JSONObject metadata) {
	   keywords.clear();
	   if(metadata.containsKey(KEYWORD_KEY)){
		   JSONArray metadataKeywords = metadata.get(KEYWORD_KEY).isArray();
		   
		   for(int i = 0; i < metadataKeywords.size(); i++){
			   keywords.add(metadataKeywords.get(i).isString().stringValue());
		   }
	   }
	   
	   objectives.clear();
	   if(metadata.containsKey(OBJECTIVES_KEY)){
		   JSONArray metadataObjectives = metadata.get(OBJECTIVES_KEY).isArray();
		   
		   for(int i = 0; i < metadataObjectives.size(); i++){
			   JSONObject metadataObj = metadataObjectives.get(i).isObject();
			   DarResourceObjective obj = new DarResourceObjective(metadataObj.get(OBJECTIVE_TITLE_KEY).isString().stringValue(),
	   																metadataObj.get(OBJECTIVE_DESC_KEY).isString().stringValue());
			   
			   objectives.add(obj);
		   }
	   }
	   
	   environment = "";
	   if(metadata.containsKey(ENVIRONMENT_KEY)){
		   environment = metadata.get(ENVIRONMENT_KEY).isString().stringValue();
	   }
	   
	   coverage = "";
	   if(metadata.containsKey(COVERAGE_KEY)){
		   coverage = metadata.get(COVERAGE_KEY).isString().stringValue();
	   }
	   
   }
   
   //parses the collection's items
   private void parseCollectionItems(JSONArray items) {
      collectionItems.clear();
      String locatorKey;
      for(int i=0;i<items.size();i++) {
         locatorKey = DsUtil.generateId(ITEM_LOCATOR_KEY_PREFIX);
         collectionItems.add(new CollectionItem(items.get(i).isObject(),collectionId,locatorKey));
      }
      Collections.sort(collectionItems);
   }
   
   //Updates the access map based on the collection users
   //This is just a faster way of determining user access
   private void updateUserAccessMap() {
      userAccessMap.clear();
      for (CollectionUser cu: collectionUsers) userAccessMap.put(cu.getUserId(),cu.getAccess());
   }
   
   //Updates the access map based on the collection groups
   //This is just a faster way of determining group access
   private void updateGroupAccessMap() {
      groupAccessMap.clear();
      for (CollectionGroup cg: collectionGroups) groupAccessMap.put(cg.getGroupId(),cg.getAccess());
   }
   
   //parses the collection's users
   private void parseCollectionUsers(JSONArray users) {
      collectionUsers.clear();
      String locatorKey;
      CollectionUser cu;
      for(int i=0;i<users.size();i++) {
         locatorKey = DsUtil.generateId(USER_LOCATOR_KEY_PREFIX);
         cu = new CollectionUser(users.get(i).isObject(),collectionId,locatorKey);
         collectionUsers.add(cu);      
      }
      Collections.sort(collectionUsers);
      updateUserAccessMap();
   }
   
   //parses the collection's groups
   private void parseCollectionGroups(JSONArray groups) {
      collectionGroups.clear();
      String locatorKey;
      CollectionGroup cg;
      for(int i=0;i<groups.size();i++) {
         locatorKey = DsUtil.generateId(GROUP_LOCATOR_KEY_PREFIX);
         cg = new CollectionGroup(groups.get(i).isObject(),collectionId,locatorKey);
         collectionGroups.add(cg);      
      }
      Collections.sort(collectionGroups);
      updateGroupAccessMap();
   }
   
   //return the collection user with the given ID
   private CollectionUser getCollectionUser(String colUserId) {
      for (CollectionUser cu: collectionUsers) {
         if (colUserId.equalsIgnoreCase(cu.getUserId())) return cu;         
      }
      return null;
   }
   
   //return the collection group with the given ID
   private CollectionGroup getCollectionGroup(String colGroupId) {
      for (CollectionGroup cg: collectionGroups) {
         if (colGroupId.equalsIgnoreCase(cg.getGroupId())) return cg;         
      }
      return null;
   }
   
   /**
    * Adds the given collection user to the collection user list and user access map.
    * 
    * @param cu The collection user to add
    */
   public void addCollectionUser(CollectionUser cu) {
      if (cu == null) return;
      CollectionUser u = getCollectionUser(cu.getUserId());
      if (u == null) collectionUsers.add(cu);
      else u.setAccess(cu.getAccess());      
      Collections.sort(collectionUsers);
      updateUserAccessMap();
   }
   
   /**
    * Adds the given collection group to the collection group list and group access map.
    * 
    * @param cg The collection group to add
    */
   public void addCollectionGroup(CollectionGroup cg) {
      if (cg == null) return;
      CollectionGroup g = getCollectionGroup(cg.getGroupId());
      if (g == null) collectionGroups.add(cg);
      else g.setAccess(cg.getAccess());      
      Collections.sort(collectionGroups);
      updateGroupAccessMap();
   }
   
   /**
    * Adds the given collection item to the collection item list.
    * 
    * @param ci The collection item to add
    */
   public void addCollectionItem(CollectionItem ci) {
      if (ci == null) return;
      collectionItems.add(ci);
      Collections.sort(collectionItems);
   }
   
   /**
    * Adds the given collection item to the top of the collection item list and does not sort afterwards.
    * 
    * @param ci The collection item to add
    */
   public void addCollectionItemToTop(CollectionItem ci) {
      if (ci == null) return;
      collectionItems.add(0,ci);      
   }
   
   /**
    * Returns a list of collection users excluding the one with the given ID.
    * 
    * @return Returns a list of collection users excluding the one with the given ID.
    */
   public ArrayList<CollectionUser> getExcludedCollectionUserList(String userId) {
      HashMap<String,CollectionUser> tempMap = new HashMap<String,CollectionUser>();
      for (CollectionUser u:collectionUsers) tempMap.put(u.getUserId(),u);
      ArrayList<CollectionUser> retList = new ArrayList<CollectionUser>();
      for (String key:tempMap.keySet()) if (!key.equalsIgnoreCase(userId)) retList.add(tempMap.get(key));
      Collections.sort(retList);
      return retList;
   }
   
   /**
    * Returns true if an item with the given URL already exists.  Returns false otherwise.
    * 
    * @param url The URL to search for.
    * @return  Returns true if an item with the given URL already exists.  Returns false otherwise.
    */
   public boolean itemExists(String url) {
      if (url == null || url.trim().isEmpty()) return false;
      for (CollectionItem ci:collectionItems) {
         if (url.equalsIgnoreCase(ci.getResourceUrl())) return true;
      }
      return false;
   }
   
   /**
    * Removes the item with the given URL.
    * 
    * @param url The URL of the item to remove.
    */
   public void removeItem(String url) {
      for (CollectionItem ci:collectionItems) {
         if (url.equalsIgnoreCase(ci.getResourceUrl())) {
            collectionItems.remove(ci);
            break;
         }
      }
   }
   
   /**
    * Removes the user with the given user ID.
    * 
    * @param userId The ID of the user to remove.
    */
   public void removeUser(String userId) {
      for (CollectionUser cu:collectionUsers) {
         if (userId.equalsIgnoreCase(cu.getUserId())) {
            collectionUsers.remove(cu);            
            break;
         }
      }      
      updateUserAccessMap();
   }
   
   /**
    * Removes the group with the given group ID.
    * 
    * @param group The ID of the group to remove.
    */
   public void removeGroup(String groupId) {
      for (CollectionGroup cg:collectionGroups) {
         if (groupId.equalsIgnoreCase(cg.getGroupId())) {
            collectionGroups.remove(cg);            
            break;
         }
      }      
      updateGroupAccessMap();
   }
   
   /**
    * Updates the orders/indices of collection items based on the values in the given map.
    * 
    * @param itemOrderMap The map containing item orders/indices 
    */
   public void updateItemIndices(HashMap<String,Integer> itemOrderMap) {
      for (CollectionItem ci:collectionItems) {
         ci.setItemIndex(itemOrderMap.get(ci.getLocatorKey()).intValue());
      }
   }
   
   /**
    * Updates the accesses of collection users based on the values in the given map.
    * 
    * @param userAccessMap The map containing user accesses
    */
   public void updateUserAccesses(HashMap<String,String> userAccessMap) {
      for (CollectionUser cu:collectionUsers) {
         cu.setAccess(userAccessMap.get(cu.getLocatorKey()));
      }
      updateUserAccessMap();
   }
   
   /**
    * Updates the accesses of collection groups based on the values in the given map.
    * 
    * @param groupAccessMap The map containing group accesses
    */
   public void updateGroupAccesses(HashMap<String,String> groupAccessMap) {
      for (CollectionGroup cg:collectionGroups) {
         cg.setAccess(groupAccessMap.get(cg.getLocatorKey()));
      }
      updateGroupAccessMap();
   }
   
   //builds the collection item JSON array
   private JSONArray buildItemArray() {
      JSONArray ja = new JSONArray();
      int idx = 0;
      for (CollectionItem ci: collectionItems) {
         ja.set(idx,ci.toJson());
         idx++;
      }
      return ja;
   }
   
   //builds the collection user JSON array
   private JSONArray buildUserArray() {
      JSONArray ja = new JSONArray();
      int idx = 0;
      for (CollectionUser cu: collectionUsers) {
         ja.set(idx,cu.toJson());
         idx++;
      }
      return ja;
   }
   
   //builds the collection group JSON array
   private JSONArray buildGroupArray() {
      JSONArray ja = new JSONArray();
      int idx = 0;
      for (CollectionGroup cg: collectionGroups) {
         ja.set(idx,cg.toJson());
         idx++;
      }
      return ja;
   }
   
   /**
    * Builds and returns a JSON representation of the collection
    * 
    * @return Returns a JSON representation of the collection
    */
   public JSONObject toJson() {
      JSONObject jo = new JSONObject();
      jo.put(COLLECTION_ID_KEY, new JSONString(collectionId));
      if (description != null) {
         jo.put(DESC_KEY, new JSONString(description.trim()));
      }
      else {
         jo.put(DESC_KEY, new JSONString(""));
      }
      
      JSONArray arr = new JSONArray();
      if(keywords.size() != 0){
    	  for(int i = 0; i < keywords.size(); i++){
    		  String word = keywords.get(i);
    		  arr.set(i, new JSONString(word));
    	  }
      }
      jo.put(KEYWORD_KEY, arr);
      
      
      arr = new JSONArray();
      if(objectives.size() != 0){
    	  for(int i = 0; i < objectives.size(); i++){
    		  DarResourceObjective obj = objectives.get(i);
    		  JSONObject jobj = new JSONObject();
    		  jobj.put(OBJECTIVE_TITLE_KEY, new JSONString(obj.getTitle()));
    		  jobj.put(OBJECTIVE_DESC_KEY, new JSONString(obj.getDescription()));
    		  arr.set(i, jobj);
    	  }
      }
      jo.put(OBJECTIVES_KEY, arr);
      
      jo.put(COVERAGE_KEY, new JSONString(coverage));
      jo.put(ENVIRONMENT_KEY, new JSONString(environment));
      
      jo.put(NAME_KEY, new JSONString(name.trim()));
      jo.put(ITEMS_KEY, buildItemArray());
      jo.put(USERS_KEY,buildUserArray());
      jo.put(GROUPS_KEY, buildGroupArray());
      return jo;
   }

   /**
    * {@link Collection#collectionId}
    */
   public String getCollectionId() {return collectionId;}
   public void setCollectionId(String collectionId) {this.collectionId = collectionId;}
   
   /**
    * {@link Collection#description}
    */
   public String getDescription() {return description;}
   public void setDescription(String description) {this.description = description;}
   
   /**
    * {@link Collection#name}
    */
   public String getName() {return name;}
   public void setName(String name) {this.name = name;}
   
   /**
    * {@link Collection#creator}
    */
   public String getCreator() {return creator;}
   public void setCreator(String creator) {this.creator = creator;}

   /**
    * {@link Collection#updatedDateStr}
    */
   public String getUpdatedDateStr() {return updatedDateStr;}
   public void setUpdatedDateStr(String updatedDateStr) {this.updatedDateStr = updatedDateStr;}

   /**
    * {@link Collection#createDateStr}
    */
   public String getCreateDateStr() {return createDateStr;}
   public void setCreateDateStr(String createDateStr) {this.createDateStr = createDateStr;}
   
   /**
    * {@link Collection#collectionGroups}
    */
   public ArrayList<CollectionGroup> getCollectionGroups() {return collectionGroups;}
   public void setCollectionGroups(ArrayList<CollectionGroup> collectionGroups) {this.collectionGroups = collectionGroups;}

   /**
    * {@link Collection#collectionUsers}
    */
   public ArrayList<CollectionUser> getCollectionUsers() {return collectionUsers;}
   public void setCollectionUsers(ArrayList<CollectionUser> collectionUsers) {this.collectionUsers = collectionUsers;}

   /**
    * {@link Collection#collectionItems}
    */
   public ArrayList<CollectionItem> getCollectionItems() {return collectionItems;}
   public void setCollectionItems(ArrayList<CollectionItem> collectionItems) {this.collectionItems = collectionItems;}
   
   /**
    * {@link Collection#updatedDateRaw}
    */
   public long getUpdatedDateRaw() {return updatedDateRaw;}
   public void setUpdatedDateRaw(long updatedDateRaw) {this.updatedDateRaw = updatedDateRaw;}

   /**
    * {@link Collection#createDateRaw}
    */
   public long getCreateDateRaw() {return createDateRaw;}
   public void setCreateDateRaw(long createDateRaw) {this.createDateRaw = createDateRaw;}
   
   /**
    * {@link Collection#userAccessMap}
    */
   public HashMap<String, String> getUserAccessMap() {return userAccessMap;}
   public void setUserAccessMap(HashMap<String, String> accessMap) {this.userAccessMap = accessMap;}
   
   /**
    * {@link Collection#groupAccessMap}
    */
   public HashMap<String, String> getGroupAccessMap() {return groupAccessMap;}
   public void setGroupAccessMap(HashMap<String, String> groupAccessMap) {this.groupAccessMap = groupAccessMap;}
   
   /**
    * {@link Collection#hasChanged}
    */
   public boolean getHasChanged() {return hasChanged;}
   public void setHasChanged(boolean hasChanged) {this.hasChanged = hasChanged;}
   
   /**
    * {@link Collection#hasChanged}
    */
   public boolean isMetadataBeingChanged() {return metadataBeingChanged;}
   public void setMetadataBeingChanged(boolean metadataBeingChanged) {this.metadataBeingChanged = metadataBeingChanged;}
   
   
   public String[] getKeywords() {return keywords.toArray(new String[keywords.size()]);}
   public void setKeywords(String keywords){
	   this.keywords.clear();
	   String[] keys = keywords.split(",");
	   
	   for(String key : keys){
		   this.keywords.add(key.trim());
	   }
   }
   
   public ArrayList<DarResourceObjective> getObjectives() {return objectives;}
   public void setObjectives(ArrayList<DarResourceObjective> objectives){
	   this.objectives.clear();
	   this.objectives.addAll(objectives);
   }
   public void removeObjectives(DarResourceObjective objective){
	   objectives.remove(objective);
   }
   
   public String getCoverage() { return coverage; }
   public void setCoverage(String cov){
	   coverage = cov;
   }
   
   public String getEnvironment(){ return environment; }
   public void setEnvironment(String env){
	   environment = env;
   }
   
   /**
    * {@link Collection#sessionUserAccess}
    */
   public String getSessionUserAccess() {return sessionUserAccess;}
   public void setSessionUserAccess(String sessionUserAccess) {this.sessionUserAccess = sessionUserAccess;}
      
   @Override
   public int compareTo(Collection o) {
      if (this.updatedDateRaw == o.getUpdatedDateRaw()) return 0;
      else if (this.updatedDateRaw < o.getUpdatedDateRaw()) return 1;
      else return -1;
   }
   
}
