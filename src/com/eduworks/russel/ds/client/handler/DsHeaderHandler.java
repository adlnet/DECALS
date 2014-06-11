package com.eduworks.russel.ds.client.handler;

import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.russel.ds.client.DsUtil;
import com.eduworks.russel.ds.client.DsScreenDispatch;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;

/**
 * Attach handlers for the elements on the DECALS Header
 * 
 * Handlers are can be attached as needed or all together using {@link #setUpHeader(String)}.
 * 
 * @author Tom B.
 *
 */
public class DsHeaderHandler {
   
   private DsScreenDispatch screenDispatch = null;
   
   public DsHeaderHandler(DsScreenDispatch screenDispatch) {
      this.screenDispatch = screenDispatch;
   }
   
   protected EventCallback dashboardListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.setUpAppropriateHomePage(screenDispatch);
      }
   };
  
   protected EventCallback logoutListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         ESBApi.logout(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {screenDispatch.loadGuestScreen();}
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
            });
      }
   };
   
   public void attachDashboardHandler() {      
      PageAssembler.attachHandler("headerDashboard", Event.ONCLICK, dashboardListener);
   }
   
   public void attachLogoutHandler() {
      PageAssembler.attachHandler("headerLogout", Event.ONCLICK, logoutListener);
   }
   
   public void attachUsernameToHeader(String headerUsernameText) {
      ((Label)PageAssembler.elementToWidget("headerUsername", PageAssembler.LABEL)).setText(headerUsernameText);
   }
   
   public void setUpHeader(String headerUsernameText) {
      attachDashboardHandler();
      attachLogoutHandler();
      attachUsernameToHeader(headerUsernameText);
   }

}
