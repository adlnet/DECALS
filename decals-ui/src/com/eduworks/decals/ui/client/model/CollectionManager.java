package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.google.gwt.json.client.JSONObject;

/**
 * Manages a group of collections.
 * 
 * @author Eduworks Corporation
 *
 */
public class CollectionManager {
   
   private ArrayList<Collection> collectionList = new ArrayList<Collection>();
   
   private static final int NEW_COLLECTION_ITEM_INDEX = 1;
   
   /**
    * Initializes the collection list
    * 
    * @param collections The JSON collection list
    */
   public void initCollectionList(JSONObject collections) {parseUserCollections(collections);}
   
   //parse collection list
   private void parseUserCollections(JSONObject collections) {
      collectionList.clear();
      for (JSONObject jo : DsUtil.parseJsonReturnList(collections)) collectionList.add(new Collection(jo));
      Collections.sort(collectionList);
   }
   
   /**
    * {@link CollectionManager#collectionList}
    */
   public ArrayList<Collection> getCollectionList() {return collectionList;}
   public void setCollectionList(ArrayList<Collection> collectionList) {this.collectionList = collectionList;}
   
   /**
    * Returns the number of collections being managed.
    * 
    * @return Returns the number of collections being managed.
    */
   public long getNumberOfCollections() {return collectionList.size();}
   
   /**
    * Adds the given collection to the collection list as the first element.
    * 
    * @param col The collection to add;
    */
   public void addCollection(Collection col) {
      collectionList.add(0,col);
      syncCollectionSessionPermissions(col,DsSession.getUser().getUserId(),DsSession.getUserGroups());
   }
   
   /**
    * Removes the collection with the given ID from the collection list.
    * 
    * @param colId The ID of the collection to remove from the collection list.
    */
   public void removeCollection(String colId) {
      if(colId == null || colId.trim().isEmpty()) return;
      for (Collection c: collectionList) {
         if (colId.equalsIgnoreCase(c.getCollectionId())) {
            collectionList.remove(c);
            break;
         }
      }
   }
   
   /**
    * Replaces the collection list collection with the given collection based on matching IDs 
    * 
    * @param col The collection to place on the list
    */
   public void replaceCollection(Collection col) {
      removeCollection(col.getCollectionId());
      addCollection(col);
      Collections.sort(collectionList);
      //syncCollectionSessionPermissions(col,DsSession.getUser().getUserId(),DsSession.getUserGroups());
   }   
   
   //return the collection with the given ID
   private Collection getCollection(String colId) {
      for (Collection c: collectionList) {
         if (colId.equalsIgnoreCase(c.getCollectionId())) return c;         
      }
      return null;
   }
   
   //creates a new collection item with the given information
   private CollectionItem createCollectionItem(String collectionId, String title, String url, String description) {
      CollectionItem ci = new CollectionItem();
      ci.setCollectionId(collectionId);
      ci.setItemIndex(NEW_COLLECTION_ITEM_INDEX);
      ci.setLocatorKey(DsUtil.generateId(Collection.ITEM_LOCATOR_KEY_PREFIX));
      ci.setResourceDescription(description);
      ci.setResourceUrl(url);
      ci.setResourceTitle(title); 
      return ci;
   }
   
   //creates a new collection user with the given information
   private CollectionUser createCollectionUser(String collectionId, String userId, String firstName, String lastName, String access) {
      CollectionUser cu = new CollectionUser();
      cu.setCollectionId(collectionId);
      cu.setLocatorKey(DsUtil.generateId(Collection.USER_LOCATOR_KEY_PREFIX));
      cu.setUserId(userId);
      cu.setEmailAddress(userId);
      cu.setFirstName(firstName);
      cu.setLastName(lastName);
      cu.setAccess(access);
      return cu;
   }
   
   /**
    * Adds a new collection item with the given information to a collection with the given ID.
    * 
    * Returns true if the item is added.  Returns false if the item already existed or the collection does not exist.
    * 
    * @param collectionId The ID of the collection to add the item
    * @param title The item title
    * @param url The item URL
    * @param description The item description
    * @return  Returns true if the item is added.  Returns false if the item already existed or the collection does not exist.
    */
   public boolean addCollectionItem(String collectionId, String title, String url, String description) {
      Collection c = getCollection(collectionId);
      if (c == null) return false;
      if (c.itemExists(url)) return false;          
      c.addCollectionItemToTop(createCollectionItem(collectionId,title,url,description));
      return true;
   }
   
   /**
    * Adds a new collection item with the given information to a collection with the given ID.
    * 
    * Overwrites an item with the same URL if it exists.
    * 
    * @param collectionId The ID of the collection to add the item
    * @param title The item title
    * @param url The item URL
    * @param description The item description
    */
   public void overwriteCollectionItem(String collectionId, String title, String url, String description) {
      Collection c = getCollection(collectionId);
      c.removeItem(url);
      c.addCollectionItemToTop(createCollectionItem(collectionId,title,url,description));
   }
   
