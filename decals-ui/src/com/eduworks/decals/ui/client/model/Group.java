package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;
import java.util.Collections;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Represents a group
 * 
 * @author Eduworks Corporation
 *
 */
public class Group implements Comparable<Group> {
   
   protected static final String GROUP_ID_KEY = "groupId";
   protected static final String NAME_KEY = "name";
   protected static final String CREATOR_KEY = "creator";   
   protected static final String CREATE_DATE_KEY = "createdDate";
   protected static final String UPDATE_DATE_KEY = "updatedDate";
   protected static final String USERS_KEY = "users";
   protected static final String TYPE_KEY = "type";
   
   protected String groupId;
   protected String name;
   protected String creator;
   protected long updatedDateRaw;
   protected String updatedDateStr;
   protected long createDateRaw;
   protected String createDateStr;
   protected String groupType;
   
   private ArrayList<AppUser> groupUsers = new ArrayList<AppUser>();
   
   public Group() {}
   
   /**
    * Constructor that parses out a collection formatted JSON object 
    * 
    * @param itemInfo
    */
   public Group(JSONObject groupInfo) {
      if (groupInfo.containsKey(GROUP_ID_KEY)) groupId = groupInfo.get(GROUP_ID_KEY).isString().stringValue();
      if (groupInfo.containsKey(NAME_KEY)) name = groupInfo.get(NAME_KEY).isString().stringValue();
      if (groupInfo.containsKey(CREATOR_KEY)) creator = groupInfo.get(CREATOR_KEY).isString().stringValue();
      if (groupInfo.containsKey(TYPE_KEY)) groupType = groupInfo.get(TYPE_KEY).isString().stringValue();
      if (groupInfo.containsKey(CREATE_DATE_KEY)) {
         createDateRaw = Long.parseLong(groupInfo.get(CREATE_DATE_KEY).isString().stringValue());
         createDateStr = DsUtil.getDateFormatLongDate(Long.parseLong(groupInfo.get(CREATE_DATE_KEY).isString().stringValue()));
      }
      if (groupInfo.containsKey(UPDATE_DATE_KEY)) {
         updatedDateRaw = Long.parseLong(groupInfo.get(UPDATE_DATE_KEY).isString().stringValue());
         updatedDateStr = DsUtil.getDateFormatLongDate(Long.parseLong(groupInfo.get(UPDATE_DATE_KEY).isString().stringValue()));
      }
      if (groupInfo.containsKey(USERS_KEY)) parseGroupUsers(groupInfo.get(USERS_KEY).isArray());      
   }
   
   //parses the groups users
   private void parseGroupUsers(JSONArray users) {
      groupUsers.clear();
      AppUser au;
      for(int i=0;i<users.size();i++) {
         au = new AppUser(users.get(i).isObject());
         groupUsers.add(au);      
      }
      Collections.sort(groupUsers);
   }
   
   /**
    * Removes the user with the given user ID.
    * 
    * @param userId The ID of the user to remove.
    */
   public void removeUser(String userId) {
      for (AppUser au:groupUsers) {
         if (userId.equalsIgnoreCase(au.getUserId())) {
            groupUsers.remove(au);            
            break;
         }
      }
   }
   
   /**
    * Returns true if the group contains the given user.  Returns false otherwise.
    * 
    * @param userId The user ID to check.
    * @return Returns true if the group contains the given user.  Returns false otherwise.
    */
   public boolean hasUser(String userId) {
      if (userId == null || userId.trim().isEmpty()) return false;
      for (AppUser au: groupUsers) {
         if (userId.equalsIgnoreCase(au.getUserId())) return true;
      }
      return false;
   }
   
   /**
    * Returns the number of users in the group.
    * 
    * @return  Returns the number of users in the group.
    */
   public long getNumberofUsers() {return groupUsers.size();}
   
   /**
    * {@link Group#groupId}
    */
   public String getGroupId() {return groupId;}
   public void setGroupId(String groupId) {this.groupId = groupId;}
   
   /**
    * {@link Group#name}
    */
   public String getName() {return name;}
   public void setName(String name) {this.name = name;}
   
   /**
    * {@link Group#creator}
    */
   public String getCreator() {return creator;}
   public void setCreator(String creator) {this.creator = creator;}

   /**
    * {@link Group#updatedDateStr}
    */
   public String getUpdatedDateStr() {return updatedDateStr;}
   public void setUpdatedDateStr(String updatedDateStr) {this.updatedDateStr = updatedDateStr;}

   /**
    * {@link Group#createDateStr}
    */
   public String getCreateDateStr() {return createDateStr;}
   public void setCreateDateStr(String createDateStr) {this.createDateStr = createDateStr;}

   /**
    * {@link Group#groupUsers}
    */
   public ArrayList<AppUser> getGroupUsers() {return groupUsers;}
   public void setGroupUsers(ArrayList<AppUser> groupUsers) {this.groupUsers = groupUsers;}

   /**
    * {@link Group#updatedDateRaw}
    */
   public long getUpdatedDateRaw() {return updatedDateRaw;}
   public void setUpdatedDateRaw(long updatedDateRaw) {this.updatedDateRaw = updatedDateRaw;}

   /**
    * {@link Group#createDateRaw}
    */
   public long getCreateDateRaw() {return createDateRaw;}
   public void setCreateDateRaw(long createDateRaw) {this.createDateRaw = createDateRaw;}
   
   /**
    * {@link Group#groupType}
    */
   public String getGroupType() {return groupType;}
   public void setGroupType(String groupType) {this.groupType = groupType;}
        
   @Override
   public int compareTo(Group o) {
      if (this.updatedDateRaw == o.getUpdatedDateRaw()) return 0;
      else if (this.updatedDateRaw < o.getUpdatedDateRaw()) return 1;
      else return -1;
   }


}
