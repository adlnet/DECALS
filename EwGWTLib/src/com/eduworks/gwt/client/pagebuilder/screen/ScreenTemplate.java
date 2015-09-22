/*
Copyright 2012-2013 Eduworks Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.eduworks.gwt.client.pagebuilder.screen;

import com.eduworks.gwt.client.component.HtmlTemplates;
import com.eduworks.gwt.client.pagebuilder.modal.ModalDispatch;
import com.eduworks.gwt.client.pagebuilder.overlay.OverlayDispatch;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;


public abstract class ScreenTemplate {	
	public abstract ScreenDispatch getDispatcher();
	public abstract OverlayDispatch getOverlayDispatcher();
	public abstract ModalDispatch getModalDispatcher();
	
	public abstract HtmlTemplates getTemplates();
	
	public abstract void display();
	
	public abstract void lostFocus();
	
	public String getScreenName() {
		return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
	}
	
	public static native void consoleLog(String str) /*-{
		$wnd.console.log(str);
	}-*/;
	
	
	
	private static final int TIMER_STATUS_WINDOW_DELAY 	= 10000;   // now set to 10 secs
	
	
	
	
	public static enum AlertType{
		WARNING,
		ERROR,
		INFO,
		SUCCESS,
		SECONDARY
	}
	
	public static void setAlertBoxMessage(String message, AlertType type, final String containerName){
		
		Element messageContainer = DOM.getElementById(containerName);
		
		if (messageContainer != null) {
			
			messageContainer.getFirstChildElement().setInnerText(message);
			
			clearAlertState();
			
			if(type == AlertType.WARNING){
				messageContainer.addClassName("warning");
				messageContainer.getStyle().setColor("white");
			}
			else if(type == AlertType.ERROR){
				messageContainer.addClassName("alert");
				messageContainer.getStyle().setColor("white");
			}
			else if(type == AlertType.INFO){
				messageContainer.addClassName("info");
				messageContainer.getStyle().setColor("black");
			}
			else if(type == AlertType.SUCCESS){
				messageContainer.addClassName("success");
				messageContainer.getStyle().setColor("white");
			}
			else if(type == AlertType.SECONDARY){
				messageContainer.addClassName("secondary");
				messageContainer.getStyle().setColor("black");
			}
			
			messageContainer.addClassName("active");
			messageContainer.removeClassName("hidden");
			
			Timer timeoutTimer = new Timer() {
			      public void run() {
			        hideAlertBox(containerName);
			      }};
			timeoutTimer.schedule(TIMER_STATUS_WINDOW_DELAY);
		}
	}
	
	public static void hideAlertBox(String containerName){
		Element messageContainer = DOM.getElementById(containerName);
		
		if (messageContainer != null) {
			messageContainer.addClassName("hidden");
			messageContainer.removeClassName("active");
		}
	}
	
	public static void clearAlertState() {
		Element messageContainer = DOM.getElementById("innerMessageContainer");
		
		if (messageContainer != null) {
			messageContainer.addClassName("hidden");
			messageContainer.removeClassName("active");
			messageContainer.removeClassName("warning");
			messageContainer.removeClassName("alert");
			messageContainer.removeClassName("info");
			messageContainer.removeClassName("success");
			messageContainer.removeClassName("secondary");
		}
	}
	
	
}