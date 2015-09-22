package com.eduworks.decals.ui.client.handler;

import com.eduworks.decals.ui.client.DsScreenDispatch;
import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.model.UserRoles;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.Event;

/**
 * Attach handlers for the elements on the DECALS Header
 * 
 * Handlers can be attached as needed or all together using {@link #setUpHeader(String)}.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsHeaderHandler {
   
   private DsScreenDispatch screenDispatch = null;
   
   private static final String HEADER_LOGOUT = "headerLogout";
   private static final String HEADER_USERNAME = "headerUsername";   
   private static final String HEADER_ADMIN = "headerAdmin";
   private static final String HEADER_ADMIN_DIVIDER = "headerAdminDivider";
   private static final String HEADER_USER_MGMT = "headerUserMgmt";
   private static final String HEADER_USER_MGMT_DIVIDER = "headerUserMgmtDivider";
   private static final String HEADER_USER_PREFS = "headerUserPrefs";
   private static final String HEADER_USER_PREFS_DIVIDER = "headerUserPrefsDivider";
   
   private static final String SEARCH_HEADER_USERNAME = "searchHeaderUsername";
   private static final String HEADER_USERID = "headerUserId";
   
   /**
    * DsHeaderHandler constructor
    * 
    * @param screenDispatch The screenDispatch to associate with this handler
    */
   public DsHeaderHandler(DsScreenDispatch screenDispatch) {
      this.screenDispatch = screenDispatch;
   }
   
   /**
    * Listener for logout clicks
    */
   protected EventCallback logoutListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         //DsSession.setCachedLrSearchHandler(null);
         DsSession.getInstance().setSessionState(DsSession.SessionState.LOGGING_OUT);        
         DsESBApi.decalsLogout(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {screenDispatch.loadGuestScreen();}
            @Override
            public void onFailure(Throwable caught) {screenDispatch.loadGuestScreen();}
            });
      }
   };
   
   protected EventCallback adminClickListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         screenDispatch.loadApplicationAdminScreen();
      }     
   };
   
   protected EventCallback userManagementClickListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         screenDispatch.loadUserManagementScreen();
      }     
   };
   
   protected EventCallback userPreferencesClickListener = new EventCallback() {
	      @Override
	      public void onEvent(Event event) {
	         screenDispatch.loadUserPreferencesScreen();
	         DsUserTabsHandler.getInstance().setAsNoTabsActive();
	      }     
	   };
   
   //set up the application administration links
   private void setUpAdminLinks() {
      if (DsSession.getUser().hasRole(UserRoles.ADMIN_ROLE)) {
         DsUtil.setAnchorStyle(HEADER_ADMIN,"display:inline");
         DsUtil.setLabelAttribute(HEADER_ADMIN_DIVIDER, "style","display:inline");
         PageAssembler.attachHandler(HEADER_ADMIN, Event.ONCLICK, adminClickListener);
      }
      else {
         DsUtil.hideAnchor(HEADER_ADMIN);
         DsUtil.hideLabel(HEADER_ADMIN_DIVIDER);
      }
   }
   
   //set up the user management links
   private void setUpUserManagementLinks() {
      if (DsSession.getUser().hasRole(UserRoles.USER_MANAGER_ROLE) || DsSession.getUser().hasRole(UserRoles.ADMIN_ROLE)) {
         DsUtil.setAnchorStyle(HEADER_USER_MGMT,"display:inline");
         DsUtil.setLabelAttribute(HEADER_USER_MGMT_DIVIDER, "style","display:inline");
         PageAssembler.attachHandler(HEADER_USER_MGMT, Event.ONCLICK, userManagementClickListener);
      }
      else {
         DsUtil.hideAnchor(HEADER_USER_MGMT);
         DsUtil.hideLabel(HEADER_USER_MGMT_DIVIDER);
      }
   }
   
   //sets up the application administration and user management
   private void setUpAdvancedPermissions() {
      setUpAdminLinks();
      setUpUserManagementLinks();
   }
   
   /**
    * Attach the logout listener
    */
   public void attachUserPreferencesHandler() {PageAssembler.attachHandler(HEADER_USER_PREFS, Event.ONCLICK, userPreferencesClickListener);}
   
   /**
    * Attach the logout listener
    */
   public void attachLogoutHandler() {PageAssembler.attachHandler(HEADER_LOGOUT, Event.ONCLICK, logoutListener);}
   
   /**
    * Attach the user name to the page header
    * 
    * @param headerUsernameText The user name to attach
    */
   public void attachUsernameToHeader(String headerUsernameText) {
      DsUtil.setLabelText(HEADER_USERNAME, headerUsernameText);     
      DsUtil.setLabelText(SEARCH_HEADER_USERNAME, headerUsernameText);
   }
   
   /**
    * Set up all header options
    * 
    * @param headerUsernameText The user name to attach to the header
    */
   public void setUpHeader(String headerUsernameText, String headerUserId) {
      attachLogoutHandler();
      attachUserPreferencesHandler();
      attachUsernameToHeader(headerUsernameText);
      setUpAdvancedPermissions();
      
      attachUserId(headerUserId);
   }
   
   public void attachUserId(String userId){
	   DsUtil.setLabelText(HEADER_USERID, userId);
   }

}
