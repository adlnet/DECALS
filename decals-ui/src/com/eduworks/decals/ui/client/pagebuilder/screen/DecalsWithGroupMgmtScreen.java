package com.eduworks.decals.ui.client.pagebuilder.screen;

import java.util.ArrayList;
import java.util.HashMap;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.Group;
import com.eduworks.decals.ui.client.model.GroupManager;
import com.eduworks.decals.ui.client.model.GroupType;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.decals.ui.client.util.GroupsViewBuilder;
import com.eduworks.decals.ui.client.util.DsUtil.NavMode;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Event;

/**
 * Used for screens that have group management (user home screen and user management screen)
 * 
 * @author Eduworks Corporation
 *
 */
public abstract class DecalsWithGroupMgmtScreen extends DecalsScreen {
   
   protected String grpNavContainer;
   protected String emptyGroupMessageContainer;   
   protected String groupBusyContainer;
   protected String currentGroupContainer;
   protected String groupSelectionContainer;
   protected String moreGroupsSelectionContainer;
   protected String moreGroupsSelectionLink;
   protected String lessGroupsSelectionContainer;
   protected String lessGroupsSelectionLink;
   
   private static final String CGRP_CUR_GRP_NAME = "curGroupName";
   private static final String CGRP_ADD_USER_LINK = "curGrpAddUserLink";
   private static final String CGRP_DELETE_LINK = "curGrpDeleteLink";
   
   private static final String CGRP_USER_NAME = "cgrpUserName";
   private static final String CGRP_USER_EMAIL = "cgrpUserEmail";
   private static final String CGRP_USER_LIST_CONTAINER = "cgrpUsers";
   private static final String CGRP_DETAILS_CONTAINER = "currentGroupDetailsContainer";
   private static final String CGRP_USER_COUNT = "currentGroupUserCount";
      
   private static final String AG_MODAL = "modalAddGroup";
   private static final String AG_FORM = "addGroupForm";
   private static final String AG_NAME = "addGroupName";
   private static final String AG_SUBMIT_BTNS = "addGroupSubmitButtons";
   private static final String AG_BUSY = "addGroupBusy";
   private static final String AG_SUCCESS = "addGroupSuccess";
   
   private static final String DG_CONFIRM_MODAL = "modalDeleteGroupConfirm";
   private static final String DG_FORM = "deleteGroupForm";
   private static final String DG_NAME = "deleteGroupName";
   private static final String DG_GRP_ID = "deleteGroupId";
   private static final String DG_SUBMIT_BTNS = "deleteGroupSubmitButtons";
   private static final String DG_BUSY = "deleteGroupBusy";
   private static final String DG_SUCCESS = "deleteGroupSuccess";
   
   private static final String DGU_MODAL = "modalDeleteGroupUserConfirm";
   private static final String DGU_USER_NAME = "delGroupUserUserName";
   private static final String DGU_GRP_NAME = "delGroupUserGroupName";
   private static final String DGU_FORM = "delGroupUserForm";
   private static final String DGU_USER_ID = "delGroupUserUserId";
   private static final String DGU_GRP_ID = "delGroupUserGroupId";
   private static final String DGU_BUSY = "delGroupUserBusy";
   private static final String DGU_SUBMIT_BTNS = "delGroupUserSubmitButtons";
   
   private static final String AGU_MODAL = "modalAddGroupUser";
   private static final String AGU_GRP_NAME = "addUserGroupName";
   private static final String AGU_PICKLIST = "addGroupUserPickList";
   private static final String AGU_INNER_CONTAINER = "addGroupUserListContainer";
   private static final String AGU_USER_LIST_CONTAINER = "addGroupUserList";
   private static final String AGU_USER_NAME = "aguUserName";
   private static final String AGU_USER_ID = "aguUserId";
   private static final String AGU_OK_BTN = "addGroupUserOkBtn";
   private static final String AGU_CONFIRM = "addGroupUserConfirm";
   private static final String AGU_SELECT_COUNT = "addGroupUserCount";
   private static final String AGU_ADD_BTN = "addGroupUserAddBtn";   
   private static final String AGU_SUBMIT_BTNS = "addGroupUserSubmitBtns";
   private static final String AGU_BUSY = "addGroupUserBusy";
   
   private static final int NUMBER_OF_INITIAL_SELECTIONS_SHOWN = 3;
   private static final int LIST_ITEMS_PER_PAGE = 5;
   
   protected enum GroupTypeEnum{PUBLIC,PRIVATE}
   
   protected GroupTypeEnum instanceGroupType = GroupTypeEnum.PRIVATE;
   protected NavMode currentGroupNavMode = NavMode.LESS;   
   protected Group currentGroup;
   protected GroupManager groupManager = new GroupManager();
   
