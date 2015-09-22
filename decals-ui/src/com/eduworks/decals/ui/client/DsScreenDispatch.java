package com.eduworks.decals.ui.client;

import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsApplicationAdminScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsGuestScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsStudentHomeScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsTeacherHomeScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsUserHomeScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsUserLrRSearchScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsUserManagementScreen;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsUserPreferencesScreen;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenDispatch;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenTemplate;

public class DsScreenDispatch extends ScreenDispatch
{
   
	/**
	 * Loads DsGuestScreen.
	 */
	public void loadGuestScreen() {loadScreen(new DsGuestScreen(), true);}	
	
	/**
	 * Loads DsStudentHomeScreen.
	 */
	public void loadStudentHomeScreen() {loadScreen(new DsStudentHomeScreen(), true);}	
	
	/**
	 * Loads DsTeacherHomeScreen.
	 */
	public void loadTeacherHomeScreen() {loadScreen(new DsTeacherHomeScreen(), true);}
	
	/**
    * Loads DsUserHomeScreen.
    */
   public void loadUserHomeScreen() {loadScreen(new DsUserHomeScreen(), true);}
   
   /**
    * Loads DsUserLRSearchScreen.
    */
   public void loadUserLRSearchScreen() {loadScreen(new DsUserLrRSearchScreen(), true);}
   
   /**
    * Loads DsApplicationAdminScreen.
    */
   public void loadApplicationAdminScreen() {loadScreen(new DsApplicationAdminScreen(), true);}
   
   /**
    * Loads DsUserManagementScreen.
    */
   public void loadUserManagementScreen() {loadScreen(new DsUserManagementScreen(), true);}
   
   /**
    * Loads DsUserPreferencesScreen.
    */
   public void loadUserPreferencesScreen() {
	   
	   DsESBApi.decalsUserPreferences(new ESBCallback<ESBPacket>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(ESBPacket esbPacket) {
			loadScreen(new DsUserPreferencesScreen(esbPacket), true);
		}
		   
	   });
	   
   }
   
   public static ScreenTemplate getScreen(String token){
		for (int x=history.size()-1;x>-1;x--) {
			if (history.get(x).token.equals(token)) {
				if (history.get(x).screen!=null)
					return history.get(x).screen;
			}
		}
		
		return null;
	}
}
