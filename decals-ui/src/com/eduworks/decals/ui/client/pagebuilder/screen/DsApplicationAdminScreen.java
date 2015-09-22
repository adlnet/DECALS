package com.eduworks.decals.ui.client.pagebuilder.screen;

import java.util.HashMap;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.handler.DsHeaderHandler;
import com.eduworks.decals.ui.client.handler.DsUserTabsHandler;
import com.eduworks.decals.ui.client.handler.ViewHandler;
import com.eduworks.decals.ui.client.model.AdminUserMgmtHelper;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.ParadataPublicationInfo;
import com.eduworks.decals.ui.client.model.UserRoles;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.util.AdminUserMgmtViewBuilder;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.gwt.client.pagebuilder.modal.ModalDispatch;
import com.eduworks.gwt.client.pagebuilder.overlay.OverlayDispatch;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * Application administration screen.
 * 
 * @author Eduworks Corporation
 *
 */

public class DsApplicationAdminScreen extends DecalsScreen implements ViewHandler {
   
   private static final String ADMINISTRATORS_LINK = "daaAdministratorsLink";
   private static final String PARA_PUB_LINK = "daaParaPubLink";
   private static final String ADMINISTRATORS_LINK_TEXT = "daaAdministratorsLinkText";
   private static final String PARA_PUB_LINK_TEXT = "daaParaPubLinkText";
   
   private static final String ADMINISTRATORS_CONTAINER = "daaAdministratorsContainer";
   private static final String PARA_PUB_CONTAINER = "daaParadataPublishContainer";
   
   private static final String ADMIN_DETAILS_CONTAINER = "daaAdministratorsDetails";
   private static final String PARA_PUB_DETAILS_CONTAINER = "daaParadataPublishDetails";
   
   private static final String DAA_ADMIN_NAME = "daaAdminName";
   private static final String DAA_ADMIN_EMAIL = "daaAdminEmail";
   private static final String DAA_ADMIN_LIST_CONTAINER = "daaAministrators";
   
   private static final String DAA_PPUB_TITLE = "daaPpubTitle";
   private static final String DAA_PPUB_URL = "daaPpubUrl";
   private static final String DAA_PPUB_LIST_CONTAINER = "daaParadataPublishings";
   
   private static final String DAA_BUSY = "daaBusy";
   
   private static final String DAA_NEW_ADMIN = "daaAddNewAdmin";
   private static final String DAA_PUBLISH_ALL = "daaPPubAll";
   
   private static final String PPUB_ALL_MODAL = "modalPublishAllParadataConfirm";
   private static final String PPUB_ALL_SUBMIT = "papSubmit";
   private static final String PPUB_ALL_BUSY = "papBusy";
   private static final String PPUB_ALL_NODE_NAME = "papNodeName";
   private static final String PPUB_ALL_SUBMIT_BTN_CONTAINER = "papSubmitButtons";
   private static final String PPUB_ALL_SUCCESS_CONTAINER = "papSuccess";
   
   private static final String PPUB_RSRC_MODAL = "modalPublishResourceParadataConfirm";
   private static final String PPUB_RSRC_TITLE = "prpTitle";
   private static final String PPUB_RSRC_NODE_NAME = "prpNodeName";   
   private static final String PPUB_RSRC_FORM = "prpForm";
   private static final String PPUB_RSRC_RESOURCE_URL = "prpResourceUrl";
   private static final String PPUB_RSRC_TTL_HIDDEN = "prpTitleHidden";
   private static final String PPUB_RSRC_SUBMIT_BTN_CONTAINER = "prpSubmitButtons";
   private static final String PPUB_RSRC_SUBMIT = "prpSubmitBtn";
   private static final String PPUB_RSRC_BUSY = "prpBusy";
   private static final String PPUB_RSRC_SUCCESS_CONTAINER = "prpSuccess";
   private static final String PPUB_RSRC_SUCCESS_LR_ID = "prpSuccessLrId";
   private static final String PPUB_RSRC_FAIL_CONTAINER = "prpFail";
   private static final String PPUB_RSRC_ERROR_MSG = "prpErrorMessage";   
   private static final String PPUB_OK_KEY = "ok";
   private static final String PPUB_ID_KEY = "id";
   private static final String PPUB_MESSAGE_KEY = "message";
   private static final String PPUB_INVALID_RESPONSE_MESSAGE_PREFIX = "Invalid response from server: ";
   
