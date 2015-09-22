package com.eduworks.decals.ui.client.pagebuilder;

import com.eduworks.decals.ui.client.Decals_ui;
import com.eduworks.decals.ui.client.DsScreenDispatch;
import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.pagebuilder.DsHtmlTemplates;
import com.eduworks.gwt.client.component.AppSettings;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.modal.ModalDispatch;
import com.eduworks.gwt.client.pagebuilder.overlay.OverlayDispatch;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenTemplate;

/**
 * Base screen class.
 * 
 * @author Eduworks Corporation
 *
 */
public abstract class DecalsScreen extends ScreenTemplate {
   
   public static final String CONTENT_PANE = "contentPane";
   public static final String VALID_EVENT = "valid";
   
	public DsScreenDispatch getDispatcher() {return (DsScreenDispatch) AppSettings.dispatcher;}
	public DsHtmlTemplates getTemplates() {return (DsHtmlTemplates) AppSettings.templates;}
	@Override
	public OverlayDispatch getOverlayDispatcher() {return Decals_ui.overlayDispatcher;}

	@Override
	public ModalDispatch getModalDispatcher() {return Decals_ui.modalDispatcher;}
	
	 protected void validateSession(){
		   DsSession.getInstance().validateSession(new ESBCallback<ESBPacket>() {

				@Override
				public void onFailure(Throwable caught) {
					getDispatcher().loadGuestScreen();
				}

				@Override
				public void onSuccess(ESBPacket esbPacket) {}
			});
	   }
}
