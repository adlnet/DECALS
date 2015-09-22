package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;
import java.util.Collections;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.google.gwt.json.client.JSONObject;

/**
 * Manages a collection of groups.
 * 
 * @author Eduworks Corporation
 *
 */
public class GroupManager {
   
   private ArrayList<Group> groupList = new ArrayList<Group>();
   
   private String forUser = "";
   
   /**
    * Initializes the group list
    * 
    * @param groups The JSON group list
    */
   public void initGroupList(JSONObject groups) {parseGroups(groups);}
   
   //parse group list
   private void parseGroups(JSONObject groups) {
      groupList.clear();
      for (JSONObject jo : DsUtil.parseJsonReturnList(groups)) groupList.add(new Group(jo));
      Collections.sort(groupList);
   }
   
   /**
    * Adds the given group to the group list as the first element.
    * 
    * @param group The group to add;
    */
   public void addGroup(Group group) {groupList.add(0,group);}
   
   /**
    * Removes the group with the given ID from the group list.
    * 
    * @param groupId The ID of the group to remove from the group list.
    */
   public void removeGroup(String groupId) {
      if(groupId == null || groupId.trim().isEmpty()) return;
      for (Group g: groupList) {
         if (groupId.equalsIgnoreCase(g.getGroupId())) {
            groupList.remove(g);
            break;
         }
      }
   }
   
   //return the group with the given ID
   private Group getGroup(String groupId) {
      for (Group g: groupList) {
         if (groupId.equalsIgnoreCase(g.getGroupId())) return g;         
      }
      return null;
   }
   
   /**
    * Returns a list of users that is given user list - group users.
    * 
    * @param groupId The ID of the group to use.
    * @param userList The super set user list
    * 
    * @return Returns a list of users that is given user list - group users.
    */
   public ArrayList<AppUser> removeGroupUsersFromUserList(String groupId, ArrayList<AppUser> userList) {
      Group g = getGroup(groupId);     
      ArrayList<AppUser> tempList = new ArrayList<AppUser>();
      for (AppUser u: userList) {
         if (!g.hasUser(u.getUserId())) tempList.add(u);
      }
      return tempList;
   }
   
   /**
    * Removes the given user from the given group.
    * 
    * @param groupId The group ID
    * @param userId The user ID
    */
   public void removeGroupUser(String groupId, String userId) {
      getGroup(groupId).removeUser(userId);
   }
   
   /**
    * {@link GroupManager#groupList}
    */
   public ArrayList<Group> getGroupList() {return groupList;}
   public void setGroupList(ArrayList<Group> groupList) {this.groupList = groupList;}
   
   /**
    * {@link GroupManager#forUser}
    */
   public String getForUser() {return forUser;}
   public void setForUser(String forUser) {this.forUser = forUser;}
   
   /**
    * Returns the number of groups being managed.
    * 
    * @return Returns the number of groups being managed.
    */
   public long getNumberOfGroups() {return groupList.size();}
   
   /**
    * Generates and returns a unique group name if the given name is already in use.
    * 
    * ex: If 'Math Collection' exists, 'Math Collection (2)' is returned if it doesn't exist.
    * If 'Math Collection (2)' exists, 'Math Collection (3)' is returned if it doesn't exist.
    * etc.
    *
    * Recommended by client...
    * 
    * @param name The name to "uniqify"
    * @return Returns a unique group name if the given name is already in use.
    */
   public String generateUniqueGroupName(String name) {
      boolean found = false;
      for (Group g:groupList) {
         if (g.getName().equals(name)) {
            found = true;
            break;
         }
      }
      if (found) return generateUniqueGroupName(name,0);
      else return name;
   }
   
   // Generates and returns a unique group name if the given name is already in use.
   private String generateUniqueGroupName(String name, int idx) {
      int idx2 = idx + 1;      
      boolean found = false;
      for (Group g:groupList) {
         if (g.getName().equals(name + " (" + idx2  + ")")) {
            found = true;
            break;
         }
      }
      if (found) return generateUniqueGroupName(name,idx2);
      else return name + " (" + idx2  + ")";
   }

}
