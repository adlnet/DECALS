package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.handler.ViewHandler;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.google.gwt.json.client.JSONObject;

/**
 * Helps with application administration and user management.
 * 
 * Was going to separate these into two separate helpers but there seems like a lot of functionality overlap.
 * 
 * @author Eduworks Corporation
 *
 */
public class AdminUserMgmtHelper {
   
   private ArrayList<AppUser> userList = new ArrayList<AppUser>();
   private ArrayList<AppUser> adminList = new ArrayList<AppUser>();
   private ArrayList<AppUser> userManagerList = new ArrayList<AppUser>();
   private ArrayList<ParadataPublicationInfo> paraPubInfoList = new ArrayList<ParadataPublicationInfo>();   
   
   private ViewHandler viewHandler;
   
   public AdminUserMgmtHelper() {}
   
   /**
    * Initializes the helper for administrator usage. (User List, Administrator List, and Paradata Publication Info List should be available.) 
    */
   public void initForAdministratorUsage(ViewHandler viewHandler) {
      this.viewHandler = viewHandler;
      buildParaPubInfoList(true);
   }
   
   /**
    * Initializes the helper for user management usage. (User List and User Manager List should be available.)
    */
   public void initForUserManagerUsage(ViewHandler viewHandler) {
      this.viewHandler = viewHandler;
      buildUserManagerList(true);
   }
   
   //builds the the paradata publication list from the paradata publication info get service returns
   private void buildParaPubInfoListFromReturn(JSONObject infoList) {
      paraPubInfoList.clear();
      for (JSONObject jo : DsUtil.parseJsonReturnList(infoList)) paraPubInfoList.add(new ParadataPublicationInfo(jo));
      Collections.sort(paraPubInfoList);
   }
   
   //Builds the paradata publication information list.
   //If buildOtherLists is true then the administrator and user lists are also built.
   //Done this way for synchronization 
   private void buildParaPubInfoList(final boolean buildOtherLists) {
      DsESBApi.decalsGetParadataPublicationInfoList(new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            buildParaPubInfoListFromReturn(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());            
            if (buildOtherLists) buildAdminList(buildOtherLists);
            else viewHandler.showContents();
         }
         @Override
         public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
      }); 
   }
   
   //Builds the administrator list.
   //If buildOtherLists is true then the user list is also built.
   //Done this way for synchronization
   private void buildAdminList(final boolean buildOtherLists) {
      DsESBApi.decalsGetAdminList(new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            DsUtil.buildUserTypeListFromReturn(adminList,result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());            
            if (buildOtherLists) buildUserList();
            else viewHandler.showContents();
         }
         @Override
         public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
      });
   }
   
   //Builds the user manager list.
   //If buildOtherLists is true then the user list is also built.
   //Done this way for synchronization
   private void buildUserManagerList(final boolean buildOtherLists) {
      DsESBApi.decalsGetUserManagerList(new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            DsUtil.buildUserTypeListFromReturn(userManagerList,result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());            
            if (buildOtherLists) buildUserList();
            else viewHandler.showContents();
         }
         @Override
         public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
      });
   }
   
   //builds the user list
   private void buildUserList() {
      DsESBApi.decalsGetUserList(new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            DsUtil.buildUserTypeListFromReturn(userList,result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
            viewHandler.showContents();
         }
         @Override
         public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
      });    
   }
   
   /**
    * Returns a list of non-administrator users.
    * 
    * @return Returns a list of non-administrator users.
    */
   public ArrayList<AppUser> getNonAdminUserList() {
      HashMap<String,AppUser> tempMap = new HashMap<String,AppUser>();
      for (AppUser u:userList) tempMap.put(u.getUserId(),u);
      for (AppUser a:adminList) tempMap.remove(a.getUserId());
      ArrayList<AppUser> retList = new ArrayList<AppUser>();
      for (String key:tempMap.keySet()) retList.add(tempMap.get(key));
      Collections.sort(retList);
      return retList;
   }
   
   /**
    * Returns a list of users excluding the one with the given ID.
    * 
    * @return Returns a list of users excluding the one with the given ID.
    */
   public ArrayList<AppUser> getExcludedUserList(String userId) {
      HashMap<String,AppUser> tempMap = new HashMap<String,AppUser>();
      for (AppUser u:userList) tempMap.put(u.getUserId(),u);
      ArrayList<AppUser> retList = new ArrayList<AppUser>();
      for (String key:tempMap.keySet()) if (!key.equalsIgnoreCase(userId)) retList.add(tempMap.get(key));
      Collections.sort(retList);
      return retList;
   }
   
   /**
    * Returns a list of non-user managers.
    * 
    * @return Returns a list of non-user managers.
    */
   public ArrayList<AppUser> getNonUserManagerUserList() {
      HashMap<String,AppUser> tempMap = new HashMap<String,AppUser>();
      for (AppUser u:userList) tempMap.put(u.getUserId(),u);
      for (AppUser a:userManagerList) tempMap.remove(a.getUserId());
      ArrayList<AppUser> retList = new ArrayList<AppUser>();
      for (String key:tempMap.keySet()) retList.add(tempMap.get(key));
      Collections.sort(retList);
      return retList;
   }
   
   /**
    * Refreshes the paradata publication information list based on the given info.
    * Prevents a trip to the server if the data is already on hand.
    * 
    * @param paraPubInfoList The paradata publication info list
    */
   public void refreshParaPubInfoList(JSONObject paraPubInfoList) {
      buildParaPubInfoListFromReturn(paraPubInfoList);
      viewHandler.showContents();
   }
   
   /**
    * Adds the given user to the administrator list.
    * Prevents a trip to the server if the data is already on hand.
    * 
    * @param newAdmin The user to add to the administrator list
    */
   public void addAdminToList(AppUser newAdmin) {
      adminList.add(newAdmin);
      Collections.sort(adminList);
      viewHandler.showContents();
   }
   
   /**
    * Refreshes the administrator list.
    */
   public void refreshAdminList() {buildAdminList(false);}
   
   /**
    * Adds the given user to the user manager list.
    * Prevents a trip to the server if the data is already on hand.
    * 
    * @param newUserManager The user to add to the administrator list
    */
   public void addUserManagerToList(AppUser newUserManager) {
      userManagerList.add(newUserManager);
      Collections.sort(userManagerList);
      viewHandler.showContents();
   }
   
   /**
    * Refreshes the user manager list.
    */
   public void refreshUserManagerList() {buildUserManagerList(false);}
   
   /**
    * Refreshes the paradata publication information list.
    */
   public void refreshParaPubInfoList() {buildParaPubInfoList(false);}
   
   /**
    * Refreshes the user list.
    */
   public void refreshUserList() {buildUserList();}
     
   /**
    * {@link AdminUserMgmtHelper#userList}
    */
   public ArrayList<AppUser> getUserList() {return userList;}
   
   /**
    * {@link AdminUserMgmtHelper#adminList}
    */
   public ArrayList<AppUser> getAdminList() {return adminList;}
   
   /**
    * {@link AdminUserMgmtHelper#userManagerList}
    */
   public ArrayList<AppUser> getUserManagerList() {return userManagerList;}
   
   /**
    * {@link AdminUserMgmtHelper#paraPubInfoList}
    */
   public ArrayList<ParadataPublicationInfo> getParaPubInfoList() {return paraPubInfoList;}
   
}
