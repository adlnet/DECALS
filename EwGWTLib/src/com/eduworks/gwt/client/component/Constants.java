package com.eduworks.gwt.client.component;

import com.google.gwt.user.client.ui.RootPanel;

public class Constants {
	
	public static boolean isOverlayed() {
		if (RootPanel.getBodyElement().hasClassName("overlayed"))
			return true;
		else
			return false;
	}
	
	public static void setOverlayed(boolean status) {
		if (status) 
			RootPanel.getBodyElement().addClassName("overlayed");
		else
			RootPanel.getBodyElement().removeClassName("overlayed");
	}
	
	
	public static boolean isWaiting() {
		if (RootPanel.getBodyElement().hasClassName("waiting"))
			return true;
		else
			return false;
	}
	
	public static void setWaiting(boolean status) {
		if (status) 
			RootPanel.getBodyElement().addClassName("waiting");
		else
			RootPanel.getBodyElement().removeClassName("waiting");
	}
}
