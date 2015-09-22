package com.eduworks.gwt.client.pagebuilder.modal;

import com.eduworks.gwt.client.component.HtmlTemplates;
import com.eduworks.gwt.client.pagebuilder.overlay.OverlayDispatch;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenDispatch;

public abstract class ModalTemplate {
	
	public abstract ScreenDispatch getScreenDispatcher();
	public abstract OverlayDispatch getOverlayDispatcher();
	public abstract ModalDispatch getModalDispatcher();
	public abstract HtmlTemplates getTemplates();
	
	public abstract void display();
	
	public abstract ModalSize getModalSize();
	
	protected enum ModalSize{
		TINY, 		// 30%
		SMALL,		// 40%
		MEDIUM,		// 60%
		LARGE,		// 75%
		XLARGE,		// 90%
		FULL		// 100%
	}
}