   /**
    * Adds a new user with the given information to a collection with the given ID.
    * 
    * Overwrites an user with the same user ID if it exists.
    * 
    * @param collectionId The ID of the collection to add the user
    * @param userId The ID of the user
    * @param firstName The first name of the user
    * @param lastName The last name of the user
    * @param access  The collection access of the user
    */
   public void overwriteCollectionUser(String collectionId, String userId, String firstName, String lastName, String access) {
      Collection c = getCollection(collectionId);
      c.removeUser(userId);
      c.addCollectionUser(createCollectionUser(collectionId,userId,firstName,lastName,access));
   }
   
   //creates a new collection group with the given information
   private CollectionGroup createCollectionGroup(String collectionId, String groupId, String groupName, String groupType, String access) {
      CollectionGroup cg = new CollectionGroup();
      cg.setCollectionId(collectionId);
      cg.setLocatorKey(DsUtil.generateId(Collection.GROUP_LOCATOR_KEY_PREFIX));
      cg.setGroupId(groupId);
      cg.setName(groupName);
      cg.setGroupType(groupType);
      cg.setAccess(access);
      return cg;
   }
   
   /**
    * Adds a new group with the given information to a collection with the given ID.
    * 
    * Overwrites an group with the same user ID if it exists.
    * 
    * @param collectionId The ID of the collection to add the user
    * @param groupId The ID of the group
    * @param groupName The name of the group
    * @param groupType The type of the group
    * @param access  The collection access of the group
    */
   public void overwriteCollectionGroup(String collectionId, String groupId, String groupName, String groupType, String access) {
      Collection c = getCollection(collectionId);
      c.removeGroup(groupId); 
      c.addCollectionGroup(createCollectionGroup(collectionId,groupId,groupName,groupType,access));
   }
   
   /**
    * Returns a list of users that is given user list - collection users.
    * 
    * @param collectionId The ID of the collection to use.
    * @param userList The super set user list
    * 
    * @return Returns a list of users that is given user list - collection users.
    */
   public ArrayList<AppUser> removeCollectionUsersFromUserList(String collectionId, ArrayList<AppUser> userList) {
      Collection c = getCollection(collectionId);     
      ArrayList<AppUser> tempList = new ArrayList<AppUser>();
      for (AppUser u: userList) {
         if (!c.getUserAccessMap().containsKey(u.getUserId())) tempList.add(u);
      }
      return tempList;
   }

   /**
    * Returns a list of groups that is given group list - collection group.
    * 
    * @param collectionId The ID of the collection to use.
    * @param groupList The super set group list
    * 
    * @return Returns a list of groups that is given group list - collection group.
    */
   public ArrayList<Group> removeCollectionGroupsFromGroupList(String collectionId, ArrayList<Group> groupList) {
      Collection c = getCollection(collectionId);     
      ArrayList<Group> tempList = new ArrayList<Group>();
      for (Group g: groupList) {
         if (!c.getGroupAccessMap().containsKey(g.getGroupId())) tempList.add(g);
      }
      return tempList;
   }
   
   /**
    * Returns true if any collection is in a changed state.  False if they are not.
    * 
    * @return Returns true if any collection is in a changed state.  False if they are not.
    */
   public boolean anyCollectionInChangedState() {
      for (Collection c: collectionList) {
         if (c.getHasChanged()) return true;
      }
      return false;         
   }
   
   /**
    * Removes the given user from the given collection.
    * 
    * @param collectionId The collection ID
    * @param userId The user ID
    */
   public void removeCollectionUser(String collectionId, String userId) {
      getCollection(collectionId).removeUser(userId);
   }
   
   /**
    * Removes the given group from the given collection.
    * 
    * @param collectionId The collection ID
    * @param groupId The group ID
    */
   public void removeCollectionGroup(String collectionId, String groupId) {
      getCollection(collectionId).removeGroup(groupId);
   }
   
   /**
    * Removes the given group from all collections.
    * 
    * @param groupId The group ID
    */
   public void removeGroupFromAllCollections(String groupId) {
      for (Collection c: collectionList) c.removeGroup(groupId);
   }
   
   /**
    * Removes the given item from the given collection.
    * 
    * @param collectionId The collection ID
    * @param resourceUrl The item URL
    */
   public void removeCollectionItem(String collectionId, String resourceUrl) {
      getCollection(collectionId).removeItem(resourceUrl);
   }
   
