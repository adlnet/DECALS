package com.eduworks.decals.ui.client.pagebuilder.screen;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.handler.DsHeaderHandler;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * Stub for the Teacher home screen
 * 
 * @author Eduworks Corporation
 *
 */
public class DsTeacherHomeScreen extends DecalsScreen {
   
   @Override
   public void display() {    
      PageAssembler.ready(new HTML(getTemplates().getTeacherHomePanel().getText()));
      PageAssembler.buildContents();
      DOM.getElementById("teacher").setAttribute("style", "display:block;");
      DsHeaderHandler dhh = new DsHeaderHandler(getDispatcher());
      dhh.setUpHeader(DsSession.getUser().getFirstName(), DsSession.getUser().getEmailAddress());
   }
   
   @Override
   public void lostFocus() {}
}