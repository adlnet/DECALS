package com.eduworks.decals.ui.client.util;

import com.eduworks.gwt.client.net.callback.EventCallback;
import com.google.gwt.user.client.Event;

/**
 * Message closer handler.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsMessageCloser extends EventCallback {
   
   private String messageId;
   
   public DsMessageCloser(String messageId) {this.messageId = messageId;}
   
   @Override
   public void onEvent(Event event) {   
      DsUtil.hideLabel(messageId);
   }
}