   private static final String RA_MODAL = "modalRevokeAdminConfirm";
   private static final String RA_NAME = "revokeAdminName";
   private static final String RA_FORM = "revokeAdminForm";
   private static final String RA_USER_ID = "revokeAdminUserId";
   private static final String RA_BUSY = "revokeAdminBusy";   
   private static final String RA_SUBMIT_BTN_CONTAINER = "revokeAdminSubmitButtons";
   private static final String RA_SUCCESS_CONTAINER = "revokeAdminSuccess";   
   
   private static final String AA_MODAL = "modalAddAdmin";
   private static final String AA_INNER_CONTAINER = "addAdminUserListContainer";
   private static final String AA_USER_LIST_CONTAINER = "addAdminUserList";
   private static final String AA_USER_NAME = "addAdminUserName";
   private static final String AA_USER_ID = "addAdminUserId";
   private static final String AA_FINISHED_BTN = "addAdminFinishedButton";
   private static final String AA_SUCCESS_CONTAINER = "addAdminSuccess";
   private static final String AA_SUCCESS_NAME = "addAdminSuccessName";
   private static final String AA_BUSY = "addAdminBusy";
   private static final String AA_PICKLIST = "addAdminPickList";
   private static final String AA_CONFIRM = "addAdminConfirm";
   private static final String AA_CONFIRM_NAME = "addAdminConfirmName";
   private static final String AA_CONFIRM_NAME_HIDDEN = "addAdminConfirmNameHidden";   
   private static final String AA_CONFIRM_USER_ID = "addAdminConfirmUserId";
   private static final String AA_CONFIRM_FORM = "addAdminConfirmForm";      
   
   private static final int LIST_ITEMS_PER_PAGE = 5;
   
   private HashMap<String,ParadataPublicationInfo> publishWidgets = new HashMap<String,ParadataPublicationInfo>();
   private HashMap<String,AppUser> revokeWidgets = new HashMap<String,AppUser>();
   private HashMap<String,AppUser> newAdminWidgets = new HashMap<String,AppUser>();
   
   private AdminUserMgmtHelper aaHelper = new AdminUserMgmtHelper();   
   
   //handle post add new administrator
   private void handleAddNewAdminResults(JSONObject results) {      
      DsUtil.hideLabel(AA_BUSY);
      AppUser newAdmin = new AppUser(results);
      DsUtil.setLabelText(AA_SUCCESS_NAME, newAdmin.getFullName());
      DsUtil.showLabel(AA_SUCCESS_CONTAINER);
      aaHelper.addAdminToList(newAdmin);
      newAdminWidgets.clear();
      AdminUserMgmtViewBuilder.buildNewAdminUserList(AA_INNER_CONTAINER, aaHelper.getNonAdminUserList(),newAdminWidgets);
      registerAddNewAdminWidgets();
      initNewAdminUserListFiltering(AA_USER_LIST_CONTAINER,AA_USER_NAME,AA_USER_ID,LIST_ITEMS_PER_PAGE);
   }
   
   //add new administrator confirm submit listener
   protected EventCallback addAdminConfirmSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {     
         String userId = DsUtil.getLabelText(AA_CONFIRM_USER_ID);      
         DsUtil.hideLabel(AA_CONFIRM);
         DsUtil.showLabel(AA_PICKLIST);     
         DsUtil.hideLabel(AA_FINISHED_BTN);
         DsUtil.showLabel(AA_BUSY);
         DsESBApi.decalsAssignUserRole(userId, UserRoles.ADMIN_ROLE, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleAddNewAdminResults(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(AA_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });         
      }
   };  
   
   //handle add new administrator
   private void handleAddNewAdmin(AppUser admin) {
      DsUtil.setLabelText(AA_CONFIRM_NAME,admin.getFullName());
      DsUtil.setLabelText(AA_CONFIRM_NAME_HIDDEN,admin.getFullName());      
      DsUtil.setLabelText(AA_CONFIRM_USER_ID,admin.getUserId());
      DsUtil.hideLabel(AA_PICKLIST);
      DsUtil.hideLabel(AA_SUCCESS_CONTAINER);
      DsUtil.showLabel(AA_CONFIRM); 
   }
   