   /**
    * Removes the given item from all collections
    * 
    * @param resourceUrl The item URL
    */
   public void removeItemFromAllCollections(String resourceUrl) {for (Collection c:collectionList) c.removeItem(resourceUrl);}
   
   /**
    * Removes the given user from all collections
    * 
    * @param userId The user ID
    */
   public void removeUserFromAllCollections(String userId) {for (Collection c:collectionList) c.removeUser(userId);}

   
   /**
    * Updates the collection with the given ID with the given information
    * 
    * @param collectionId  The ID of the collection to update
    * @param description The collection description
    * @param itemOrderMap The map containing collection item ordering
    * @param userAccessMap The map containing user access
    * @param groupAccessMap The map containing group access
    */
   public void updateCollection(String collectionId, String description, HashMap<String,Integer> itemOrderMap, HashMap<String,String> userAccessMap, 
         HashMap<String,String> groupAccessMap, String keywords, String coverage, String env, ArrayList<DarResourceObjective> newObjectives, 
         ArrayList<DarResourceObjective> oldObjectives) {
      Collection c = getCollection(collectionId);
      c.setDescription(description);
      c.updateItemIndices(itemOrderMap);
      c.updateUserAccesses(userAccessMap);      
      c.updateGroupAccesses(groupAccessMap);
      
      c.setKeywords(keywords);
      c.setCoverage(coverage);
      c.setEnvironment(env);
      c.setObjectives(newObjectives);
   }
   
   /**
    * Returns a list of modifiable collections for the session user.
    * 
    * BE SURE TO SYNC THE USER'S GROUPS AS WELL BEFORE CALLING THIS
    * 
    * @return  Returns a list of modifiable collections for the session user.
    */
   public ArrayList<Collection> getListOfSessionModifiableCollections() {
      ArrayList<Collection> ret = new ArrayList<Collection>();
      for (Collection c :collectionList) {
         if (c.sessionUserCanModify()) ret.add(c);
      }
      Collections.sort(ret);
      return ret;
   }
   
   /**
    * Returns the number of modifiable collections for the session user.
    * 
    * BE SURE TO SYCN THE USER'S GROUPS AS WELL BEFORE CALLING THIS
    * 
    * @return Returns the number of modifiable collections for the session user.
    */
   public long getNumberOfSessionModifiableCollections() {return getListOfSessionModifiableCollections().size();}
   
   
   /**
    * Generates and returns a unique collection name if the given name is already in use.
    * 
    * ex: If 'Math Collection' exists, 'Math Collection (2)' is returned if it doesn't exist.
    * If 'Math Collection (2)' exists, 'Math Collection (3)' is returned if it doesn't exist.
    * etc.
    *
    * Recommended by client...
    * 
    * @param name The name to "uniqify"
    * @return Returns a unique collection name if the given name is already in use.
    */
   public String generateUniqueCollectionName(String name) {
      boolean found = false;
      for (Collection c:collectionList) {
         if (c.getName().equals(name)) {
            found = true;
            break;
         }
      }
      if (found) return generateUniqueCollectionName(name,0);
      else return name;
   }
   
   // Generates and returns a unique collection name if the given name is already in use.
   private String generateUniqueCollectionName(String name, int idx) {
      int idx2 = idx + 1;      
      boolean found = false;
      for (Collection c:collectionList) {
         if (c.getName().equals(name + " (" + idx2  + ")")) {
            found = true;
            break;
         }
      }
      if (found) return generateUniqueCollectionName(name,idx2);
      else return name + " (" + idx2  + ")";
   }
   
   //synchronizes the given collection permissions with session user groups
   private void syncCollectionSessionPermissions(Collection c, String userId, ArrayList<Group> userGroupList) {
      boolean hasMod = false;
      hasMod = false;
      if (c.getUserAccessMap().containsKey(userId)) c.setSessionUserAccess(c.getUserAccessMap().get(userId));
      else {            
         for (Group g: userGroupList) {
            if (c.getGroupAccessMap().containsKey(g.getGroupId())) {
               if (CollectionAccess.MODIFY_ACCESS.equalsIgnoreCase(c.getGroupAccessMap().get(g.getGroupId()))) {
                  hasMod = true;
                  break;
               }
            }
         }
         if (hasMod) c.setSessionUserAccess(CollectionAccess.MODIFY_ACCESS);
         else c.setSessionUserAccess(CollectionAccess.VIEW_ACCESS);
      }
   }
   
   /**
    * Synchronizes all collection permissions with user groups    
    */
   public void syncAllSessionCollectionPermissions() {
      String userId = DsSession.getUser().getUserId();
      ArrayList<Group> userGroupList = DsSession.getUserGroups();      
      for (Collection c: collectionList) syncCollectionSessionPermissions(c,userId,userGroupList);
   }
   
}
