package com.eduworks.gwt.client.component;

import com.eduworks.gwt.client.pagebuilder.modal.ModalDispatch;
import com.eduworks.gwt.client.pagebuilder.overlay.OverlayDispatch;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenDispatch;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenTemplate;
import com.google.gwt.core.client.GWT;

public class AppSettings {
   
   public static ScreenDispatch dispatcher = new ScreenDispatch();
   public static OverlayDispatch overlayDispatcher = new OverlayDispatch();
   public static ModalDispatch modalDispatcher = new ModalDispatch();
   
   public static HtmlTemplates templates = GWT.create(HtmlTemplates.class);
   
   public static ScreenTemplate defaultScreen;
   
   public static String siteName = "";
   public static String helpURL = ""; 

}