   //add new administrator event listener
   private class AddNewAdminClickListener extends EventCallback {      
      private AppUser admin;    
      public AddNewAdminClickListener(AppUser admin) {
         this.admin = admin;
      }      
      @Override
      public void onEvent(Event event) {handleAddNewAdmin(admin);}
   }
   
   //register add new administrator widget event handlers
   private void registerAddNewAdminWidgets() {
      for (String key:newAdminWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new AddNewAdminClickListener(newAdminWidgets.get(key)));
      }
   }
   
   //add new administrator listener
   protected EventCallback addNewAdminListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(AA_SUCCESS_CONTAINER);
         DsUtil.hideLabel(AA_CONFIRM);
         DsUtil.showLabel(AA_PICKLIST);         
         DsUtil.showLabel(AA_FINISHED_BTN);
         newAdminWidgets.clear();
         AdminUserMgmtViewBuilder.buildNewAdminUserList(AA_INNER_CONTAINER, aaHelper.getNonAdminUserList(),newAdminWidgets);
         registerAddNewAdminWidgets();
         initNewAdminUserListFiltering(AA_USER_LIST_CONTAINER,AA_USER_NAME,AA_USER_ID,LIST_ITEMS_PER_PAGE);
         PageAssembler.openPopup(AA_MODAL);         
      }
   };
      
   //revokes administrator privileges
   private void revokeAdmin() {
      String userId = DsUtil.getLabelText(RA_USER_ID);
      DsESBApi.decalsRevokeUserRole(userId, UserRoles.ADMIN_ROLE, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            aaHelper.refreshAdminList();
            DsUtil.hideLabel(RA_BUSY);
            DsUtil.showLabel(RA_SUCCESS_CONTAINER);            
         }
         @Override
         public void onFailure(Throwable caught) {
            DsUtil.hideLabel(RA_BUSY);
            DsUtil.handleFailedApiCall(caught);
         }
      });       
   }
   
   //revoke administrator submit listener
   protected EventCallback revokeAdminSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         DsUtil.showLabel(RA_BUSY);   
         DsUtil.hideLabel(RA_SUBMIT_BTN_CONTAINER);
         revokeAdmin();
      }
   };    
   
   //handles publish resource paradata response
   private void handlePublishResourceParadataResponse(JSONObject response) {
      DsUtil.hideLabel(PPUB_RSRC_BUSY);
      if (response.containsKey(PPUB_OK_KEY) && "true".equalsIgnoreCase(response.get(PPUB_OK_KEY).isString().stringValue())) {
         DsUtil.setLabelText(PPUB_RSRC_SUCCESS_LR_ID,response.get(PPUB_ID_KEY).isString().stringValue());
         DsUtil.showLabel(PPUB_RSRC_SUCCESS_CONTAINER);         
         aaHelper.refreshParaPubInfoList();        
      }
      else if (response.containsKey(PPUB_OK_KEY) && "false".equalsIgnoreCase(response.get(PPUB_OK_KEY).isString().stringValue())) {
         DsUtil.setLabelText(PPUB_RSRC_ERROR_MSG,response.get(PPUB_MESSAGE_KEY).isString().stringValue());
         DsUtil.showLabel(PPUB_RSRC_FAIL_CONTAINER);
      }
      else {
         DsUtil.setLabelText(PPUB_RSRC_ERROR_MSG,PPUB_INVALID_RESPONSE_MESSAGE_PREFIX + response.toString());
         DsUtil.showLabel(PPUB_RSRC_FAIL_CONTAINER);
      }
   }
   
   //publishes resource paradata to the learning registry
   private void publishResourceParadata() {
      String resourceUrl = DsUtil.getLabelText(PPUB_RSRC_RESOURCE_URL);
      DsESBApi.decalsPublishResourceParadata(resourceUrl,new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            handlePublishResourceParadataResponse(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());            
         }
         @Override
         public void onFailure(Throwable caught) {
            DsUtil.hideLabel(PPUB_RSRC_BUSY);
            DsUtil.handleFailedApiCall(caught);
         }
      });       
   }
   
   //publish resource paradata submit listener
   protected EventCallback publishResourceParadataSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         DsUtil.showLabel(PPUB_RSRC_BUSY);   
         DsUtil.hideLabel(PPUB_RSRC_SUBMIT_BTN_CONTAINER);
         publishResourceParadata();
      }
   };   
   
   //handle publish resource paradata
   private void handleParadataPublish(ParadataPublicationInfo ppi) {
      DsUtil.setLabelText(PPUB_RSRC_NODE_NAME,DsSession.getInstance().getLrPublishNode());
      DsUtil.setLabelText(PPUB_RSRC_TITLE,ppi.getResourceTitle());
      DsUtil.setLabelText(PPUB_RSRC_TTL_HIDDEN,ppi.getResourceTitle());
      DsUtil.setLabelText(PPUB_RSRC_RESOURCE_URL,ppi.getResourceUrl());
      DsUtil.hideLabel(PPUB_RSRC_SUCCESS_CONTAINER);
      DsUtil.hideLabel(PPUB_RSRC_FAIL_CONTAINER);
      DsUtil.showLabel(PPUB_RSRC_SUBMIT_BTN_CONTAINER);
      PageAssembler.openPopup(PPUB_RSRC_MODAL);
   }
   
   //handle revoke administrator privileges
   private void handleRevokeAdmin(AppUser admin) {
      DsUtil.setLabelText(RA_NAME,admin.getFullName());
      DsUtil.setLabelText(RA_USER_ID,admin.getUserId());
      DsUtil.hideLabel(RA_SUCCESS_CONTAINER);
      DsUtil.showLabel(RA_SUBMIT_BTN_CONTAINER);
      PageAssembler.openPopup(RA_MODAL);
   }

   //show administrators listener
   protected EventCallback showAdministratorsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         toggleView(ADMINISTRATORS_LINK_TEXT,ADMINISTRATORS_CONTAINER);                  
      }
   };
   
   //show paradata publishing listener
   protected EventCallback showParaPubListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         toggleView(PARA_PUB_LINK_TEXT,PARA_PUB_CONTAINER);         
      }
   };
   
   //toggles the view
   private void toggleView(String navTextId, String contentContainerId) {
      DsUtil.setLabelAttribute(ADMINISTRATORS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(PARA_PUB_LINK_TEXT, "class", "");      
      DsUtil.setLabelAttribute(navTextId, "class", "active");
      DsUtil.hideLabel(ADMINISTRATORS_CONTAINER);
      DsUtil.hideLabel(PARA_PUB_CONTAINER);      
      DsUtil.showLabel(contentContainerId);
   }
   
   //publishes all paradata to the learning registry
   private void publishAllParadata() {
      DsESBApi.decalsPublishAllResourceParadata(new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            aaHelper.refreshParaPubInfoList(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
            DsUtil.hideLabel(PPUB_ALL_BUSY);
            DsUtil.showLabel(PPUB_ALL_SUCCESS_CONTAINER);
         }
         @Override
         public void onFailure(Throwable caught) {
            DsUtil.hideLabel(PPUB_ALL_BUSY);
            DsUtil.handleFailedApiCall(caught);
         }
      });       
   }
   
   //publish all paradata submit listener
   protected EventCallback publishAllParadataSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         DsUtil.showLabel(PPUB_ALL_BUSY);   
         DsUtil.hideLabel(PPUB_ALL_SUBMIT_BTN_CONTAINER);
         publishAllParadata();
      }
   };
   
   //publish all paradata listener
   protected EventCallback publishAllParadataListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.setLabelText(PPUB_ALL_NODE_NAME,DsSession.getInstance().getLrPublishNode());
         DsUtil.hideLabel(PPUB_ALL_SUCCESS_CONTAINER);
         DsUtil.showLabel(PPUB_ALL_SUBMIT_BTN_CONTAINER);         
         PageAssembler.openPopup(PPUB_ALL_MODAL);
      }
   };
   
   //revoke click event listener
   private class RevokeClickListener extends EventCallback {      
      private AppUser admin;    
      public RevokeClickListener(AppUser admin) {
         this.admin = admin;
      }      
      @Override
      public void onEvent(Event event) {handleRevokeAdmin(admin);}
   }
   
   //publish click event listener
   private class ParadataPublishClickListener extends EventCallback {      
      private ParadataPublicationInfo ppi;    
      public ParadataPublishClickListener(ParadataPublicationInfo ppi) {
         this.ppi = ppi;
      }      
      @Override
      public void onEvent(Event event) {handleParadataPublish(ppi);}
   }
   
   
   //register revoke widget event handlers
   private void registerRevokeWidgets() {
      for (String key:revokeWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new RevokeClickListener(revokeWidgets.get(key)));
      }
   }
   
   //sets up the administrator list
   private void buildAdministratorsDisplay() {
      revokeWidgets.clear();
      AdminUserMgmtViewBuilder.buildAdministratorList(ADMIN_DETAILS_CONTAINER,aaHelper.getAdminList(),revokeWidgets);
      initAdminListFiltering(DAA_ADMIN_LIST_CONTAINER,DAA_ADMIN_NAME,DAA_ADMIN_EMAIL,LIST_ITEMS_PER_PAGE);
      registerRevokeWidgets();
   }
   
   //register publish widget event handlers
   private void registerPublishWidgets() {
      for (String key:publishWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new ParadataPublishClickListener(publishWidgets.get(key)));
      }
   }
   
   //sets up the paradata publishing info list 
   private void buildParadataPublishingInfoDisplay() {
      publishWidgets.clear();
      AdminUserMgmtViewBuilder.buildParadataPublishingInfoList(PARA_PUB_DETAILS_CONTAINER,aaHelper.getParaPubInfoList(),publishWidgets);
      initPPubListFiltering(DAA_PPUB_LIST_CONTAINER,DAA_PPUB_TITLE,DAA_PPUB_URL,LIST_ITEMS_PER_PAGE);
      registerPublishWidgets();
   }
   
   //had to temporarily remove the pagination until I can figure out a way to manage the event handlers for paginated items
   
   //initializes administrator list filtering
   private final native String initAdminListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var adminOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var adminList = new $wnd.List(listContainer, adminOptions);      
   }-*/;
   
   //initializes new administrator user list filtering
   private final native String initNewAdminUserListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var newAdminUserOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var newAdminUserList = new $wnd.List(listContainer, newAdminUserOptions);      
   }-*/;
   
   //initializes paradata publishing list filtering
   private final native String initPPubListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var pPubOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var pPubList = new $wnd.List(listContainer, pPubOptions);      
   }-*/;
   
   @Override
   public void display() {
      DsUserTabsHandler.getInstance().setAsNoTabsActive();
      PageAssembler.ready(new HTML(getTemplates().getApplicationAdminPanel().getText()));
      PageAssembler.buildContents();
      DsHeaderHandler dhh = new DsHeaderHandler(getDispatcher());
      dhh.setUpHeader(DsSession.getUser().getFirstName(), DsSession.getUser().getEmailAddress());
      PageAssembler.attachHandler(ADMINISTRATORS_LINK,Event.ONCLICK,showAdministratorsListener);
      PageAssembler.attachHandler(PARA_PUB_LINK,Event.ONCLICK,showParaPubListener);
      PageAssembler.attachHandler(DAA_PUBLISH_ALL,Event.ONCLICK,publishAllParadataListener);
      PageAssembler.attachHandler(DAA_NEW_ADMIN,Event.ONCLICK,addNewAdminListener);      
      PageAssembler.attachHandler(PPUB_ALL_SUBMIT,Event.ONCLICK,publishAllParadataSubmitListener);
      PageAssembler.attachHandler(PPUB_RSRC_FORM,DecalsScreen.VALID_EVENT,publishResourceParadataSubmitListener);
      PageAssembler.attachHandler(RA_FORM,DecalsScreen.VALID_EVENT,revokeAdminSubmitListener);
      PageAssembler.attachHandler(AA_CONFIRM_FORM,DecalsScreen.VALID_EVENT,addAdminConfirmSubmitListener);
      aaHelper.initForAdministratorUsage(this);      
   }

   @Override
   public void lostFocus() {}

   @Override
   public void showContents() {
      DsUtil.hideLabel(DAA_BUSY);      
      buildAdministratorsDisplay();
      buildParadataPublishingInfoDisplay();
   }
}
