package com.eduworks.russel.ds.client.pagebuilder.screen;

import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.russel.ds.client.DsSession;
import com.eduworks.russel.ds.client.handler.DsHeaderHandler;
import com.eduworks.russel.ds.client.pagebuilder.DecalsScreen;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;

public class DsStudentHomeScreen extends DecalsScreen {
   
	@Override
	public void display() {	   
	   PageAssembler.ready(new HTML(templates().getStudentHomePanel().getText()));
      PageAssembler.buildContents();
      DOM.getElementById("student").setAttribute("style", "display:block;");
      DsHeaderHandler dhh = new DsHeaderHandler(view());
      dhh.setUpHeader(DsSession.getInstance().getFullName());
	}
	
	@Override
	public void lostFocus() {}
}
