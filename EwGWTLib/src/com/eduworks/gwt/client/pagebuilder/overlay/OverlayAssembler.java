package com.eduworks.gwt.client.pagebuilder.overlay;

import java.util.ArrayList;

import com.eduworks.gwt.client.component.Constants;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OverlayAssembler extends PageAssembler {
	protected static FlowPanel overlayBody = new FlowPanel();
	
	protected static String overlayPanelName = "contentOverlay";
	protected static String CLOSE_OVERLAY_BTN_ID = "close-overlay-btn";
	
	private static  ArrayList<Widget> overlayContents = new ArrayList<Widget>();
	
	public static void setOverlayElements(String overlayElementId, String closeBtnId){
		setContainer(overlayElementId);
		setCloseOverlayButtonId(closeBtnId);
	}
	
	public static void setContainer(String oPanelName)
	{
		overlayBody.getElement().setId("overlayContainer");
		overlayBody.getElement().setClassName("content");
		
		overlayPanelName = oPanelName;

		RootPanel.get(overlayPanelName).add(overlayBody);
	}
	
	public static void setCloseOverlayButtonId(String id){
		CLOSE_OVERLAY_BTN_ID = id;
	}
	
	public static void ready(Widget obj)
	{
		DOM.getElementById(overlayPanelName).addClassName("active");
		overlayContents.add(obj);
	}
	
	public static void buildContents()
	{
		overlayBody.clear();
		Element childNode;
		while ((childNode=DOM.getFirstChild(overlayBody.getElement()))!=null)
			DOM.removeChild(overlayBody.getElement(), childNode);
			   
		for (int i = 0; i < overlayContents.size(); i++)
			overlayBody.add(overlayContents.get(i));

		overlayContents.clear();
	}
	
	public static void showOverlay(){
		if (RootPanel.get(overlayPanelName).getElement() != null)
			RootPanel.get(overlayPanelName).getElement().addClassName("active");
		
		RootPanel.getBodyElement().addClassName("overlayed");
		Constants.setOverlayed(true);
	}
	
	public static void hideOverlay(){
		if (RootPanel.get(overlayPanelName).getElement() != null)
			RootPanel.get(overlayPanelName).getElement().removeClassName("active");
		
		RootPanel.getBodyElement().removeClassName("overlayed");
		Constants.setOverlayed(false);
	}
}
