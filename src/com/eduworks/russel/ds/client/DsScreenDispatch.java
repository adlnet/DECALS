package com.eduworks.russel.ds.client;

import com.eduworks.russel.ds.client.pagebuilder.screen.DsGuestScreen;
import com.eduworks.russel.ds.client.pagebuilder.screen.DsStudentHomeScreen;
import com.eduworks.russel.ds.client.pagebuilder.screen.DsSearchScreen;
import com.eduworks.russel.ds.client.pagebuilder.screen.DsTeacherHomeScreen;
import com.eduworks.russel.ui.client.ScreenDispatch;

public class DsScreenDispatch extends ScreenDispatch
{
//	@Override
//	public void loadHomeScreen() {
//		loadScreen(new DsStudentHomeScreen(), true);
//	}
	
//	@Override
//	public void loadLoginScreen() {
//		loadScreen(new DsStudentHomeScreen(), true);
//	}
	
	public void loadSearchScreen() {
		loadScreen(new DsSearchScreen(), true);
	}
	
	public void loadGuestScreen() {
	   loadScreen(new DsGuestScreen(), true);
	}
	
	public void loadStudentHomeScreen() {
      loadScreen(new DsStudentHomeScreen(), true);
   }
	
	public void loadTeacherHomeScreen() {
      loadScreen(new DsTeacherHomeScreen(), true);
   }
}