   protected HashMap<String,Group> groupSelectionWidgets = new HashMap<String,Group>();
   protected HashMap<String,AppUser> newGroupUserWidgets = new HashMap<String,AppUser>();
   protected HashMap<String,AppUser> groupUserDeleteWidgets = new HashMap<String,AppUser>();
   
   protected ArrayList<AppUser>fullUserList  = new ArrayList<AppUser>();
   protected HashMap<String,AppUser> newGroupUsers = new HashMap<String,AppUser>();
   
   //initializes new group user list filtering
   private final native String initNewGroupUserListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var newGroupUserOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var newGroupUserList = new $wnd.List(listContainer, newGroupUserOptions);      
   }-*/;
   
   //initializes group user list filtering
   protected final native String initGroupUserListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var groupUserOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var groupUserList = new $wnd.List(listContainer, groupUserOptions);      
   }-*/;
   
   //handle delete group user
   protected void handleDeleteGroupUser(AppUser au) {
      DsUtil.setLabelText(DGU_GRP_NAME,currentGroup.getName());
      DsUtil.setLabelText(DGU_USER_NAME,au.getFullName());
      DsUtil.setLabelText(DGU_USER_ID,au.getUserId());      
      DsUtil.setLabelText(DGU_GRP_ID,currentGroup.getGroupId());
      DsUtil.hideLabel(DGU_BUSY);
      DsUtil.showLabel(DGU_SUBMIT_BTNS);
      PageAssembler.openPopup(DGU_MODAL); 
   }
   
   //delete collection user click event listener
   protected class DeleteGroupUserClickListener extends EventCallback {      
      private AppUser au;    
      public DeleteGroupUserClickListener(AppUser au) {
         this.au = au;
      }      
      @Override
      public void onEvent(Event event) {handleDeleteGroupUser(au);}
   }
   
   //register group user delete widget event handlers
   protected void registerGroupUserDeleteWidgets() {
      for (String key:groupUserDeleteWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new DeleteGroupUserClickListener(groupUserDeleteWidgets.get(key)));
      }
   }    
   
   //builds the current group view
   protected void buildCurrentGroupView() {
      DsUtil.setLabelText(CGRP_CUR_GRP_NAME, currentGroup.getName());
      DsUtil.setLabelText(CGRP_USER_COUNT, "(" + String.valueOf(currentGroup.getNumberofUsers()) + ")");
      groupUserDeleteWidgets.clear();
      GroupsViewBuilder.populateGroupData(CGRP_DETAILS_CONTAINER, currentGroup, groupUserDeleteWidgets);
      registerGroupUserDeleteWidgets();
      initGroupUserListFiltering(CGRP_USER_LIST_CONTAINER,CGRP_USER_NAME,CGRP_USER_EMAIL,LIST_ITEMS_PER_PAGE);   
   }
   
   //group selection event listener
   protected class SelectGroupClickListener extends EventCallback {      
      private Group grp;    
      public SelectGroupClickListener(Group grp) {
         this.grp = grp;
      }      
      @Override
      public void onEvent(Event event) {  
         currentGroup = grp;
         buildCurrentGroupView();
      }
   }
   
   //registers the group selection widgets
   protected void registerGroupSelectionWidgets() {
      for (String key:groupSelectionWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new SelectGroupClickListener(groupSelectionWidgets.get(key)));
      }
   }
   
   //builds the group navigation
   protected void buildGroupNavigation() {
      int numToShow = NUMBER_OF_INITIAL_SELECTIONS_SHOWN;
      if (NavMode.MORE.equals(currentGroupNavMode)) numToShow = GroupsViewBuilder.UNLIMITED;
      groupSelectionWidgets.clear();      
      GroupsViewBuilder.buildGroupNavigation(grpNavContainer,groupSelectionContainer,moreGroupsSelectionContainer,lessGroupsSelectionContainer,
            groupManager.getGroupList(),numToShow,groupSelectionWidgets);
      registerGroupSelectionWidgets();
   }
   
   //builds the group view
   protected void buildGroupsView() {
      DsUtil.hideLabel(groupBusyContainer);
      buildGroupNavigation();
      if (groupManager.getNumberOfGroups() == 0) {
         DsUtil.hideLabel(currentGroupContainer);
         DsUtil.showLabel(emptyGroupMessageContainer);
      }
      else {
         currentGroup = groupManager.getGroupList().get(0);
         buildCurrentGroupView();
         DsUtil.hideLabel(emptyGroupMessageContainer);         
         DsUtil.showLabel(currentGroupContainer);
      }
   }
   
   //initializes the groups view
   protected void initializeGroupsView() {
      DsUtil.hideLabel(currentGroupContainer);
      DsUtil.showLabel(groupBusyContainer);      
      if (GroupTypeEnum.PUBLIC.equals(instanceGroupType)) {
         DsESBApi.decalsGetPublicGroups(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               groupManager.initGroupList(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
               buildGroupsView();
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
         });
      }
      else {
         DsESBApi.decalsGetUserPrivateGroups(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               groupManager.initGroupList(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
               buildGroupsView();
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
         });
      }
   }
   
   //show all groups navigation listener
   protected EventCallback showAllGroupsNavListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         currentGroupNavMode = NavMode.MORE;
         buildGroupNavigation();
      }
   };
   
   //show less groups navigation listener
   protected EventCallback showLessGroupsNavListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         currentGroupNavMode = NavMode.LESS;
         buildGroupNavigation();
      }
   };
   
   //handle add group response
   protected void handleAddGroupResponse(JSONObject groupRes) {
      groupManager.addGroup(new Group(groupRes));
      buildGroupsView();
      DsUtil.hideLabel(AG_BUSY);
      DsUtil.showLabel(AG_SUBMIT_BTNS);
      //DsUtil.showLabel(AG_SUCCESS);
      PageAssembler.closePopup(AG_MODAL);
   }
   
   //returns the appropriate group type for an add new group
   private String getCreateGroupType() {
      if (GroupTypeEnum.PUBLIC.equals(instanceGroupType)) return GroupType.PUBLIC_TYPE;
      else return GroupType.PRIVATE_TYPE;      
   }
   
   //add group handler
   protected EventCallback addGroupHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         String name = DsUtil.getTextBoxText(AG_NAME);
         name = groupManager.generateUniqueGroupName(name);
         DsUtil.hideLabel(AG_SUBMIT_BTNS);
         DsUtil.showLabel(AG_BUSY);
         DsESBApi.decalsCreateGroup(name, getCreateGroupType(), new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleAddGroupResponse(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(AG_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });
      }
   };
   
   //handle delete group response
   protected void handleDeleteGroupResponse(String groupId) {
      groupManager.removeGroup(groupId);
      buildGroupsView();
      DsUtil.hideLabel(DG_BUSY);   
      DsUtil.showLabel(DG_SUCCESS);   
      DsSession.getUserCollectionManager().removeGroupFromAllCollections(groupId);
   }
   
   //delete group handler
   protected EventCallback deleteGroupHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         final String groupId = DsUtil.getLabelText(DG_GRP_ID);
         DsUtil.hideLabel(DG_SUBMIT_BTNS);
         DsUtil.showLabel(DG_BUSY);
         DsESBApi.decalsDeleteGroup(groupId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleDeleteGroupResponse(groupId);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(DG_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });
      }
   };   
   
   //handle delete group user response
   protected void handleDeleteGroupUserResponse(String groupId, String userId) {
      groupManager.removeGroupUser(groupId, userId);
      buildCurrentGroupView();
      DsUtil.hideLabel(DGU_BUSY);   
      DsUtil.showLabel(DGU_SUBMIT_BTNS);
      PageAssembler.closePopup(DGU_MODAL);
   }
   
   //delete collection user submit handler
   protected EventCallback deleteGroupUserSubmitHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {  
         DsUtil.hideLabel(DGU_SUBMIT_BTNS);
         DsUtil.showLabel(DGU_BUSY);
         final String userId = DsUtil.getLabelText(DGU_USER_ID);
         final String groupId = DsUtil.getLabelText(DGU_GRP_ID);
         DsESBApi.decalsRemoveGroupUser(groupId, userId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleDeleteGroupUserResponse(groupId,userId);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(DGU_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });         
      }
   };
   
   //delete group click handler
   protected EventCallback deleteGroupClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) { 
         DsUtil.setLabelText(DG_NAME,currentGroup.getName());
         DsUtil.setLabelText(DG_GRP_ID,currentGroup.getGroupId());
         DsUtil.hideLabel(DG_SUCCESS);
         DsUtil.showLabel(DG_SUBMIT_BTNS);
         PageAssembler.openPopup(DG_CONFIRM_MODAL);
      }
   };
   
   //handle add new collection user
   protected void handleAddNewGroupUser(AppUser gu) {
      if (newGroupUsers.containsKey(gu.getUserId())) newGroupUsers.remove(gu.getUserId());
      else newGroupUsers.put(gu.getUserId(),gu);
   }
   
   //add new collection user event listener
   protected class AddGroupUserClickListener extends EventCallback {      
      private AppUser gu;    
      public AddGroupUserClickListener(AppUser gu) {
         this.gu = gu;
      }      
      @Override
      public void onEvent(Event event) {handleAddNewGroupUser(gu);}
   }
   
   //register new collection user widget event handlers
   protected void registerNewGroupUserWidgets() {
      for (String key:newGroupUserWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new AddGroupUserClickListener(newGroupUserWidgets.get(key)));
      }
   }
   
   //sets up the add group user modal
   protected void setUpAddGroupUserModal() {
      newGroupUsers.clear();
      DsUtil.hideLabel(AGU_BUSY);
      DsUtil.showLabel(AGU_SUBMIT_BTNS);
      DsUtil.hideLabel(AGU_CONFIRM);
      DsUtil.showLabel(AGU_PICKLIST);   
      DsUtil.setLabelText(AGU_GRP_NAME,currentGroup.getName());
      newGroupUserWidgets.clear();
      ArrayList<AppUser> selectList = groupManager.removeGroupUsersFromUserList(currentGroup.getGroupId(),fullUserList);
      GroupsViewBuilder.buildNewGroupUserList(AGU_INNER_CONTAINER,selectList,newGroupUserWidgets);
      registerNewGroupUserWidgets();
      initNewGroupUserListFiltering(AGU_USER_LIST_CONTAINER,AGU_USER_NAME,AGU_USER_ID,LIST_ITEMS_PER_PAGE);
      PageAssembler.openPopup(AGU_MODAL);      
   }
   
   //add group user click handler
   protected EventCallback addGroupUserClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         DsESBApi.decalsGetUserList(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               DsUtil.buildUserTypeListFromReturn(fullUserList,result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
               setUpAddGroupUserModal();
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
         });   
      }
   };
   
   //add group user OK click handler
   protected EventCallback addGroupUserOkClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         if (newGroupUsers.size() == 0) PageAssembler.closePopup(AGU_MODAL); 
         else {
            DsUtil.setLabelText(AGU_SELECT_COUNT, String.valueOf(newGroupUsers.size()));
            DsUtil.hideLabel(AGU_PICKLIST);
            DsUtil.showLabel(AGU_CONFIRM);            
         }
      }
   };
   
   //builds a JSON array from the new group users hash map values
   protected JSONArray getNewGroupUsersArray() {
      JSONArray ja = new JSONArray();
      JSONObject jo;
      AppUser au;
      int count = 0;
      for (String s:newGroupUsers.keySet()) {
         au = newGroupUsers.get(s);
         jo = new JSONObject();
         jo.put(AppUser.FIRST_NAME_KEY,new JSONString(au.getFirstName()));
         jo.put(AppUser.LAST_NAME_KEY,new JSONString(au.getLastName()));
         jo.put(AppUser.USERID_KEY,new JSONString(au.getUserId()));
         ja.set(count,jo);
         count++;            
      }     
      return ja;
   }
     
   //add group user Add click handler
   protected EventCallback addGroupUserAddClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(AGU_SUBMIT_BTNS);
         DsUtil.showLabel(AGU_BUSY);         
         DsESBApi.decalsAddGroupUsers(currentGroup.getGroupId(), getNewGroupUsersArray(), new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               initializeGroupsView();
               PageAssembler.closePopup(AGU_MODAL);
            }
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(AGU_BUSY);       
               DsUtil.handleFailedApiCall(caught);
            }
         });   
      }
   };
   
   /**
    * Attaches group handlers.
    */
   protected void attachGroupHandlers() {  
      PageAssembler.attachHandler(moreGroupsSelectionLink,Event.ONCLICK,showAllGroupsNavListener);      
      PageAssembler.attachHandler(lessGroupsSelectionLink,Event.ONCLICK,showLessGroupsNavListener);      
      PageAssembler.attachHandler(AG_FORM,VALID_EVENT,addGroupHandler);
      PageAssembler.attachHandler(CGRP_DELETE_LINK,Event.ONCLICK,deleteGroupClickHandler);
      PageAssembler.attachHandler(DG_FORM,VALID_EVENT,deleteGroupHandler);
      PageAssembler.attachHandler(DGU_FORM,VALID_EVENT,deleteGroupUserSubmitHandler);
      PageAssembler.attachHandler(CGRP_ADD_USER_LINK,Event.ONCLICK,addGroupUserClickHandler);
      PageAssembler.attachHandler(AGU_OK_BTN,Event.ONCLICK,addGroupUserOkClickHandler);
      PageAssembler.attachHandler(AGU_ADD_BTN,Event.ONCLICK,addGroupUserAddClickHandler);
   }
   
  

}
