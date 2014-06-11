package com.eduworks.russel.ds.client.pagebuilder.screen;

import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.russel.ds.client.pagebuilder.DecalsScreen;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

public class DsSearchScreen extends DecalsScreen {

	@Override
	public void display() {
		PageAssembler.ready(new HTML(templates().getSearchPanel().getText()));
		PageAssembler.buildContents();
				
		PageAssembler.attachHandler("headerSearchBar", 
									Event.ONKEYDOWN,
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											
										}
									});
	}
	
	@Override
	public void lostFocus() {
		
	}
}
