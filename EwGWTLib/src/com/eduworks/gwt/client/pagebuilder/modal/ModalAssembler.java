package com.eduworks.gwt.client.pagebuilder.modal;

import java.util.ArrayList;

import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModalAssembler extends PageAssembler {

	protected static FlowPanel modalBody = new FlowPanel();
	protected static String modalPanelName;
	
	private static ArrayList<Widget> modalContents = new ArrayList<Widget>();
	
	public static void setContainer(String oPanelName)
	{
		modalBody.getElement().setId("modalContainer");
		
		modalPanelName = oPanelName;

		RootPanel.get(modalPanelName).add(modalBody);
	}
	
	public static void ready(Widget obj)
	{
		modalContents.add(obj);
	}
	
	public static void buildContents()
	{
		modalBody.clear();
		Element childNode;
		while ((childNode=DOM.getFirstChild(modalBody.getElement()))!=null)
			DOM.removeChild(modalBody.getElement(), childNode);
			   
		for (int i = 0; i < modalContents.size(); i++)
			modalBody.add(modalContents.get(i));

		modalContents.clear();
	}
	
	public static void resizeModal(ModalTemplate.ModalSize size){
		Element modal = RootPanel.get(modalPanelName).getElement();
		
		modal.removeClassName("tiny");
		modal.removeClassName("small");
		modal.removeClassName("medium");
		modal.removeClassName("large");
		modal.removeClassName("xlarge");
		modal.removeClassName("full");
		
		switch(size){
		case TINY:
			modal.addClassName("tiny");
			break;
		case SMALL:
			modal.addClassName("small");
			break;
		case MEDIUM:
			modal.addClassName("medium");
			break;
		case LARGE:
			modal.addClassName("large");
			break;
		case XLARGE:
			modal.addClassName("xlarge");
			break;
		case FULL:
			modal.addClassName("full");
			break;
		}
	}
	
	public static void showModal(){
		// Currently handled by Foundation JS (Might not want to do this forever, but right now it shows reveal animation using JS)
		// Modals are shown by adding the attribute data-reveal-id="reusableModal" to an anchor tag
		// Foundation needs to be called in the modal creating page, once the html is built

		// Could change to call a Native JS Method that could call a jquery reveal on the element with id set to modalPanelName
		
		openPopup(modalPanelName);
	}
	
	public static void hideModal(){
		closePopup(modalPanelName);
	}
	
}
