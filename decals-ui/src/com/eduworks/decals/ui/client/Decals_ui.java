package com.eduworks.decals.ui.client;

import java.security.interfaces.DSAKey;

import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.pagebuilder.DsHtmlTemplates;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsGuestScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsUserHomeScreen;
import com.eduworks.gwt.client.component.AppEntry;
import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Decals_ui extends AppEntry implements EntryPoint, ValueChangeHandler<String> {
   
   private static final String IS_TN_ROOT_PROP = "int.search.thumbnail.root";
   private static final String COMPETENCY_MANAGER_PROP = "competency.url";
   
   @Override
	public void onModuleLoad() {
      dispatcher = new DsScreenDispatch();
		defaultScreen = new DsGuestScreen();
		templates = GWT.create(DsHtmlTemplates.class);
		
		//parse DECALS properties
		CommunicationHub.sendHTTP(CommunicationHub.GET, DEFAULT_INSTALLATION_SETTINGS_LOC, null, false, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket ESBPacket) {
        	 parseDecalsProperties(ESBPacket.getString(CONTENT_STREAM).split("\r\n|\r|\n"));
        	 if(!(DsESBApi.sessionId = DsESBApi.getStoredSessionId()).isEmpty()){
    			 DsESBApi.decalsValidateSession(storedSessionValidatedCallback);
    		}
         }
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Couldn't find network settings");
         }
      });
	}
   
   //Parse DECALS specific properties
   private void parseDecalsProperties(String[] rawProperties) {      
      if ((rawProperties[0].indexOf(SITE_NAME_PROP) != -1)) {
         for (String prop:rawProperties) {            
            if (prop.indexOf(IS_TN_ROOT_PROP) != -1) DsSession.getInstance().setInteractiveSearchThumbnailRootUrl(parseProperty(prop));
            if (prop.indexOf(COMPETENCY_MANAGER_PROP) != -1) DsSession.getInstance().setCompetencyManagerUrl(parseProperty(prop));
         }
      }
   }
   

   public static ESBCallback<ESBPacket> storedSessionValidatedCallback = new ESBCallback<ESBPacket>() {
		@Override
		public void onSuccess(ESBPacket esbPacket) {
		
			DsESBApi.username = esbPacket.getPayloadString();
		
			DsESBApi.decalsGetUser(DsESBApi.username, getStoredUsernameCallback);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			DsESBApi.setSessionId("");
		}
	};
	
	   public static ESBCallback<ESBPacket> getStoredUsernameCallback = new ESBCallback<ESBPacket>(){
			@Override
			public void onFailure(Throwable caught) {}
			
			@Override
			public void onSuccess(ESBPacket esbPacket) {
				DsSession.getInstance().setSessionUser(new AppUser(esbPacket.get(ESBApi.ESBAPI_RETURN_OBJ).isObject()));
				DsSession.getInstance().setSessionState(DsSession.SessionState.LOGGED_IN);
				DsSession.getInstance().buildUserGroupsAndCollections();
	            DsSession.getInstance().setCachedLrSearchHandler(null);
	            PageAssembler.setTemplate(new DsUserHomeScreen().getTemplates().getHeader().getText(), new DsUserHomeScreen().getTemplates().getFooter().getText(), DecalsScreen.CONTENT_PANE);
				((DsScreenDispatch)Decals_ui.dispatcher).loadUserHomeScreen();
			}
	  };
}
