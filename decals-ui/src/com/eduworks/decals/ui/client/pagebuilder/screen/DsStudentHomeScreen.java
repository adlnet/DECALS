package com.eduworks.decals.ui.client.pagebuilder.screen;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.handler.DsHeaderHandler;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * Stub for the student home screen.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsStudentHomeScreen extends DecalsScreen {
   
	@Override
	public void display() {	 
		validateSession();
	   PageAssembler.ready(new HTML(getTemplates().getStudentHomePanel().getText()));
      PageAssembler.buildContents();
      DOM.getElementById("student").setAttribute("style", "display:block;");
      DsHeaderHandler dhh = new DsHeaderHandler(getDispatcher());
      dhh.setUpHeader(DsSession.getUser().getFirstName(), DsSession.getUser().getEmailAddress());
	}
	
	@Override
	public void lostFocus() {}
}
