package com.eduworks.decals.ui.client.util;

/**
 * Message container class.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsMessage {
   
   private String html;
   private String closeBtnId;      
   
   public DsMessage(String html, String closeBtnId) {
      this.html = html;
      this.closeBtnId = closeBtnId;
   }
   
   public String getHtml() {return html;}
   public String getCloseBtnId() {return closeBtnId;}

}
