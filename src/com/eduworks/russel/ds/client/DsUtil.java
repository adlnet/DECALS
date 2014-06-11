package com.eduworks.russel.ds.client;

import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * General DECALS utility package for handling various tasks.
 * 
 * @author Tom B.
 *
 */
public class DsUtil {
   
   private static final String ERROR_BOX_STYLE = "alert-box error";
   
   public static void handleFailedApiCall(Throwable caught) {
      //TODO handle this more elegantly
      Window.alert("failure->caught:" + caught.getMessage());
   }
   
   /**
    * Dispatches to the teacher home screen if the user is a teacher.  Dispatches to student home page otherwise.
    * 
    * @param screenDispatch
    */
   public static void setUpAppropriateHomePage(final DsScreenDispatch screenDispatch) {
      ESBApi.userHasPermission("isTeacher", new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            if ("true".equalsIgnoreCase(result.getPayloadString())) screenDispatch.loadTeacherHomeScreen();
            else screenDispatch.loadStudentHomeScreen();
         }
         @Override
         public void onFailure(Throwable caught) {handleFailedApiCall(caught);}
         });
   }
   
   /**
    * Removes all widgets from the given root panel.
    * 
    * @param rp
    */
   public static final void removeAllWidgetsFromRootPanel(RootPanel rp) {      
      for (int i=rp.getWidgetCount() - 1;i>=0;i--) {
         rp.remove(i);
      }
   }
   
   /**
    * Clears all widgets from the widget container and displays the given error message in a simple error widget placed in the given error widget container.
    * (Say that fast!)
    * 
    * @param errorWidgetContainer The container in which to place the widget.
    * @param errorMessage The message to display.
    */
   public static final void showSimpleErrorMessage(String errorWidgetContainer, String errorMessage) {
      ((Label)PageAssembler.elementToWidget(errorWidgetContainer, PageAssembler.LABEL)).getElement().setAttribute("style","display:block");
      StringBuffer sb = new StringBuffer();
      sb.append("<div class=\"" + ERROR_BOX_STYLE + "\">");
      sb.append("<span>" + errorMessage + "</span>");
      sb.append("</div>");
      HTML errorDialog = new HTML(sb.toString());
      removeAllWidgetsFromRootPanel(RootPanel.get(errorWidgetContainer));
      RootPanel.get(errorWidgetContainer).add(errorDialog);            
   }

}
