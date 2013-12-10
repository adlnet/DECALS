package com.eduworks.russel.ds.client.pagebuilder.screen;

import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.russel.ds.client.DsScreenDispatch;
import com.eduworks.russel.ds.client.pagebuilder.DecalsScreen;
import com.eduworks.russel.ds.client.pagebuilder.DsHtmlTemplates;
import com.eduworks.russel.ui.client.Constants;
import com.eduworks.russel.ui.client.pagebuilder.screen.HomeScreen;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

public class DsHomeScreen extends DecalsScreen {

	@Override
	public void display() {
		PageAssembler.ready(new HTML(templates().getRecentItemPanel().getText()));
		PageAssembler.buildContents();
		
		PageAssembler.attachHandler("r-launchUpload", 
									Event.ONCLICK,
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											view().loadEditScreen();
										}
									});
	}
	
	@Override
	public void lostFocus() {
		// TODO Auto-generated method stub
		
	}
}
