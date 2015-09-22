package com.eduworks.decals.ui.client.api;

import java.util.ArrayList;
import java.util.Vector;

import com.eduworks.decals.ui.client.Decals_ui;
import com.eduworks.decals.ui.client.DsScreenDispatch;
import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.model.DarResourceMetadata;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.GRADE_LEVEL;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.LANGUAGE;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.RESOURCE_TYPE;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.MultipartPost;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenDispatch;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * DECALS specific API for levr scripts/ESB.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsESBApi extends ESBApi {
   
   public static final String MSG_KEY = "msg";
   public static final String TRUE_MSG = "\"true\"";
   public static final String INVALID_USERNAME_MSG = "\"Invalid username\"";
   public static final String FILE_UPLOAD_SERVICE = "decalsFileUpload";
      
   public static final String SESSION_ID_KEY = "sessionId";
   public static final String FILE_ID_KEY = "fileId";
   public static final String USER_ID_KEY = "userId";
   public static final String RESOURCE_URL_KEY = "resourceUrl";
   public static final String RESOURCE_URLS_KEY = "resourceUrls";
   public static final String ROLE_ID_KEY = "roleId";
   public static final String COLLECTION_ID_KEY = "collectionId";
   public static final String COLLECTION_NAME_KEY = "collectionName";
   public static final String COLLECTION_DESC_KEY = "collectionDescription";  
   private static final String TO_USER_ID_KEY = "toUserId";
   private static final String NEW_PASSWORD_KEY = "newPassword";
   private static final String SEARCH_TERM_KEY = "searchTerm";
   private static final String ITEMS_PER_PAGE_KEY = "itemsPerPage";
   private static final String PAGE_KEY = "page";
   private static final String ASSIGN_ID_KEY = "assignmentId";
   private static final String MESSAGE_KEY = "message";
   private static final String TITLE_KEY = "title";
   private static final String WORD_KEY = "word";
   private static final String LEVELS_KEY = "levels";
   private static final String TEXT_KEY = "text";
   private static final String TERMS_KEY = "terms";
   private static final String USE_MMA_KEY = "useMustMatchAll";
   private static final String QUERY_KEY = "query";
   private static final String ROWS_KEY = "rows";
   private static final String RETURN_FLDS_KEY = "returnFields";
   private static final String ID_SORT_KEY = "idSort";
   private static final String USE_CURSOR_KEY = "useCursor";
   private static final String START_KEY = "start";
   private static final String SORT_FLD_KEY = "sortField";
   private static final String SORT_ORDER_KEY = "sortOrder";
   private static final String EMAIL_KEY = "email";
   private static final String PASSWORD_KEY = "password";
   private static final String FIRST_NAME_KEY = "firstName";
   private static final String LAST_NAME_KEY = "lastName";
   private static final String PAGE_TITLE_KEY = "pageTitle";
   private static final String URL_KEY = "url";
   private static final String LR_DOC_ID_KEY = "lrDocId";
   private static final String RATING_KEY = "rating";
   private static final String COMMENT_ID_KEY = "commentId";
   private static final String COMMENT_KEY = "comment";
   private static final String ACCESS_KEY = "access";   
   private static final String RESOURCE_TITLE_KEY = "resourceTitle";
   private static final String RESOURCE_DESC_KEY = "resourceDescription";
   private static final String ITEM_IDX_KEY = "itemIndex";  
   private static final String COLLECTION_DATA_KEY = "collectionData";
   private static final String GROUP_ID_KEY = "groupId";
   private static final String USERS_KEY = "users";
   private static final String GROUP_NAME_KEY = "groupName";
   private static final String GROUP_TYPE_KEY = "groupType";
            
   public static final String DECALS_FORM_DATA_NAME = "decalsData";
   public static final String FILE_METADATA_FORM_DATA_NAME = "fileMetadata";
   public static final String LR_PUBLISH_FORM_DATA_NAME = "lrData";
   
   public static final String DOWNLOAD_FILE_URL_PREFIX_KEY = "fileDownloadUrl";
   public static final String LR_PUBLISH_NODE_KEY = "lrPublishNode";   
   public static final String LR_PUBLISH_SUBMITTER_KEY = "lrPublishSubmitter";
   public static final String LR_PUBLISH_CURATOR_KEY = "lrPublishCurator";
   public static final String LR_PUBLISH_FROM_NODE_KEY = "lrPublishFromNode";
   public static final String LR_PUBLISH_PD_ACTOR_KEY = "lrPublishParadataActor";
   
   public static final String COMPETENCY_SESSION_ID_KEY = "competencySessionId";

   
   //Turns an array list of strings into a levr recognized array of strings
   private static String buildQueryTerms(ArrayList<String> queryTerms) {
      StringBuffer sb = new StringBuffer();
      sb.append("[");
      for (int i=0;i<queryTerms.size();i++) {
         sb.append(queryTerms.get(i));
         if ((i + 1) !=queryTerms.size()) sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
   }
   
   /**
    * Creates a group with the given information
    * 
    * @param groupName The name of the group
    * @param groupType The type of the group (public|private)
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsCreateGroup(String groupName, String groupType, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(GROUP_NAME_KEY, groupName);
      jo.put(GROUP_TYPE_KEY, groupType);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsCreateGroup"),mp,false,callback);
   }   
   
   /**
    * Deletes the given group
    * 
    * @param groupId The ID of the group
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsDeleteGroup(String groupId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(GROUP_ID_KEY, groupId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsDeleteGroup"),mp,false,callback);
   }   
   
   /**
    * Adds the given users to the given group
    * 
    * @param groupId The ID of the group
    * @param users The array of users
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsAddGroupUsers(String groupId, JSONArray users, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(GROUP_ID_KEY, groupId);
      jo.put(USERS_KEY, users);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddGroupUsers"),mp,false,callback);
   }
   
   /**
    * Removes the given user from the given group
    * 
    * @param groupId The ID of the group
    * @param userId The ID of the user
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsRemoveGroupUser(String groupId, String userId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(GROUP_ID_KEY, groupId);
      jo.put(USER_ID_KEY, userId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsRemoveGroupUser"),mp,false,callback);
   }
   
   /**
    * Returns the list of public groups
    * 
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetPublicGroups(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetPublicGroups"),mp,false,callback);
   }
   
   /**
    * Returns the list of the session user's private groups
    * 
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetUserPrivateGroups(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetUserPrivateGroups"),mp,false,callback);
   }
   
   /**
    * Returns the list of groups the given user belongs to
    * 
    * @param userId The user ID
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetMemberGroupsForUser(String userId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, userId);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetUserMemberGroups"),mp,false,callback);
   }
   
   /**
    * Returns a user's collections
    * 
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetUserCollections(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, username);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetCollectionsForUser"),mp,false,callback);
   }
   
   /**
    * Updates a collection with the given data
    * 
    * @param collectionId The ID of the collection
    * @param collectionData The updated collection information
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsUpdateCollection(String collectionId, JSONObject collectionData, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_ID_KEY, collectionId);
      jo.put(COLLECTION_DATA_KEY, collectionData);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsUpdateCollection"),mp,false,callback);
   }
   
   /**
    * Returns the collection with the given ID
    * 
    * @param collectionId The ID of the collection
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetCollection(String collectionId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_ID_KEY, collectionId);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetCollection"),mp,false,callback);
   }
   
   /**
    * Retrieves the application administrator list
    *  
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetAdminList(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetAdminList"),mp,false,callback);
   }
   
   /**
    * Creates a new collection
    * 
    * @param collectionName The name of the new collection
    * @param collectionDescription The description of the new collection
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsCreateCollection(String collectionName, String collectionDescription, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_NAME_KEY, collectionName);
      jo.put(COLLECTION_DESC_KEY, collectionDescription);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsCreateCollection"),mp,false,callback);
   }
   
   /**
    * Deletes a collection
    * 
    * @param collectionId The ID of the collection to delete
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsDeleteCollection(String collectionId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_ID_KEY, collectionId);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsDeleteCollection"),mp,false,callback);
   }
   
   /**
    * Adds a user to a collection.
    * Can be used to modify a user's access to a collection
    * 
    * @param collectionId The ID of the collection
    * @param userId The ID of the user
    * @param access The access for the user to the collection (view/modify)
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsAddCollectionUser(String collectionId, String userId, String access, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_ID_KEY, collectionId);
      jo.put(USER_ID_KEY, userId);
      jo.put(ACCESS_KEY, access);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddCollectionUser"),mp,false,callback);
   }
   
   /**
    * Removes a user from a collection.
    * 
    * @param collectionId The ID of the collection
    * @param userId The ID of the user
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsRemoveCollectionUser(String collectionId, String userId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_ID_KEY, collectionId);
      jo.put(USER_ID_KEY, userId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsRemoveCollectionUser"),mp,false,callback);
   }
   
   /**
    * Adds an item to a collection.
    * Can be used to modify an items collection details
    * 
    * @param collectionId The ID of the collection
    * @param resourceUrl The URL of the item
    * @param resourceTitle The title of the item 
    * @param resourceDescription The description of the item
    * @param itemIndex The index of the item in the collection
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsAddCollectionItem(String collectionId, String resourceId, String resourceUrl, String resourceTitle, String resourceDescription, JSONObject itemInfo, String itemIndex, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_ID_KEY, collectionId);
      jo.put(RESOURCE_URL_KEY, resourceUrl);
      jo.put(RESOURCE_TITLE_KEY, resourceTitle);
      jo.put(RESOURCE_DESC_KEY, resourceDescription);
      jo.put(ITEM_IDX_KEY, itemIndex);      
      jo.put("resourceId", resourceId);
      
      if(itemInfo != null){
    	  jo.put("itemInfo", itemInfo);
      }
      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddCollectionItem"),mp,false,callback);
   }
   
   /**
    * Removes an item from a collection.
    * 
    * @param collectionId The ID of the collection
    * @param resourceUrl The URL of the item
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsRemoveCollectionItem(String collectionId, String resourceUrl, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(COLLECTION_ID_KEY, collectionId);
      jo.put(RESOURCE_URL_KEY, resourceUrl);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsRemoveCollectionItem"),mp,false,callback);
   }
   
   /**
    * Deletes a DECALS application repository resource
    * 
    * @param resourceId The ID of the resource to delete
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsDeleteDarResource(String resourceId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(FILE_ID_KEY, resourceId);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsDeleteResource"),mp,false,callback);
   }
      
   /**
    * Deletes a DECALS application user and all associated resources
    * 
    * @param userId The ID of the user to delete
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsDeleteUserAndResources(String userId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, userId);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsDeleteUserAndResources"),mp,false,callback);
   }
   
   /**
    * Deletes a DECALS application user and transfers all associated resources
    * 
    * @param userId The ID of the user to delete
    * @param toUserId The ID of the user to receive the resources
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsDeleteUserTransferResources(String userId, String toUserId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, userId);  
      jo.put(TO_USER_ID_KEY, toUserId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsDeleteUserTransferResources"),mp,false,callback);
   }   
   
   /**
    * Resets the number of metadata entries for user contributions.
    * This should be the fastest way to determine the number of contributions for a user.
    *  
    * @param userId The ID of the user 
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsDarMetadataCountForUser(String userId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, userId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsResourceMetadataCountForUser"),mp,false,callback);
   }
   
   /**
    * Resets the user password as a user manager/administrator
    *  
    * @param userId The ID of the user to reset
    * @param newPassword The new password
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsResetPasswordAsAdmin(String userId, String newPassword, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, userId);
      jo.put(NEW_PASSWORD_KEY, newPassword);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsResetUserPasswordFromAdmin"),mp,false,callback);
   }
   
   /**
    * Retrieves the application user list
    *  
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetUserList(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetUserList"),mp,false,callback);
   }
   
   /**
    * Retrieves the user manager list
    *  
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetUserManagerList(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetUserManagerList"),mp,false,callback);
   }
   
   /**
    * Retrieves the paradata publication information list
    *  
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetParadataPublicationInfoList(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetAllParaPubInfo"),mp,false,callback);
   }
   
   /**
    * Publishes all resource paradata
    *  
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsPublishAllResourceParadata(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsPublishAllResourceParadata"),mp,false,callback);
   }
   
   /**
    * Publishes the paradata for a given resource
    *  
    * @param resourceUrl The URL of the resource to publish
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsPublishResourceParadata(String resourceUrl, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(RESOURCE_URL_KEY, resourceUrl);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsPublishResourceParadata"),mp,false,callback);
   }
   
   /**
    * Adds the given role to the given user
    *  
    * @param userId The ID of the user to give the role
    * @param roleId The ID of the role to assign to the user
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsAssignUserRole(String userId, String roleId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, userId);
      jo.put(ROLE_ID_KEY, roleId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddUserRole"),mp,false,callback);
   }
   
   /**
    * Removes the given role to the given user
    *  
    * @param userId The ID of the user to remove the role
    * @param roleId The ID of the role to remove from the user
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsRevokeUserRole(String userId, String roleId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(USER_ID_KEY, userId);
      jo.put(ROLE_ID_KEY, roleId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsRemoveUserRole"),mp,false,callback);
   }
      
   /**
    * Adds a comment to a resource (both DAR and registry)
    *  
    * @param resourceUrl The URL of the resource
    * @param comment The comment
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsAddComment(String resourceUrl, String comment, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(RESOURCE_URL_KEY, resourceUrl);
      jo.put(COMMENT_KEY, comment);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddComment"),mp,false,callback);
   }
   
   /**
    * Removes a comment from a resource (both DAR and registry)
    *  
    * @param resourceUrl The URL of the resource
    * @param commentId The ID of the comment to remove
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsRemoveComment(String resourceUrl, String commentId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(RESOURCE_URL_KEY, resourceUrl);
      jo.put(COMMENT_ID_KEY, commentId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsRemoveComment"),mp,false,callback);
   }
   
   
   /**
    * Returns the list of comments for a URL
    *  
    * @param resourceUrl The URL of the resource
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetCommentsForUrl(String resourceUrl, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(RESOURCE_URL_KEY, resourceUrl);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetCommentsForUrl"),mp,false,callback);
   }
   
   /**
    * Returns comment information for the given set of resource URLS
    *  
    * @param resourceUrlList The URL of the resource
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetCommentInfoForUrls(ArrayList<String> resourceUrls, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      //jo.put(SESSION_ID_KEY, sessionId);
      jo.put(RESOURCE_URLS_KEY, DsUtil.buildJsonArrayFromStringList(resourceUrls));      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetCommentInfoForUrlGroup"),mp,false,callback);
   }   
   
   public static String decalsGetParadataForUrls(ArrayList<String> resourceUrls, ESBCallback<ESBPacket> callback) {
	      MultipartPost mp = new MultipartPost();
	      ESBPacket jo = new ESBPacket();      
	      //jo.put(SESSION_ID_KEY, sessionId);
	      jo.put(RESOURCE_URLS_KEY, DsUtil.buildJsonArrayFromStringList(resourceUrls));      
	      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetParadataForUrlGroup"),mp,false,callback);
	   }   
   
   /**
    * Adds a rating to a resource (both DAR and registry)
    *  
    * @param resourceUrl The URL of the resource
    * @param rating The rating
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsAddRating(String resourceUrl, int rating, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(RESOURCE_URL_KEY, resourceUrl);
      jo.put(RATING_KEY, rating);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddRating"),mp,false,callback);
   }
   
   /**
    * Returns rating information for the given set of resource URLS
    *  
    * @param resourceUrlList The URL of the resource
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetRatingInfoForUrls(ArrayList<String> resourceUrls, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      //jo.put(SESSION_ID_KEY, sessionId);
      jo.put(RESOURCE_URLS_KEY, DsUtil.buildJsonArrayFromStringList(resourceUrls));      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetRatingInfoForUrlGroup"),mp,false,callback);
   }
   
   /**
    * Returns repository resource
    * 
    * @param resourceId The ID of the resource to delete
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetDarResource(String resourceId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();      
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsFileDownload?fileId=" + resourceId),mp,false,callback);
   }
   
   /**
    * Publish the given packet to the LR.
    * 
    * This should do all publish type activities (publish metadata and paradata and delete metadata and paradata 
    * depending on the lrDataPacket)
    * 
    * @param lrDataPacket The document packet to send to the LR
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsPublishToLR(ESBPacket lrDataPacket, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);            
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      mp.appendMultipartFormData(LR_PUBLISH_FORM_DATA_NAME, lrDataPacket);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsPublishToLr"),mp,false,callback);
   }
   
   /**
    * Updates the LR document ID and publish date of the given resource to the given value
    * 
    * @param resourceId The ID of the resource to update
    * @param lrDocId The value of the LR document ID
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsUpdateDarResourceLRPublishData(String resourceId, String lrDocId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);            
      jo.put(FILE_ID_KEY, resourceId);
      jo.put(LR_DOC_ID_KEY, lrDocId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);      
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsUpdateResourceLrData"),mp,false,callback);
   }
      
   /**
    * Attempts to generate a metadata set for the given DECALS application repository resource
    * 
    * @param resourceId The ID of the resource to generate metadata
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGenerateDarResourceMetadata(String resourceId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(FILE_ID_KEY, resourceId);      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGenerateResourceMetadata"),mp,false,callback);
   }
   
   /**
    * Retrieves the DECALS application settings
    * 
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetApplicationSettings(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAppSettings"),mp,false,callback);
   }
   
   /**
    * Updates the metadata of a DECALS application repository item
    * 
    * @param darMetadata The metadata values to update
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsUpdateDarMetadata(DarResourceMetadata darMetadata, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(FILE_ID_KEY, darMetadata.getId());      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      mp.appendMultipartFormData(FILE_METADATA_FORM_DATA_NAME, darMetadata.generateMetadataPayloadPacket());
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsResourceUpdateMetadata"),mp,false,callback);
   }
   
   /**
    * Add a URL to the repository
    * 
    * @param url The URL to add to the repository
    * @param pageTitle The page title of the URL
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsAddWebpage(String url, String pageTitle, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put(SESSION_ID_KEY, sessionId);
      jo.put(PAGE_TITLE_KEY, pageTitle);
      jo.put(URL_KEY, url);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsUrlUpload"),mp,false,callback);
   }
         
   /**
    * Get a user's information.  Session ID should already be set.
    * 
    * @param userId The user ID of the user to retrieve
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGetUser(String userId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, userId);
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetUser"),mp,false,callback);
   }
   
   /**
    * Login a DECALS user
    * 
    * @param userId The user's ID
    * @param password The user's password
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsLogin(String userId, String password, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, userId);
      jo.put(PASSWORD_KEY, password);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsLogin"),mp,false,callback);
   }
   
   /**
    * Logout a DECALS user
    * 
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsLogout(ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, username);
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsLogout"),mp,false,callback);
   }
   
   /**
    * Create a DECALS user
    * 
    * @param email The user's email (also used for user ID)
    * @param password The user's password
    * @param firstName The user's first name
    * @param lastName The user's last name
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsCreateUser(String email, String password, String firstName, String lastName, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, email);
      jo.put(EMAIL_KEY, email);
      jo.put(PASSWORD_KEY, password);
      jo.put(FIRST_NAME_KEY, firstName);
      jo.put(LAST_NAME_KEY, lastName);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsCreateUser"),mp,false,callback);
   }
   
   /**
    * Perform a SOLR search for the DECALS application resources for the given user
    * 
    * @param userId The user ID to use for the data retrieval
    * @param rows The number of rows to return
    * @param sortField The field on which to sort
    * @param sortOrder The order in which to sort (DESC, ASC)
    * @param mustMatchAll The must match all flag (if set to yes, then all terms in the query must be matched 100%)
    * @param start The record to start retrieval
    * @param callback The event callback
    * @return
    */
   public static String decalsDarSolrSearchByUser(String userId, String rows, String sortField, String sortOrder, boolean mustMatchAll, int start, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, userId);
      jo.put(ROWS_KEY, rows);
      jo.put(ID_SORT_KEY, "false");
      jo.put(USE_CURSOR_KEY, "false");
      jo.put(USE_MMA_KEY, String.valueOf(mustMatchAll));
      jo.put(SORT_FLD_KEY, sortField);
      jo.put(SORT_ORDER_KEY, sortOrder);
      jo.put(START_KEY, start);
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetFilesByUser"),mp,false,callback);
   }
      
   /**
    * Perform a SOLR search for the interactive search.
    * 
    * @param query The SOLR query
    * @param rows The number of rows to return
    * @param returnFields The fields to retrieve
    * @param mustMatchAll The must match all flag (if set to yes, then all terms in the query must be matched 100%)
    * @param start The record to start retrieval
    * @param callback The event callback
    * @return Returns the query result JSON string
    */
   public static String decalsSolrRegistrySearch(String query, String rows, String returnFields, boolean mustMatchAll, int start, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(QUERY_KEY, query);
      jo.put(ROWS_KEY, rows);
      jo.put(RETURN_FLDS_KEY, returnFields);
      jo.put(ID_SORT_KEY, "false");
      jo.put(USE_CURSOR_KEY, "false");
      jo.put(USE_MMA_KEY, String.valueOf(mustMatchAll));
      jo.put(START_KEY, start);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsSolrRegistryQuery"),mp,false,callback);
   }
      
   /**
    * Performs a query count for all queries in the queryTerms list.
    * Used for interactive search
    * 
    * @param queryTerms The list of SOLR queries
    * @param mustMatchAll The must match all flag (if set to yes, then all terms in the query must be matched 100%)
    * @param callback The event callback
    * @return Returns the query result count JSON string
    */
   public static String decalsSolrRegistryQueryCounts(ArrayList<String> queryTerms, boolean mustMatchAll, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(TERMS_KEY, buildQueryTerms(queryTerms));
      jo.put(USE_MMA_KEY, String.valueOf(mustMatchAll));
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsSolrRegistryQueryCounts"),mp,false,callback);
   }
   
   
   /**
    * Returns all nouns for all text in the text list
    * 
    * @param textList The list of texts for which to retrieve the nouns
    * @param callback The event callback
    * @return Returns the nouns result JSON string
    */
   public static String decalsGetTextNouns(ArrayList<String> textList, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(TEXT_KEY,buildQueryTerms(textList));
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetTextNouns"),mp,false,callback);
   }
   
   /**
    * Returns the nouns and keywords for the given text.
    * 
    * @param text The text for which to find the nouns and keywords.
    * @param callback The event callback
    * @return Returns the nouns and keywords for the given text as a JSON string
    */
   public static String decalsGetTextHighlights(String text, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(TEXT_KEY,text);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetTextHighlights"),mp,false,callback);
   }
   
   
   /**
    * Returns the word ontology/definitions/relationships for the given word.
    * 
    * @param word The word to define
    * @param levels The number of relationship levels to search
    * @param callback The even callback
    * @return Returns the word ontology/definitions/relationships for the given word as a JSON string
    */
   public static String decalsDefineWord(String word, int levels, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(WORD_KEY,word);
      jo.put(LEVELS_KEY,String.valueOf(levels));
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsDefineWord"),mp,false,callback);
   }
   
   /**
    * Returns a set of Wikipedia related information for the page with the given title.
    * 
    * @param title The (approximate) title of the wikipedia page to search 
    * @param callback The event callback
    * @return Returns a set of Wikipedia related information for the page with the given title as a JSON string.
    */
   public static String decalsWikiInfo(String title, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(TITLE_KEY,title);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsWikiInfo"),mp,false,callback);
   }
   
   /**
    * Adds a tracking statement to the default levr database with the given information.
    * 
    * @param assignmentId The user/session assignment ID
    * @param message The message to track
    * @param callback The event callback
    * @return Tracking update confirmation as a JSON string.
    */
   public static String decalsAddTracking(String assignmentId, String message, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(ASSIGN_ID_KEY,assignmentId);
      jo.put(MESSAGE_KEY,message);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddTracking"),mp,false,callback);
   }
   
   /**
    * Perform a basic search
    * @param searchTerm The search term
    * @param rows The number of rows to return
    * @param page The page to start retrieval
    * @return Returns the query result JSON string
    */
   public static String decalsBasicSearch(String searchTerm, int rows, int page, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(SEARCH_TERM_KEY, searchTerm);
      jo.put(ITEMS_PER_PAGE_KEY, rows);
      jo.put(PAGE_KEY, page);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsBasicSearch"),
                                 mp, 
                                 false, 
                                 callback);
   }
   
   /**
    * Perform a SOLR search for the Learner Focused search.
    * 
    * @param terms The Terms to search for 
    * @param rows The number of rows to return
    * @param returnFields The fields to retrieve
    * @param mustMatchAll The must match all flag (if set to yes, then all terms in the query must be matched 100%)
    * @param start The record to start retrieval
    * @param callback The event callback
    * @return Returns the query result JSON string
    */
   public static String decalsSolrLearnerFocusedSearch(String terms, String rows, String returnFields, boolean mustMatchAll, int start, 
		   ArrayList<String> ignorePrefs, String appliedGradeLevels, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(SESSION_ID_KEY, sessionId);
      
      jo.put(QUERY_KEY, terms);
      jo.put(ROWS_KEY, rows);
      jo.put(RETURN_FLDS_KEY, returnFields);
      jo.put(ID_SORT_KEY, "false");
      jo.put(USE_CURSOR_KEY, "false");
      jo.put(USE_MMA_KEY, String.valueOf(mustMatchAll));
      jo.put(START_KEY, start);
      
      JSONArray ignoredPrefs = new JSONArray();
      for(int i = 0; i < ignorePrefs.size(); i++){
    	  String pref = ignorePrefs.get(i);
    	  ignoredPrefs.set(i, new JSONString(pref));
      }
      jo.put("ignorePrefs", ignoredPrefs);
      
      if(ignorePrefs.contains("grade_levels")) jo.put("appliedGradeLevels", appliedGradeLevels);
      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsLearnerProfileQuery"),mp,false,callback);
   }
   
   /**
    * Get User Preferences
    * @return Returns the user preferences JSON string
    */
   public static String decalsUserPreferences(ESBCallback<ESBPacket> callback) {
	  MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, username);
      jo.put(SESSION_ID_KEY, sessionId);
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetUserPreferences"),mp,false,callback);
   }
   
   /**
    * Get Preference Types for Select Boxes
    * @return Returns the preference types JSON string
    */
   public static String decalsPreferenceTypes(ESBCallback<ESBPacket> callback) {
	  MultipartPost mp = new MultipartPost();
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsPreferenceEnumTypes"),mp,false,callback);
   }
   
   /**
    * Get User Competencies
    * @return Returns the preference types JSON string
    */
   public static String decalsUserCompetencies(ESBCallback<ESBPacket> callback) {
	  MultipartPost mp = new MultipartPost();
	  ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, username);
      jo.put(COMPETENCY_SESSION_ID_KEY, DsSession.getUser().getCompetencySessionId());
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsUserCompetencies"),mp,false,callback);
   }
   
   
   /**
    * Get Learning Objectives
    * @return Returns an array of the learning objectives user's currently have used
    */
   public static String decalsLearningObjectives(ESBCallback<ESBPacket> callback) {
	  MultipartPost mp = new MultipartPost();
	  ESBPacket jo = new ESBPacket();
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsLearningObjectiveList"),mp,false,callback);
   }
   
   /**
    * Update User Preferences
    * @param resourceTypes - 
    * @param languages -
    * @param gradeLevels - 
    * @param learningObjectives -  
    * @return Returns the preference types JSON string
    */
   public static String decalsUpdateUserPreferences(ArrayList<RESOURCE_TYPE> resourceTypes, ArrayList<LANGUAGE> languages, ArrayList<GRADE_LEVEL> gradeLevels, ArrayList<String> learningObjectives, ESBCallback<ESBPacket> callback) {
	  MultipartPost mp = new MultipartPost();
	  ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, username);
      jo.put(SESSION_ID_KEY, sessionId);
      
      JSONArray types = new JSONArray();
      for(int i = 0; i < resourceTypes.size(); i++)
    	  types.set(i, new JSONString(resourceTypes.get(i).toSymbol()));
      jo.put("resourceTypes", types);
      
      JSONArray langs = new JSONArray();
      for(int i = 0; i < languages.size(); i++)
    	  langs.set(i,new JSONString(languages.get(i).toSymbol()));
      jo.put("languages", langs);
      
      JSONArray grades = new JSONArray();
      for(int i = 0; i < gradeLevels.size(); i++)
    	  grades.set(i, new JSONString(gradeLevels.get(i).toSymbol()));
      jo.put("gradeLevels", grades);
      
      JSONArray objectives = new JSONArray();
      for(int i = 0; i < learningObjectives.size(); i++)
    	  objectives.set(i, new JSONString(learningObjectives.get(i)));
      jo.put("learningObjectives", objectives);
      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsUpdateUserPreferences"),mp,false,callback);
   }
   
   /**
    * Adds New Competencies to the Users Desired Competencies List
    * @param resourceTypes - -  
    * @return Returns the preference types JSON string
    */
   public static String decalsAddDesiredCompetencies(ArrayList<String> competencyIds, ESBCallback<ESBPacket> callback) {
	  MultipartPost mp = new MultipartPost();
	  ESBPacket jo = new ESBPacket();
      jo.put(USER_ID_KEY, username);
      jo.put(SESSION_ID_KEY, sessionId);
      
      
      mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsUserCompetencies"),mp,false,callback);
   }
   
   public static String decalsValidateSession(ESBCallback<ESBPacket> callback){
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
	   jo.put(SESSION_ID_KEY, sessionId);
	   
	   mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	   return CommunicationHub.sendMultipartPost(getESBActionURL("decalsTestCheckSession"),mp,false,callback);
   }
   
   
   public static String decalsSearchCompetencies(String query, ESBCallback<ESBPacket> callback) {
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
	   jo.put("query", query);
	   jo.put(COMPETENCY_SESSION_ID_KEY, DsSession.getUser().getCompetencySessionId());
	      
	   mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	   return CommunicationHub.sendMultipartPost(getESBActionURL("decalsSearchCompetencies"),mp,false,callback);
   }

   public static String decalsAddDesiredCompetency(String competencyId,String modelId, ESBCallback<ESBPacket> callback) {
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
	   jo.put("competencyId", competencyId);
	   jo.put("modelId", modelId);
	   jo.put(USER_ID_KEY, username);
	   jo.put(SESSION_ID_KEY, DsSession.getUser().getCompetencySessionId());
	
	   mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	   return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddDesiredCompetency"),mp,false,callback);
   }
   
   public static String decalsDuplicateResource(String resourceUrl, JSONObject flrInfo, ESBCallback<ESBPacket> callback){
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
	   
	   jo.put(USER_ID_KEY, username);
	   jo.put(SESSION_ID_KEY, DsSession.getUser().getCompetencySessionId());
	   
	   jo.put("itemInfo", flrInfo);
	   
	   mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	   return CommunicationHub.sendMultipartPost(getESBActionURL("decalsDuplicateResource"),mp,false,callback);
   }
   
   public static String addSimilarUrl(String resourceTitle, String resourceUrl, JSONObject similarFields, ESBCallback<ESBPacket> callback){
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
	   
	   jo.put(USER_ID_KEY, username);
	   jo.put(SESSION_ID_KEY, DsSession.getUser().getCompetencySessionId());
	   
	   jo.put("pageTitle", resourceTitle);
	   jo.put("url", resourceUrl);
	   
	   jo.put("similar", similarFields);
	   
	   mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	   return CommunicationHub.sendMultipartPost(getESBActionURL("decalsAddSimilarUrl"),mp,false,callback);
   }
   
   public static String decalsGetCompetencyInfo(String competencyUri, ESBCallback<ESBPacket> callback){
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
	   
	   jo.put(SESSION_ID_KEY, DsSession.getUser().getCompetencySessionId());
	   jo.put("competencyUri", competencyUri);
	   
	   mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	   return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGetCompetencyInfo"), mp, false, callback);
   }

   public static String findSimilarResources(String url, ESBCallback<ESBPacket> callback) {
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
	   
	   jo.put(SESSION_ID_KEY, DsSession.getUser().getCompetencySessionId());
	   jo.put("similarUrl", url);
	   
	   mp.appendMultipartFormData(DECALS_FORM_DATA_NAME, jo);
	   return CommunicationHub.sendMultipartPost(getESBActionURL("decalsFindSimilar"), mp, false, callback);
   }
}
