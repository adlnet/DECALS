package com.eduworks.decals.ui.client.pagebuilder.screen;

import java.util.HashMap;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.handler.DsHeaderHandler;
import com.eduworks.decals.ui.client.handler.DsUserTabsHandler;
import com.eduworks.decals.ui.client.handler.ViewHandler;
import com.eduworks.decals.ui.client.model.AdminUserMgmtHelper;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.UserRoles;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.util.AdminUserMgmtViewBuilder;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * The user management screen.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsUserManagementScreen extends DecalsWithGroupMgmtScreen implements ViewHandler {
   
   private static final String USERS_LINK = "umUsersLink";
   private static final String USER_MGR_LINK = "umUserMgrLink";
   private static final String GROUPS_LINK = "umGroupsLink";
      
   private static final String USERS_LINK_TEXT = "umUsersLinkText";
   private static final String USER_MGR_LINK_TEXT = "umUserMgrLinkText";
   private static final String GROUPS_LINK_TEXT = "umGroupsLinkText";
   
   private static final String USERS_CONTAINER = "umUsersContainer";
   private static final String USER_MGRS_CONTAINER = "umUserMgrContainer";
   private static final String GROUPS_CONTAINER = "umGroupsContainer";
   
   private static final String USERS_DETAILS_CONTAINER = "umUsersDetails";
   private static final String USER_MGR_DETAILS_CONTAINER = "umUserMgrDetails";
   
   private static final String UM_USER_NAME = "umUserName";
   private static final String UM_USER_EMAIL = "umUserEmail";
   private static final String UM_USER_LIST_CONTAINER = "umUserList";
   
   private static final String UM_USER_MGR_NAME = "umUserMgrName";
   private static final String UM_USER_MGR_EMAIL = "umUserMgrEmail";
   private static final String UM_USER_MGR_LIST_CONTAINER = "umUserMgrList";
   
   private static final String UM_BUSY = "umBusy";
      
   private static final String UM_NEW_USER = "umAddNewUser";
   private static final String UM_NEW_USER_MGR = "umAddNewUserManager";
      
   private static final String AUM_MODAL = "modalAddUserMgr";
   private static final String AUM_INNER_CONTAINER = "addUserMgrUserListContainer";
   private static final String AUM_USER_LIST_CONTAINER = "addUserMgrUserList";
   private static final String AUM_USER_NAME = "addUserMgrUserName";
   private static final String AUM_USER_ID = "addUserMgrUserId";
   private static final String AUM_FINISHED_BTN = "addUserMgrFinishedButton";
   private static final String AUM_SUCCESS_CONTAINER = "addUserMgrSuccess";
   private static final String AUM_SUCCESS_NAME = "addUserMgrSuccessName";
   private static final String AUM_BUSY = "addUserMgrBusy";
   private static final String AUM_PICKLIST = "addUserMgrPickList";
   private static final String AUM_CONFIRM = "addUserMgrConfirm";
   private static final String AUM_CONFIRM_NAME = "addUserMgrConfirmName";
   private static final String AUM_CONFIRM_NAME_HIDDEN = "addUserMgrConfirmNameHidden";   
   private static final String AUM_CONFIRM_USER_ID = "addUserMgrConfirmUserId";
   private static final String AUM_CONFIRM_FORM = "addUserMgrConfirmForm";
      
   private static final String RUM_MODAL = "modalRevokeUserMgrConfirm";
   private static final String RUM_NAME = "revokeUserMgrName";
   private static final String RUM_FORM = "revokeUserMgrForm";
   private static final String RUM_USER_ID = "revokeUserMgrUserId";
   private static final String RUM_BUSY = "revokeUserMgrBusy";   
   private static final String RUM_SUBMIT_BTN_CONTAINER = "revokeUserMgrSubmitButtons";
   private static final String RUM_SUCCESS_CONTAINER = "revokeUserMgrSuccess"; 
   
   private static final String ANU_MODAL = "modalAddNewUser";
   private static final String ANU_FORM = "addNewUserForm";
   private static final String ANU_FIRST_NAME = "newUserFirstName";
   private static final String ANU_LAST_NAME = "newUserLastName";
   private static final String ANU_EMAIL = "newUserEmail";
   private static final String ANU_PASSWORD = "newUserPassword";
   private static final String ANU_BUSY = "newUserBusy";
   private static final String ANU_ERROR_MESSAGE = "newUserErrorMsg";
   private static final String ANU_ERROR_CONTAINER = "newUserErrorContainer";
   private static final String ANU_SUBMIT_BUTTONS = "newUserSubmitButtons";  
   private static final String ANU_SUCCESS_CONTAINER = "newUserSuccessContainer";
   private static final String ANU_SUCCESS_USERID = "newUserSuccessUserId";
   
   private static final String RP_MODAL = "modalResetPassword";
   private static final String RP_USER_ID = "resetPasswordUserId";   
   private static final String RP_USER_NAME = "resetPasswordUserName";
   private static final String RP_FORM = "resetPasswordForm";
   private static final String RP_PASSWORD = "resetPasswordPassword";   
   private static final String RP_BUSY = "resetPasswordBusy";
   private static final String RP_ERROR_MESSAGE = "resetPasswordErrorMsg";
   private static final String RP_ERROR_CONTAINER = "resetPasswordErrorContainer";
   private static final String RP_SUBMIT_BUTTONS = "resetPasswordSubmitButtons";  
   private static final String RP_SUCCESS_CONTAINER = "resetPasswordSuccessContainer";   
   
   private static final String DU_MODAL = "modalDeleteUser";
   private static final String DU_USER_NAME = "deleteUserUserName";
   private static final String DU_USER_ID = "deleteUserUserId";
   private static final String DU_SUBMIT_BUTTONS = "deleteUserSubmitButtons";
   private static final String DU_DELETE_BTN = "deleteUserDeleteButton";
   private static final String DU_DELETE_ALL_BTN = "deleteUserDeleteUserAndResourcesButton";
   private static final String DU_DELETE_AND_TRNS_BTN = "deleteUserDeleteUserTransferResourcesButton";
   private static final String DU_BUSY = "deleteUserBusy";
   private static final String DU_ERROR_MESSAGE = "deleteUserErrorMsg";
   private static final String DU_ERROR_CONTAINER = "deleteUserErrorContainer";
   private static final String DU_SUCCESS_CONTAINER = "deleteUserSuccessContainer";
   private static final String DU_SUCCESS_MSG = "deleteUserSuccess";
   private static final String DU_SUCCESS_ALL_MSG = "deleteUserAllSuccess";
   private static final String DU_SUCCESS_TRANSFER_MSG = "deleteUserTransferSuccess";
   private static final String DU_RESOURCE_MSG = "deleteUserResourceMessage";
   private static final String DU_RESOURCE_MSG_NUM = "deleteUserResourceMessageNum";  
   private static final String DU_SELF_MSG = "deleteUserSelfMessage";
   private static final String DU_INITIAL = "deleteUserInitial";
   
   private static final String DUAR_CONFIRM = "deleteUserAndResConfirm";
   private static final String DUAR_CONFIRM_FORM = "duarConfirmForm";   
   private static final String DUAR_CONFIRM_NAME = "duarConfirmName";
   private static final String DUAR_CONFIRM_USER_ID = "duarConfirmUserId";
   private static final String DUAR_CONFIRM_SUBMIT_BTNS = "duarConfirmSubmitButtons";   
   
   private static final String DUAT_PICKLIST = "deleteUserTransferPicklist";   
   private static final String DUAT_INNER_CONTAINER = "duatUserListContainer";
   private static final String DUAT_USER_LIST_CONTAINER = "duatUserList";
   private static final String DUAT_USER_NAME = "duatUserName";
   private static final String DUAT_USER_ID = "duatUserId";
   
   private static final String DUAT_CONFIRM = "deleteUserTransferConfirm";
   private static final String DUAT_CONFIRM_NAME = "duatConfirmName";
   private static final String DUAT_CONFIRM_TO_NAME = "duatConfirmTransferName";
   private static final String DUAT_CONFIRM_FORM = "duatConfirmForm";  
   private static final String DUAT_CONFIRM_USER_ID = "duatConfirmUserId";
   private static final String DUAT_CONFIRM_TO_USER_ID = "duatConfirmToUserId";
   private static final String DUAT_CONFIRM_SUBMIT_BTNS = "duatConfirmSubmitButtons";   
   
   private static final String PGRP_NAV_CONTAINER = "umGroupsNavigation";
   private static final String PGRP_NONE = "emptyUmGroups";
   private static final String PGRP_BUSY = "umGroupsSearchBusy";
   private static final String PGRP_CURRENT_CONTAINER = "currentGroupContainer";
   private static final String PGRP_SELECTIONS = "umGroupsSelections";
   private static final String PGRP_MORE_SELECTIONS_CONTAINER = "umGroupsMoreSelections";
   private static final String PGRP_MORE_SELECTIONS_LINK = "umGroupsMoreSelectionsLink";
   private static final String PGRP_LESS_SELECTIONS_CONTAINER = "umGroupsLessSelections";
   private static final String PGRP_LESS_SELECTIONS_LINK = "umGroupsLessSelectionsLink";
      
   private static final String CREATE_USER_ERROR = "An error occured while trying to create the new user.";
   private static final String EMAIL_IN_USE = "This email is already in use.";
   private static final String RESET_PASSWORD_FAILED = "Reset password failed.";
   private static final String DELETE_USER_FAILED = "Delete user failed.";
   
   private static final int LIST_ITEMS_PER_PAGE = 5;
   private static final int USERS_PER_PAGE = 4;
   
   private boolean groupsInitialized = false;
   
   private HashMap<String,AppUser> resetPasswordWidgets = new HashMap<String,AppUser>();
   private HashMap<String,AppUser> deleteUserWidgets = new HashMap<String,AppUser>();
   private HashMap<String,AppUser> revokeWidgets = new HashMap<String,AppUser>();
   private HashMap<String,AppUser> newUserManagerWidgets = new HashMap<String,AppUser>();
   private HashMap<String,AppUser> deleteUserTransferWidgets = new HashMap<String,AppUser>();
   
   private AdminUserMgmtHelper umHelper = new AdminUserMgmtHelper();
   
   private AppUser deleteUser;
   
   //delete user and transfer resources confirm delete listener
   protected EventCallback deleteUserTransferResourcesConfirmSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(DUAT_CONFIRM_SUBMIT_BTNS);
         DsUtil.showLabel(DU_BUSY);
         final String userId = DsUtil.getLabelText(DUAT_CONFIRM_USER_ID);
         String toUserId = DsUtil.getLabelText(DUAT_CONFIRM_TO_USER_ID);
         DsESBApi.decalsDeleteUserTransferResources(userId, toUserId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleDeleteUserResponse(result,DU_SUCCESS_TRANSFER_MSG,userId);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(DU_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });        
      }
   };  
   
   //handle delete user with transfer
   private void handleDeleteUserWithTransfer(AppUser tu) {
      AppUser user = deleteUser;
      deleteUser = null;
      DsUtil.setLabelText(DUAT_CONFIRM_NAME,user.getFullName());         
      DsUtil.setLabelText(DUAT_CONFIRM_TO_NAME,tu.getFullName());
      DsUtil.setLabelText(DUAT_CONFIRM_USER_ID,user.getUserId());
      DsUtil.setLabelText(DUAT_CONFIRM_TO_USER_ID,tu.getUserId());
      DsUtil.hideLabel(DUAT_PICKLIST);
      DsUtil.showLabel(DUAT_CONFIRM); 
   }
   
   //add delete user with transfer event listener
   private class DeleteUserTransferClickListener extends EventCallback {      
      private AppUser tu;    
      public DeleteUserTransferClickListener(AppUser tu) {
         this.tu = tu;
      }      
      @Override
      public void onEvent(Event event) {handleDeleteUserWithTransfer(tu);}
   }
   
   //register add delete user with transfer widget event handlers
   private void registerDeleteUserTransferWidgets() {
      for (String key:deleteUserTransferWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new DeleteUserTransferClickListener(deleteUserTransferWidgets.get(key)));
      }
   }
   
   //delete user transfer resources listener
   protected EventCallback deleteUserTransferResourcesListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {                
         deleteUserTransferWidgets.clear();
         AdminUserMgmtViewBuilder.buildNewDeleteUserTransferUserList(DUAT_INNER_CONTAINER, umHelper.getExcludedUserList(deleteUser.getUserId()),deleteUserTransferWidgets);
         registerDeleteUserTransferWidgets();
         initDeleteUserTransferUserListFiltering(DUAT_USER_LIST_CONTAINER,DUAT_USER_NAME,DUAT_USER_ID,LIST_ITEMS_PER_PAGE);
         DsUtil.hideLabel(DU_INITIAL);
         DsUtil.showLabel(DUAT_PICKLIST);
      }
   };  
   
   //delete user and resources confirm delete listener
   protected EventCallback deleteUserAndResourcesConfirmSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(DUAR_CONFIRM_SUBMIT_BTNS);
         DsUtil.showLabel(DU_BUSY);
         final String userId = DsUtil.getLabelText(DUAR_CONFIRM_USER_ID);
         DsESBApi.decalsDeleteUserAndResources(userId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleDeleteUserResponse(result,DU_SUCCESS_ALL_MSG,userId);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(DU_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });        
      }
   };  
      
   //delete user and resources listener
   protected EventCallback deleteUserAndResourcesListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         AppUser user = deleteUser;
         deleteUser = null;
         DsUtil.setLabelText(DUAR_CONFIRM_NAME,user.getFullName());         
         DsUtil.setLabelText(DUAR_CONFIRM_USER_ID,user.getUserId());
         DsUtil.hideLabel(DU_INITIAL);
         DsUtil.showLabel(DUAR_CONFIRM);         
      }
   };  
   
   //delete user listener
   protected EventCallback deleteUserListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         AppUser user = deleteUser;
         deleteUser = null;
         DsUtil.hideLabel(DU_SUBMIT_BUTTONS);
         DsUtil.showLabel(DU_BUSY);
         final String userId = user.getUserId();
         DsESBApi.decalsDeleteUserAndResources(user.getUserId(), new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleDeleteUserResponse(result,DU_SUCCESS_MSG,userId);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(DU_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });        
      }
   };  
   
   //handle delete user response
   private void handleDeleteUserResponse(ESBPacket result, String successMessageId, String userId) {
      DsUtil.hideLabel(DU_BUSY);   
      if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().containsKey(DsESBApi.MSG_KEY)) {
         if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().get(DsESBApi.MSG_KEY).toString().equalsIgnoreCase(DsESBApi.TRUE_MSG)) {            
            DsUtil.showLabel(DU_SUCCESS_CONTAINER);    
            DsUtil.showLabel(successMessageId);
            umHelper.refreshUserList();
            DsSession.removeUserFromAllCollections(userId);
         }
         else {
            DsUtil.showSimpleErrorMessage(DU_ERROR_MESSAGE,DELETE_USER_FAILED);
            DsUtil.showLabel(DU_ERROR_CONTAINER);
         }
      }
      else {
         DsUtil.showSimpleErrorMessage(DU_ERROR_MESSAGE,DELETE_USER_FAILED);
         DsUtil.showLabel(DU_ERROR_CONTAINER);
      }      
   }
   
   //sets up the delete user modal
   private void setUpDeleteUserModal(String userId, double numUserResources) {
      if (DsSession.getUser().getUserId().equalsIgnoreCase(userId)) {
         DsUtil.showLabel(DU_SELF_MSG);
      }
      else if (numUserResources > 0) {
         DsUtil.setLabelText(DU_RESOURCE_MSG_NUM,String.valueOf((long) numUserResources));
         DsUtil.showLabel(DU_RESOURCE_MSG);
         DsUtil.showButtonInline(DU_DELETE_ALL_BTN);
         DsUtil.showButtonInline(DU_DELETE_AND_TRNS_BTN);
      }
      else {
         DsUtil.showButtonInline(DU_DELETE_BTN);
      }
   }
   
   //initializes the delete user modal
   private void initDeleteUser(AppUser user, double numUserResources) {
      resetDeleteUserModal();
      DsUtil.setLabelText(DU_USER_NAME,user.getFullName());
      setUpDeleteUserModal(user.getUserId(), numUserResources);
      deleteUser = user;
      PageAssembler.openPopup(DU_MODAL);
   }
   
   //resets the delete user modal
   private void resetDeleteUserModal() {
      DsUtil.hideLabel(DU_BUSY);
      DsUtil.hideLabel(DU_ERROR_CONTAINER);
      DsUtil.hideLabel(DU_SUCCESS_CONTAINER);
      DsUtil.hideLabel(DU_SUCCESS_MSG);
      DsUtil.hideLabel(DU_SUCCESS_ALL_MSG);
      DsUtil.hideLabel(DU_SUCCESS_TRANSFER_MSG);
      DsUtil.hideLabel(DU_RESOURCE_MSG);
      DsUtil.hideLabel(DU_SELF_MSG);
      DsUtil.setLabelAttribute(DU_SUBMIT_BUTTONS,"style", "display='inline'"); 
      DsUtil.setLabelAttribute(DUAR_CONFIRM_SUBMIT_BTNS,"style", "display='inline'");
      DsUtil.hideLabel(DU_DELETE_BTN);
      DsUtil.hideLabel(DU_DELETE_ALL_BTN);
      DsUtil.hideLabel(DU_DELETE_AND_TRNS_BTN);
      DsUtil.hideLabel(DUAR_CONFIRM);
      DsUtil.hideLabel(DUAT_PICKLIST);
      DsUtil.hideLabel(DUAT_CONFIRM);
      DsUtil.showLabel(DU_INITIAL);
   }
   
   //handle delete user
   private void handleDeleteUser(final AppUser user) {
      DsESBApi.decalsDarMetadataCountForUser(user.getUserId(), new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {initDeleteUser(user,result.get(ESBApi.ESBAPI_RETURN_OBJ).isNumber().doubleValue());}
         @Override
         public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
      }); 
   }
   
   //handle reset password response
   private void handleResetPasswordResponse(ESBPacket result) {
      DsUtil.hideLabel(RP_BUSY);   
      if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().containsKey(DsESBApi.MSG_KEY)) {
         if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().get(DsESBApi.MSG_KEY).toString().equalsIgnoreCase(DsESBApi.TRUE_MSG)) {            
            DsUtil.showLabel(RP_SUCCESS_CONTAINER);                        
         }
         else {
            DsUtil.showSimpleErrorMessage(RP_ERROR_MESSAGE,RESET_PASSWORD_FAILED);
            DsUtil.showLabel(RP_ERROR_CONTAINER);
         }
      }
      else {
         DsUtil.showSimpleErrorMessage(RP_ERROR_MESSAGE,RESET_PASSWORD_FAILED);
         DsUtil.showLabel(RP_ERROR_CONTAINER);
      }
   }
   
   //reset password submit listener
   protected EventCallback resetPasswordSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(RP_SUBMIT_BUTTONS);
         DsUtil.showLabel(RP_BUSY);
         String newPassword = DsUtil.getTextBoxText(RP_PASSWORD);
         String userId = DsUtil.getLabelText(RP_USER_ID);
         DsESBApi.decalsResetPasswordAsAdmin(userId, newPassword, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleResetPasswordResponse(result);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(RP_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });        
      }
   };    
   
   //handle reset password
   private void handleResetPassword(AppUser user) {
      DsUtil.hideLabel(RP_BUSY);
      DsUtil.hideLabel(RP_ERROR_CONTAINER);
      DsUtil.hideLabel(RP_SUCCESS_CONTAINER);
      DsUtil.setLabelAttribute(RP_SUBMIT_BUTTONS,"style", "display='inline'");      
      FormPanel resetPasswordForm = (FormPanel)PageAssembler.elementToWidget(RP_FORM, PageAssembler.FORM);
      resetPasswordForm.reset();
      DsUtil.setLabelText(RP_USER_NAME,user.getUserId());
      DsUtil.setLabelText(RP_USER_ID,user.getUserId());
      PageAssembler.openPopup(RP_MODAL);
   }   

   //add new user listener
   protected EventCallback addNewUserListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(ANU_BUSY);
         DsUtil.hideLabel(ANU_ERROR_CONTAINER);
         DsUtil.hideLabel(ANU_SUCCESS_CONTAINER);
         DsUtil.setLabelAttribute(ANU_SUBMIT_BUTTONS,"style", "display='inline'");
         FormPanel addNewUserForm = (FormPanel)PageAssembler.elementToWidget(ANU_FORM, PageAssembler.FORM);
         addNewUserForm.reset();
         PageAssembler.openPopup(ANU_MODAL);         
      }
   };
   
   //handle add new user response
   private void handleAddNewUserResponse(String userId, ESBPacket result) {
      DsUtil.hideLabel(ANU_BUSY);   
      if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().containsKey(DsESBApi.MSG_KEY)) {
         if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().get(DsESBApi.MSG_KEY).toString().equalsIgnoreCase(DsESBApi.TRUE_MSG)) {            
            DsUtil.setLabelText(ANU_SUCCESS_USERID,userId);
            DsUtil.showLabel(ANU_SUCCESS_CONTAINER);
            umHelper.refreshUserList();            
         }
         else {
            DsUtil.showSimpleErrorMessage(ANU_ERROR_MESSAGE,EMAIL_IN_USE);
            DsUtil.showLabel(ANU_ERROR_CONTAINER);
         }
      }
      else {
         DsUtil.showSimpleErrorMessage(ANU_ERROR_MESSAGE,CREATE_USER_ERROR);
         DsUtil.showLabel(ANU_ERROR_CONTAINER);
      }
   }
   
   //add new user submit listener
   protected EventCallback addNewUserSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(ANU_SUBMIT_BUTTONS);
         DsUtil.showLabel(ANU_BUSY);
         String firstName = DsUtil.getTextBoxText(ANU_FIRST_NAME);
         String lastName = DsUtil.getTextBoxText(ANU_LAST_NAME);
         String email = DsUtil.getTextBoxText(ANU_EMAIL);      
         String password = DsUtil.getTextBoxText(ANU_PASSWORD);         
         final String userId = email;         
         DsESBApi.decalsCreateUser(email, password, firstName, lastName, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleAddNewUserResponse(userId, result);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(ANU_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });        
      }
   };  
   
   //handle post add new user manager
   private void handleAddNewUserManagerResults(JSONObject results) {      
      DsUtil.hideLabel(AUM_BUSY);
      AppUser newUm = new AppUser(results);
      DsUtil.setLabelText(AUM_SUCCESS_NAME, newUm.getFullName());
      DsUtil.showLabel(AUM_SUCCESS_CONTAINER);
      umHelper.addUserManagerToList(newUm);
      newUserManagerWidgets.clear();
      AdminUserMgmtViewBuilder.buildNewUserManagerUserList(AUM_INNER_CONTAINER, umHelper.getNonUserManagerUserList(),newUserManagerWidgets);
      registerAddNewUserManagerWidgets();
      initNewUserManagerUserListFiltering(AUM_USER_LIST_CONTAINER,AUM_USER_NAME,AUM_USER_ID,LIST_ITEMS_PER_PAGE);
   }
   
   //add new user manager confirm submit listener
   protected EventCallback addUserManagerConfirmSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {     
         String userId = DsUtil.getLabelText(AUM_CONFIRM_USER_ID);      
         DsUtil.hideLabel(AUM_CONFIRM);
         DsUtil.showLabel(AUM_PICKLIST);     
         DsUtil.hideLabel(AUM_FINISHED_BTN);
         DsUtil.showLabel(AUM_BUSY);
         DsESBApi.decalsAssignUserRole(userId, UserRoles.USER_MANAGER_ROLE, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleAddNewUserManagerResults(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(AUM_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });         
      }
   };  
   
   //handle add new user manager
   private void handleAddNewUserManager(AppUser um) {
      DsUtil.setLabelText(AUM_CONFIRM_NAME,um.getFullName());
      DsUtil.setLabelText(AUM_CONFIRM_NAME_HIDDEN,um.getFullName());      
      DsUtil.setLabelText(AUM_CONFIRM_USER_ID,um.getUserId());
      DsUtil.hideLabel(AUM_PICKLIST);
      DsUtil.hideLabel(AUM_SUCCESS_CONTAINER);
      DsUtil.showLabel(AUM_CONFIRM); 
   }
   
   //add new user manager event listener
   private class AddNewUserManagerClickListener extends EventCallback {      
      private AppUser um;    
      public AddNewUserManagerClickListener(AppUser um) {
         this.um = um;
      }      
      @Override
      public void onEvent(Event event) {handleAddNewUserManager(um);}
   }
   
   //register add new user manager widget event handlers
   private void registerAddNewUserManagerWidgets() {
      for (String key:newUserManagerWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new AddNewUserManagerClickListener(newUserManagerWidgets.get(key)));
      }
   }
   
   //add new user manager listener
   protected EventCallback addNewUserManagerListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(AUM_SUCCESS_CONTAINER);
         DsUtil.hideLabel(AUM_CONFIRM);
         DsUtil.showLabel(AUM_PICKLIST);         
         DsUtil.showLabel(AUM_FINISHED_BTN);
         newUserManagerWidgets.clear();
         AdminUserMgmtViewBuilder.buildNewUserManagerUserList(AUM_INNER_CONTAINER, umHelper.getNonUserManagerUserList(),newUserManagerWidgets);
         registerAddNewUserManagerWidgets();
         initNewUserManagerUserListFiltering(AUM_USER_LIST_CONTAINER,AUM_USER_NAME,AUM_USER_ID,LIST_ITEMS_PER_PAGE);
         PageAssembler.openPopup(AUM_MODAL);         
      }
   };
   
   //revokes user manager privileges
   private void revokeUserManager() {
      String userId = DsUtil.getLabelText(RUM_USER_ID);
      DsESBApi.decalsRevokeUserRole(userId, UserRoles.USER_MANAGER_ROLE, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            umHelper.refreshUserManagerList();
            DsUtil.hideLabel(RUM_BUSY);
            DsUtil.showLabel(RUM_SUCCESS_CONTAINER);            
         }
         @Override
         public void onFailure(Throwable caught) {
            DsUtil.hideLabel(RUM_BUSY);
            DsUtil.handleFailedApiCall(caught);
         }
      });       
   }
   
   //revoke user manager submit listener
   protected EventCallback revokeUserManagerSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         DsUtil.showLabel(RUM_BUSY);   
         DsUtil.hideLabel(RUM_SUBMIT_BTN_CONTAINER);
         revokeUserManager();
      }
   }; 
   
   //show users listener
   protected EventCallback showUsersListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         toggleView(USERS_LINK_TEXT,USERS_CONTAINER);                  
      }
   };
   
   //show user managers listener
   protected EventCallback showUserManagersListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         toggleView(USER_MGR_LINK_TEXT,USER_MGRS_CONTAINER);         
      }
   };
   
   //show groups listener
   protected EventCallback showGroupsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (!groupsInitialized) {
            groupsInitialized = true;
            initializeGroupsView();            
         }
         toggleView(GROUPS_LINK_TEXT,GROUPS_CONTAINER);         
      }
   };
   
   //toggles the view
   private void toggleView(String navTextId, String contentContainerId) {
      DsUtil.setLabelAttribute(USERS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(USER_MGR_LINK_TEXT, "class", "");      
      DsUtil.setLabelAttribute(GROUPS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(navTextId, "class", "active");
      DsUtil.hideLabel(USERS_CONTAINER);
      DsUtil.hideLabel(USER_MGRS_CONTAINER);      
      DsUtil.hideLabel(GROUPS_CONTAINER);
      DsUtil.showLabel(contentContainerId);
   }
   
   //reset password click event listener
   private class ResetPasswordClickListener extends EventCallback {      
      private AppUser user;    
      public ResetPasswordClickListener(AppUser user) {
         this.user = user;
      }      
      @Override
      public void onEvent(Event event) {handleResetPassword(user);}
   }
   
   //delete user click event listener
   private class DeleteUserClickListener extends EventCallback {      
      private AppUser user;    
      public DeleteUserClickListener(AppUser user) {
         this.user = user;
      }      
      @Override
      public void onEvent(Event event) {handleDeleteUser(user);}
   }
   
   //register user widget event handlers
   private void registerUserWidgets() {
      for (String key:resetPasswordWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new ResetPasswordClickListener(resetPasswordWidgets.get(key)));
      }
      for (String key:deleteUserWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new DeleteUserClickListener(deleteUserWidgets.get(key)));
      }
   }
   
   //handle revoke user manager
   private void handleRevokeUserManager(AppUser userMgr) {
      DsUtil.setLabelText(RUM_NAME,userMgr.getFullName());
      DsUtil.setLabelText(RUM_USER_ID,userMgr.getUserId());
      DsUtil.hideLabel(RUM_SUCCESS_CONTAINER);
      DsUtil.showLabel(RUM_SUBMIT_BTN_CONTAINER);
      PageAssembler.openPopup(RUM_MODAL);
   }
   
   //revoke click event listener
   private class RevokeClickListener extends EventCallback {      
      private AppUser userMgr;    
      public RevokeClickListener(AppUser userMgr) {
         this.userMgr = userMgr;
      }      
      @Override
      public void onEvent(Event event) {handleRevokeUserManager(userMgr);}
   }
   
   //register revoke widget event handlers
   private void registerRevokeWidgets() {
      for (String key:revokeWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new RevokeClickListener(revokeWidgets.get(key)));
      }
   }
   
   //sets up the user list
   private void buildUsersDisplay() {
      resetPasswordWidgets.clear();
      deleteUserWidgets.clear();
      AdminUserMgmtViewBuilder.buildUserList(USERS_DETAILS_CONTAINER,umHelper.getUserList(),resetPasswordWidgets,deleteUserWidgets);
      initUserListFiltering(UM_USER_LIST_CONTAINER,UM_USER_NAME,UM_USER_EMAIL,USERS_PER_PAGE);
      registerUserWidgets();
   }
   
   //sets up the user manager list
   private void buildUserManagersDisplay() {
      revokeWidgets.clear();
      AdminUserMgmtViewBuilder.buildUserManagerList(USER_MGR_DETAILS_CONTAINER,umHelper.getUserManagerList(),revokeWidgets);
      initUserListFiltering(UM_USER_MGR_LIST_CONTAINER,UM_USER_MGR_NAME,UM_USER_MGR_EMAIL,LIST_ITEMS_PER_PAGE);
      registerRevokeWidgets();
   }
   
   //had to temporarily remove the pagination until I can figure out a way to manage the event handlers for paginated items
   
   //initializes user manager list filtering
   private final native String initUserManagerListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var userMgrOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var userManagerList = new $wnd.List(listContainer, userMgrOptions);      
   }-*/;
   
   //initializes new user manager user list filtering
   private final native String initNewUserManagerUserListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var newUserMgrUserOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var newUserMgrUserList = new $wnd.List(listContainer, newUserMgrUserOptions);      
   }-*/;
   
  //initializes delete user transfer user list filtering
   private final native String initDeleteUserTransferUserListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var deleteUserTransUserOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var deleteUserTransUserList = new $wnd.List(listContainer, deleteUserTransUserOptions);      
   }-*/;
   
   //initializes user list filtering
   private final native String initUserListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var userOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var userList = new $wnd.List(listContainer, userOptions);      
   }-*/;
   
   //initializes the group elements
   private void initGroupElements() {      
      grpNavContainer = PGRP_NAV_CONTAINER;
      emptyGroupMessageContainer = PGRP_NONE;   
      groupBusyContainer = PGRP_BUSY;
      currentGroupContainer = PGRP_CURRENT_CONTAINER;
      groupSelectionContainer = PGRP_SELECTIONS;
      moreGroupsSelectionContainer = PGRP_MORE_SELECTIONS_CONTAINER;
      moreGroupsSelectionLink = PGRP_MORE_SELECTIONS_LINK;
      lessGroupsSelectionContainer = PGRP_LESS_SELECTIONS_CONTAINER;
      lessGroupsSelectionLink = PGRP_LESS_SELECTIONS_LINK;     
   }

   @Override
   public void display() {
	  validateSession();
	  
      initGroupElements();
      instanceGroupType = GroupTypeEnum.PUBLIC;
      DsUserTabsHandler.getInstance().setAsNoTabsActive();
      PageAssembler.ready(new HTML(getTemplates().getUserManagementPanel().getText()));
      PageAssembler.buildContents();
      DsHeaderHandler dhh = new DsHeaderHandler(getDispatcher());
      dhh.setUpHeader(DsSession.getUser().getFirstName(), DsSession.getUser().getEmailAddress());
      PageAssembler.attachHandler(USERS_LINK,Event.ONCLICK,showUsersListener);
      PageAssembler.attachHandler(USER_MGR_LINK,Event.ONCLICK,showUserManagersListener);      
      PageAssembler.attachHandler(UM_NEW_USER,Event.ONCLICK,addNewUserListener);
      PageAssembler.attachHandler(ANU_FORM,DecalsScreen.VALID_EVENT,addNewUserSubmitListener);      
      PageAssembler.attachHandler(UM_NEW_USER_MGR,Event.ONCLICK,addNewUserManagerListener); 
      PageAssembler.attachHandler(AUM_CONFIRM_FORM,DecalsScreen.VALID_EVENT,addUserManagerConfirmSubmitListener);
      PageAssembler.attachHandler(RUM_FORM,DecalsScreen.VALID_EVENT,revokeUserManagerSubmitListener);
      PageAssembler.attachHandler(RP_FORM,DecalsScreen.VALID_EVENT,resetPasswordSubmitListener);
      PageAssembler.attachHandler(DU_DELETE_BTN,Event.ONCLICK,deleteUserListener);
      PageAssembler.attachHandler(DU_DELETE_ALL_BTN,Event.ONCLICK,deleteUserAndResourcesListener);
      PageAssembler.attachHandler(DU_DELETE_AND_TRNS_BTN,Event.ONCLICK,deleteUserTransferResourcesListener);
      PageAssembler.attachHandler(DUAR_CONFIRM_FORM,DecalsScreen.VALID_EVENT,deleteUserAndResourcesConfirmSubmitListener);      
      PageAssembler.attachHandler(DUAT_CONFIRM_FORM,DecalsScreen.VALID_EVENT,deleteUserTransferResourcesConfirmSubmitListener);
      PageAssembler.attachHandler(GROUPS_LINK,Event.ONCLICK,showGroupsListener);      
      attachGroupHandlers();
      umHelper.initForUserManagerUsage(this);
   }

   @Override
   public void lostFocus() {}

   @Override
   public void showContents() {
      DsUtil.hideLabel(UM_BUSY);
      buildUsersDisplay();
      buildUserManagersDisplay();      
   }

}
