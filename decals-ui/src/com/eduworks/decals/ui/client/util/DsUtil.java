package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Utility package for handling various repeated tasks.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsUtil {
   
   private static final String ERROR_BOX_STYLE = "alert-box warning";
   
   private static final int MAX_FILENAME_MESSAGE_LENGTH = 20;
   
   public static final String BOLD_STYLE = "font-weight:bold;";
   
   public enum NavMode{MORE,LESS}
   
   /**
    * Common English stop words.
    */
   public static String[] stopWords = { 
      "a","able","about","across","after","all","almost","also","am","among","an","and","any","are","as","at","be",
      "because","been","but","by","can","cannot","could","dear","did","do","does","either","else","ever","every",
      "for","from","get","got","had","has","have","he","her","hers","him","his","how","however","i","if","in","into",
      "is","it","its","just","least","let","like","likely","may","me","might","most","must","my","neither","no",
      "nor","not","of","off","often","on","only","or","other","our","own","rather","said","say","says","she",
      "should","since","so","some","than","that","the","their","them","then","there","these","they","this","tis",
      "to","too","twas","us","wants","was","we","were","what","when","where","which","while","who","whom","why",
      "will","with","would","yet","you","your"
   };
   
   /**
    * Resets the given form
    * 
    * @param formId  The ID of the form to reset.
    */
   public static void resetForm(String formId) {
      FormPanel aciForm = (FormPanel)PageAssembler.elementToWidget(formId, PageAssembler.FORM);
      aciForm.reset();
   }
   
   /**
    * Returns a nicely formatted file size string  given the file size in bytes
    * 
    * @param fileSize The file size in bytes
    * @return  Returns a nicely formatted file size string given the file size in bytes
    */
   public static String getNiceFileSizeString(long fileSize) {
      try {
         NumberFormat decimalFormat = NumberFormat.getFormat("#,##0.##");
         if (fileSize > 1000000) return decimalFormat.format((double) fileSize/1000000) + " MB";
         else if (fileSize > 1000) return decimalFormat.format((double) fileSize/1000) + " KB";
         else return decimalFormat.format(fileSize) + " bytes";         
      }
      catch (Exception e) {return "N/A";}
   }
   
   /**
    * Attempts to generate and return the number of seconds from a given duration string structured as 'hh:mm:ss'.
    * If an error occurs, 0 is returned.
    * 
    * @param durationStr
    * @return Returns the number of seconds from a given duration string structured as 'hh:mm:ss'. If an error occurs, 0 is returned.
    */
   public static long getSecondsFromDurationString(String durationStr) {
      try {
         if (durationStr == null || durationStr.trim().isEmpty()) return 0;
         long hours = 0;
         long minutes = 0;
         long seconds = 0;
         String[] parts = durationStr.split(":");
         if (parts.length == 1) {seconds = Long.parseLong(parts[0]);}
         else if (parts.length == 2) {
            minutes = Long.parseLong(parts[0]);
            seconds = Long.parseLong(parts[1]);
         }
         else if (parts.length == 3) {
            hours = Long.parseLong(parts[0]);
            minutes = Long.parseLong(parts[1]);
            seconds = Long.parseLong(parts[2]);
         }
         return seconds + (minutes * 60) + (hours * 3600);
      }
      catch (Exception e) {return 0;}
   }

   /**
    * Pads the given string s with the given string padChar to the given size 
    * 
    * @param s The string to pad
    * @param size The max size of the string
    * @param padChar The pad character/string
    * @return Returns the given string s padded with the given string padChar to the given size 
    */
   public static String leftPadString(String s, int size, String padChar) {
      if (s.length() >= size) return s;
      StringBuffer sb = new StringBuffer();
      for (int i=0;i<size-s.length();i++) sb.append(padChar);
      sb.append(s);
      return sb.toString();      
   }

   /**
    * Returns a duration string in the format of "hh:mm:ss" for the given duration in seconds
    * 
    * @param seconds The number of seconds
    * @return Returns a duration string in the format of "hh:mm:ss" for the given duration in seconds
    */
   public static String getDurationStringFromSeconds(long seconds) {
      if (seconds == 0) return null;
      int hours = (int) (seconds/3600);
      long rem = seconds % 3600;
      int mins = (int) (rem/60);
      int secs = (int) (rem % 60);            
      return leftPadString(String.valueOf(hours),2,"0") + ":" + 
         leftPadString(String.valueOf(mins),2,"0") + ":" + 
         leftPadString(String.valueOf(secs),2,"0");
   }
   
   /**
    * Builds and sorts a user list from the user get service returns
    * 
    * @param listToAdd The list to populate
    * @param users The JSON user get service returns
    */
   public static void buildUserTypeListFromReturn(ArrayList<AppUser> listToAdd, JSONObject users) {
      if (listToAdd == null) listToAdd = new ArrayList<AppUser>();
      listToAdd.clear();
      for (JSONObject jo :  DsUtil.parseJsonReturnList(users)) listToAdd.add(new AppUser(jo));
      Collections.sort(listToAdd);
   }
   
   /**
    * Returns the token for the given widget ID.
    * Dependent on format of widget IDs as generated by PageAssembler.inject
    * 
    * @param widgetId The widget ID (as generated by PageAssembler.inject) for which to find the token
    * @param suffix The suffix of the widget ID
    * @return  Returns the token for the given widget ID.
    */
   public static String getTokenFromWidgetId(String widgetId, String suffix) {
      return(widgetId.substring(0,widgetId.indexOf(suffix)));
   } 
   
   /**
    * Returns the token for the given widget ID.
    * Dependent on format of widget IDs as generated by PageAssembler.inject and a widget ID in the format of 'x-???' 
    * where 'x' is the token ID and '???' is the widget suffix.
    * 
    * @param widgetId The widget ID (as generated by PageAssembler.inject) for which to find the token
    * @return  Returns the token for the given widget ID.
    */
   public static String getTokenFromWidgetId(String widgetId) {
      return(widgetId.substring(0,widgetId.indexOf("-")));
   } 
   
   /**
    * Returns the list of distinct token IDs from the vector of widget IDs.
    * Dependent on format of widget IDs as generated by PageAssembler.inject
    * 
    * @param widgetIds The vector of widget IDs (as generated by PageAssembler.inject) for which to find the unique tokens
    * @return Returns the list of distinct token IDs from the vector of widget IDs.
    */
   public static ArrayList<String> getWidgetElementTokenList(Vector<String> widgetIds) {
      HashMap<String,String> tokenMap = new HashMap<String,String>();
      for (String widgetId:widgetIds) tokenMap.put(getTokenFromWidgetId(widgetId),null);
      return getKeyList(tokenMap);
   }
   
   /**
    * Returns the key list for the given hash map 
    * 
    * @param map The hash map in which to retrieve the keys
    * @return Returns the key list for the given hash map
    */ 
   public static ArrayList<String> getKeyList(HashMap<String,String> map) {
      ArrayList<String> keyList = new ArrayList<String>();
      Iterator<String> it = map.keySet().iterator();
      while (it.hasNext()) keyList.add(it.next());
      return keyList;      
   }
   
   /**
    * Builds and returns a string list from the given comma separated string.
    * 
    * @param commaStr
    * @return Returns a string list from the given comma separated string.
    */
   public static ArrayList<String> buildStringListFromCommaString(String commaStr) {      
      ArrayList<String> ret = new ArrayList<String>();
      if (commaStr == null || commaStr.trim().isEmpty()) return ret;
      String[] sa = commaStr.split(",");
      for (String s:sa) ret.add(s.trim());
      return ret;
   }
   
   /**
    * Builds and returns a comma separated string from a list of strings.
    * 
    * @param strList
    * @return Returns a comma separated string from a list of strings.
    */
   public static String buildCommaStringFromStringList(ArrayList<String> strList) {      
      if (strList == null || strList.isEmpty()) return null;
      StringBuffer sb = new StringBuffer();
      for (String s: strList) sb.append(s +",");
      sb.setLength(sb.length() - 1);
      return sb.toString();
   }
   
   /**
    * Builds and returns a comma separated string from the keys of a string keyed hash map.
    * 
    * @param strList
    * @return Returns a comma separated string from the keys of a string keyed hash map.
    */
   public static String buildCommaStringFromHashMap(HashMap<String,?> strMap) {      
      if (strMap == null || strMap.isEmpty()) return null;
      StringBuffer sb = new StringBuffer();
      for (String s: strMap.keySet()) sb.append(s +",");
      sb.setLength(sb.length() - 1);
      return sb.toString();
   }
   
   /**
    * Builds and returns a JSON array string from the given string list.
    * 
    * @param strList The string list
    * @return Returns a JSON array string from the given string list.
    */
   public static String buildJsonArrayStringFromStringList(ArrayList<String> strList) {
      JSONArray ja = new JSONArray();
      if (strList == null || strList.size() == 0) ja.toString();      
      for (int i=0;i<strList.size();i++) ja.set(i,new JSONString(strList.get(i)));
      return ja.toString();
   }
   
   /**
    * Builds and returns a JSON array from the given string list.
    * 
    * @param strList The string list
    * @return Returns a JSON array string from the given string list.
    */
   public static JSONArray buildJsonArrayFromStringList(ArrayList<String> strList) {
      JSONArray ja = new JSONArray();
      if (strList == null || strList.size() == 0) ja.toString();      
      for (int i=0;i<strList.size();i++) ja.set(i,new JSONString(strList.get(i)));
      return ja;
   }
   
   /**
    * Builds and returns a string list from the given JSON array
    * 
    * @param ja The JSON array
    * @return Returns a string list from the given JSON array
    */
   public static ArrayList<String> buildStringListFromJsonArray(JSONArray ja) {
      ArrayList<String> ret = new ArrayList<String>();
      for(int i=0;i<ja.size();i++) ret.add(ja.get(i).isString().stringValue());
      return ret;
   }
   
   /**
    * Parses JSON list returns (standard levr returns) into an array list of JSON objects
    * 
    * @param jsonReturn The JSON return to parse
    * @return The parsed array list
    */
   public static ArrayList<JSONObject> parseJsonReturnList(JSONObject jsonReturn) {
      ArrayList<JSONObject> ual = new ArrayList<JSONObject>();
      for (String key :jsonReturn.keySet()) ual.add(jsonReturn.get(key).isObject());
      return ual;
   }
   
   /**
    * Returns a string in the format 'EEE MMM dd HH:mm:ss yyyy' for the given long date.
    * ex: Wed Jan 29 08:12:05 2014
    * 
    * @param dl The long date
    * @return Returns a string in the format 'EEE MMM dd HH:mm:ss yyyy' for the given long date.
    */
   public static String getDateFormatLongDate(long dl) {
      try {
         return DateTimeFormat.getFormat("EEE MMM dd HH:mm:ss yyyy").format(new Date(dl));
      }
      catch (Exception e) {
         return null;
      }
   }
   
   /**
    * Returns true if the given string is a stop word.  Returns false otherwise.
    * 
    * @param s The string to check.
    * @return Returns true if the given string is a stop word.  Returns false otherwise.
    */
   public static boolean isStopWord(String s) {      
      if (s == null || s.trim().isEmpty()) return false;
      for (String sw:stopWords) {
         if (sw.equalsIgnoreCase(s)) return true;
      }
      return false;
   }
   
   /**
    * Removes all stop words from the given string.
    * 
    * @param s The string in which to remove all stop words.
    * @return The given string with all stop words removed. 
    */
   public static String removeStopWords(String s) {
      StringBuffer sb = new StringBuffer();
      String[] sa = s.split(" ");
      for (int i=0;i<sa.length;i++) {
         if (!isStopWord(sa[i])) {
            sb.append(sa[i]);
            if (i != (sa.length - 1)) sb.append(" ");
         } 
      }
      return sb.toString().trim();
   }
   
   /**
    * Replaces all non-alpha characters with an empty string.
    * 
    * @param s The string in which to remove all non-alpha characters.
    * @return The given string with all non-alpha characters removed.
    */
   public static String removeNonAlphaChars(String s) {
      return s.replaceAll("[^a-zA-Z]", "");
   }
   
   /**
    * Returns true if the given string is a number.  Returns false otherwise.
    * 
    * @param s The string to check.
    * @return Returns true if the given string is a number.  Returns false otherwise.
    */
   public static boolean isValueNumber(String s) {
      try {
         Double.parseDouble(s);
         return true;
      }
      catch (Exception e) {
         return false;
      }      
   }
   
   /**
    * Basic window.alert call that is used to handle failed API calls.
    * 
    * @param caught The throwable that was caught on the failed call.
    */
   public static void handleFailedApiCall(Throwable caught) {
      //TODO handle this more elegantly
      Window.alert("failure->caught:" + caught.getMessage());
   }
   
   /**
    * Sends the given message to the DECALS tracking service.
    * 
    * @param message The message to send.
    */
   public static void sendTrackingMessage(String message) {
//Commented out for now since no tracking is required
//      if (DsSession.getInstance().getAssignmentId() == null || DsSession.getInstance().getAssignmentId().trim().isEmpty()) return;
//      DsESBApi.decalsAddTracking(DsSession.getInstance().getAssignmentId(),message, new ESBCallback<ESBPacket>() {
//         @Override
//         public void onSuccess(ESBPacket result) {}
//         @Override
//         public void onFailure(Throwable caught) {handleFailedApiCall(caught);}
//         });
   }
   
   /**
    * Parses a given string into a JSONObject
    * 
    * I'm putting this here just in case I need to change how this is done.
    * 
    * @param jsonStr The JSON string to parse.
    * @return Returns the JSON object.
    */
   public static JSONObject parseJson(String jsonStr) {
     return (JSONObject) JSONParser.parseStrict(jsonStr);
   }
   
   /**
    * Dispatches to the teacher home screen if the user is a teacher.  Dispatches to student home page otherwise.
    * 
    * @param screenDispatch
    */
//Dead code for now  TB 1/12/2015
//   public static void setUpAppropriateHomePage(final DsScreenDispatch screenDispatch) {
//      ESBApi.userHasPermission("isTeacher", new ESBCallback<ESBPacket>() {
//         @Override
//         public void onSuccess(ESBPacket result) {
//            if ("true".equalsIgnoreCase(result.getPayloadString())) screenDispatch.loadTeacherHomeScreen();
//            else screenDispatch.loadStudentHomeScreen();
//         }
//         @Override
//         public void onFailure(Throwable caught) {handleFailedApiCall(caught);}
//         });
//   }
   
   
   
   /**
    * Generates a random ID with the given prefix
    * 
    * @param idPrefix The ID prefix to use
    * @return
    */
   public static final String generateId(String idPrefix) {
      if (idPrefix == null) return Document.get().createUniqueId();
      return idPrefix.trim() + Document.get().createUniqueId();
   }
   
   /**
    * Removes all children from an element
    * 
    * @param elementId The element ID
    */
   public static final void removeAllChildrenFromElement(String elementId) {      
      DOM.getElementById(elementId).removeAllChildren();
   }
   
   /**
    * Removes all widgets from the given root panel.
    * 
    * @param rp
    */
   public static final void removeAllWidgetsFromRootPanel(RootPanel rp) {      
      if (rp == null) return;
      for (int i=rp.getWidgetCount() - 1;i>=0;i--) {
         if (rp.getWidget(i) != null) rp.remove(i);
      }
   }
   
   /**
    * Removes an element with the given ID from the given root panel.
    * 
    * @param rp
    * @param elementIdToRemove
    */
   public static final void removeElementFromRootPanel(RootPanel rp, String elementIdToRemove) {      
      for (int i=rp.getWidgetCount() - 1;i>=0;i--) {
         if (rp.getWidget(i).getElement().getId().equals(elementIdToRemove)) {
            rp.remove(i);
            break;
         }
      }
   }
   
   /**
    * Clears all widgets from the widget container and displays the given error message in a simple error widget placed in the given error widget container.
    * (Say that fast!)
    * 
    * @param errorWidgetContainer The container in which to place the widget.
    * @param errorMessage The message to display.
    * @param removeOtherWidgets The remove other widgets indicator
    */
   public static final void showSimpleErrorMessage(String errorWidgetContainer, String errorMessage, boolean removeOtherWidgets) {
      ((Label)PageAssembler.elementToWidget(errorWidgetContainer, PageAssembler.LABEL)).getElement().setAttribute("style","display:block");
      StringBuffer sb = new StringBuffer();
      sb.append("<div class=\"" + ERROR_BOX_STYLE + "\">");
      sb.append("<span>" + errorMessage + "</span>");
      sb.append("</div>");
      HTML errorDialog = new HTML(sb.toString());
      if(removeOtherWidgets) removeAllWidgetsFromRootPanel(RootPanel.get(errorWidgetContainer));
      RootPanel.get(errorWidgetContainer).add(errorDialog);            
   }
      
   /**
    * Cleans the given string of all special characters and double spaces
    * 
    * @param s The string to clean.
    * @return Returns the clean string.
    */
   public static String cleanString(String s) {
      s = s.replace(":", " ");
      s = s.replace("-", " ");
      String s2 = s.replaceAll("[^\\w\\s]","").replaceAll("  ", " ").trim();
      String s3 = s2.replaceAll("  ", " ").trim();
      while (!s3.equals(s2)) {
         s3 = s2.replaceAll("  ", " ");
         if (!s3.equals(s2)) s2 = s3.replaceAll("  ", " ");
      }
      return s3;
   }
   
   /**
    * Returns true if the CheckBox with the given ID is checked.  Returns false otherwise.
    * 
    * @param checkBoxFieldId The ID of the CheckBox.
    * @return Returns true if the CheckBox with the given ID is checked.  Returns false otherwise.
    */
   public static boolean isCheckBoxChecked(String checkBoxFieldId) {
      if (checkBoxFieldId == null || checkBoxFieldId.trim().isEmpty()) return false;      
      return ((SimpleCheckBox)PageAssembler.elementToWidget(checkBoxFieldId, PageAssembler.CHECK_BOX)).getValue();
   }
   
   /**
    * Applies the given value to the given to the CheckBox with the given ID (true for checked, false for unchecked).
    * 
    * @param checkBoxFieldId The ID of the CheckBox.
    * @param value The value to apply to the CheckBox (true for checked, false for unchecked).
    */
   public static void applyCheckBoxValue(String checkBoxFieldId, boolean value) {
      if (checkBoxFieldId != null && !checkBoxFieldId.trim().isEmpty()) {
         ((SimpleCheckBox)PageAssembler.elementToWidget(checkBoxFieldId, PageAssembler.CHECK_BOX)).setValue(value);
      }
   }
   
   /**
    * Returns the text of the Anchor with the given ID.
    * 
    * @param anchorFieldId The ID of the Anchor field to get the text.
    * @return Returns the text of the Anchor with the given ID.
    */
   public static final String getAnchorText(String anchorFieldId) {
      if (anchorFieldId != null && !anchorFieldId.trim().isEmpty()) return ((Anchor)PageAssembler.elementToWidget(anchorFieldId, PageAssembler.A)).getText();
      else return null;            
   }
   
   /**
    * Returns the selected value/text of the Drop Down (select/option) with the given ID.
    * 
    * @param dropDownId The ID of the drop down to get the text.
    * @return  Returns the selected value/text of the Drop Down (select/option) with the given ID.
    */
   public static final String getDropDownSelectedText(String dropDownId) {
      int idx = ((ListBox)PageAssembler.elementToWidget(dropDownId, PageAssembler.SELECT)).getSelectedIndex();
      if (idx < 0) return null;
      else return ((ListBox)PageAssembler.elementToWidget(dropDownId, PageAssembler.SELECT)).getItemText(idx);      
   }
   
   /**
    * Returns the text of the TextBox with the given ID.  Returns null if the given ID is blank or null.
    * 
    * @param textBoxFieldId The ID of the TextBox field to retrieve the text.
    * @return Returns the text of the TextBox with the given ID. Returns null if the given ID is blank or null.
    */
   public static final String getTextBoxText(String textBoxFieldId) {
      if (textBoxFieldId != null && !textBoxFieldId.trim().isEmpty()) return ((TextBox)PageAssembler.elementToWidget(textBoxFieldId, PageAssembler.TEXT)).getText();
      else return null;
   }  
   
   /**
    * Sets the text of the TextBox with the given ID to the given text.
    * 
    * @param textBoxFieldId The ID of the TextBox field to set the text.
    * @param text The text to set.
    */
   public static final void setTextBoxText(String textBoxFieldId, String text) {
      if (textBoxFieldId != null && !textBoxFieldId.trim().isEmpty()) 
           ((TextBox)PageAssembler.elementToWidget(textBoxFieldId, PageAssembler.TEXT)).setText(text);      
   }
      
   /**
    * Returns the text of the TextArea with the given ID.  Returns null if the given ID is blank or null.
    * 
    * @param textAreaFieldId The ID of the TextArea field to retrieve the text.
    * @return Returns the text of the TextBox with the given ID. Returns null if the given ID is blank or null.
    */
   public static final String getTextAreaText(String textAreaFieldId) {
      if (textAreaFieldId != null && !textAreaFieldId.trim().isEmpty()) return ((TextArea)PageAssembler.elementToWidget(textAreaFieldId, PageAssembler.TEXT_AREA)).getText();
      else return null;
   }
   
   /**
    * Sets the text of the TextArea with the given ID to the given text.
    * 
    * @param textAreaFieldId The ID of the TextArea field to set the text.
    * @param text The text to set.
    */
   public static final void setTextAreaText(String textAreaFieldId, String text) {
      if (textAreaFieldId != null && !textAreaFieldId.trim().isEmpty()) 
           ((TextArea)PageAssembler.elementToWidget(textAreaFieldId, PageAssembler.TEXT_AREA)).setText(text);      
   }
   
   /**
    * Sets the text of the Anchor with the given ID to the given text.
    * 
    * @param anchorFieldId The ID of the Anchor field to set the text.
    * @param text The text to set.
    */
   public static final void setAnchorText(String anchorFieldId, String text) {
      if (anchorFieldId != null && !anchorFieldId.trim().isEmpty()) 
         ((Anchor)PageAssembler.elementToWidget(anchorFieldId,PageAssembler.A)).setText(text);      
   }
   
   /**
    * Sets the target of the Anchor with the given ID to the given target.
    * 
    * @param anchorFieldId The ID of the Anchor field to set the target.
    * @param target The target to set.
    */
   public static final void setAnchorTarget(String anchorFieldId, String target) {
      if (anchorFieldId != null && !anchorFieldId.trim().isEmpty()) 
         ((Anchor)PageAssembler.elementToWidget(anchorFieldId,PageAssembler.A)).setTarget(target);      
   }
   
   /**
    * Sets the href of the Anchor with the given ID to the given href.
    * 
    * @param anchorFieldId The ID of the Anchor field to set the href.
    * @param href The href to set.
    */
   public static final void setAnchorHref(String anchorFieldId, String href) {
      if (anchorFieldId != null && !anchorFieldId.trim().isEmpty()) 
         ((Anchor)PageAssembler.elementToWidget(anchorFieldId,PageAssembler.A)).setHref(href);      
   }
   
   /**
    * Sets the style of the Anchor with the given ID to the given style.
    * 
    * @param anchorFieldId The ID of the Anchor field to set the style.
    * @param style The style to set.
    */
   public static final void setAnchorStyle(String anchorFieldId, String style) {
      if (anchorFieldId != null && !anchorFieldId.trim().isEmpty()) 
         ((Anchor)PageAssembler.elementToWidget(anchorFieldId,PageAssembler.A)).getElement().setAttribute("style",style);
   }
   
   /**
    * Sets the style of the anchor with the given ID to 'display:none'.
    * 
    * @param anchorFieldId The ID of the Anchor field to set the style.
    */
   public static final void hideAnchor(String anchorFieldId) {
      if (anchorFieldId != null && !anchorFieldId.trim().isEmpty())
         ((Anchor)PageAssembler.elementToWidget(anchorFieldId, PageAssembler.A)).getElement().setAttribute("style","display:none");
   }
   
   /**
    * Sets the text of the Label with the given ID to the given text.
    * 
    * @param labelFieldId The ID of the Label field to set the text.
    * @param text The text to set.
    */
   public static final void setLabelText(String labelFieldId, String text) {
      if (labelFieldId != null && !labelFieldId.trim().isEmpty()) 
         ((Label)PageAssembler.elementToWidget(labelFieldId,PageAssembler.LABEL)).setText(text);      
   }
   
   /**
    * Returns the value for the given attribute/element if it exists.  Returns null otherwise.
    * 
    * @param elementId The element ID.
    * @param attributeName The attribute name
    * @return  Returns the value for the given attribute/element if it exists.  Returns null otherwise.
    */
   public static final String getElementAttributeValue(String elementId, String attributeName) {
      try {
         Element e = DOM.getElementById(elementId);
         if (e == null) return null;
         return e.getAttribute(attributeName);
      }
      catch (Exception e) {return null;}
   }
   
   /**
    * Returns the text of the Label with the given ID.  Returns null if the given ID is blank or null.
    * 
    * @param labelFieldId The ID of the Label field to retrieve the text.
    * @return Returns the text of the Label with the given ID. Returns null if the given ID is blank or null.
    */
   public static final String getLabelText(String labelFieldId) {
      try {
         if (labelFieldId != null && !labelFieldId.trim().isEmpty()) return ((Label)PageAssembler.elementToWidget(labelFieldId,PageAssembler.LABEL)).getText();
         else return null;         
      }
      catch (Exception e) {return null;}
   }
   
   /**
    * Sets the focus to the TextBox with the given ID.
    * 
    * @param textBoxFieldId The ID of the TextBox field to set focus.
    */
   public static final void setFocus(String textBoxFieldId) {
      if (textBoxFieldId != null && !textBoxFieldId.trim().isEmpty())
         ((TextBox)PageAssembler.elementToWidget(textBoxFieldId, PageAssembler.TEXT)).setFocus(true);
   }
   
   /**
    * Sets the style of the label with the given ID to 'display:block'.
    * 
    * @param labelFieldId The ID of the label field to show.
    */
   public static final void showLabel(String labelFieldId) {
      if (labelFieldId != null && !labelFieldId.trim().isEmpty())
         ((Label)PageAssembler.elementToWidget(labelFieldId, PageAssembler.LABEL)).getElement().setAttribute("style","display:block");
   }
   
   /**
    * Sets the style of the label with the given ID to 'display:inline'.
    * 
    * @param labelFieldId The ID of the label field to show.
    */
   public static final void showLabelInline(String labelFieldId) {
      if (labelFieldId != null && !labelFieldId.trim().isEmpty())
         ((Label)PageAssembler.elementToWidget(labelFieldId, PageAssembler.LABEL)).getElement().setAttribute("style","display:inline");
   }
   
   /**
    * Sets the style of the label with the given ID to 'display:none'.
    * 
    * @param labelFieldId The ID of the label field to hide.
    */
   public static final void hideLabel(String labelFieldId) {
      if (labelFieldId != null && !labelFieldId.trim().isEmpty())
         ((Label)PageAssembler.elementToWidget(labelFieldId, PageAssembler.LABEL)).getElement().setAttribute("style","display:none");
   }
   
   /**
    * Sets the given attribute of the given field to the given value
    * 
    * @param labelFieldId The ID of the label field 
    * @param attribute The ID of the attribute
    * @param value The new value of the attribute
    */
   public static final void setLabelAttribute(String labelFieldId, String attribute, String value) {
      if (labelFieldId != null && !labelFieldId.trim().isEmpty())
         ((Label)PageAssembler.elementToWidget(labelFieldId, PageAssembler.LABEL)).getElement().setAttribute(attribute,value);
   }
   
   /**
    * Sets the style of the image with the given ID to the given style.
    * 
    * @param imageFieldId The ID of the image field to hide.
    * @param style The style to set for the image
    */
   public static final void setImageStyle(String imageFieldId, String style) {
      if (imageFieldId != null && !imageFieldId.trim().isEmpty())
         ((Image)PageAssembler.elementToWidget(imageFieldId,PageAssembler.IMAGE)).getElement().setAttribute("style",style);
   }
   
   /**
    * Sets the URL of the image with the given ID to the given value.
    * 
    * @param imageFieldId The ID of the image field to hide.
    * @param url The URL to set for the image
    */
   public static final void setImageUrl(String imageFieldId, String url) {
      if (imageFieldId != null && !imageFieldId.trim().isEmpty())
         ((Image)PageAssembler.elementToWidget(imageFieldId,PageAssembler.IMAGE)).setUrl(url);
   }
   
   /**
    * Sets the style of the image with the given ID to 'display:none'.
    * 
    * @param imageFieldId The ID of the image field to hide.
    */
   public static final void hideImage(String imageFieldId) {
      setImageStyle(imageFieldId,"display:none");
   }
   
   /**
    * Sets the style of the button with the given ID to the given style.
    * 
    * @param buttonFieldId The ID of the button field to show.
    */
   public static final void setButtonStyle(String buttonFieldId, String style) {
      if (buttonFieldId != null && !buttonFieldId.trim().isEmpty())
         ((Button)PageAssembler.elementToWidget(buttonFieldId, PageAssembler.BUTTON)).getElement().setAttribute("style",style);
   }
   
   
   /**
    * Sets the style of the button with the given ID to 'display:block'.
    * 
    * @param buttonFieldId The ID of the button field to show.
    */
   public static final void showButton(String buttonFieldId) {
      setButtonStyle(buttonFieldId,"display:block");
   }
   
   /**
    * Sets the style of the button with the given ID to 'display:inline'.
    * 
    * @param buttonFieldId The ID of the button field to show.
    */
   public static final void showButtonInline(String buttonFieldId) {
      setButtonStyle(buttonFieldId,"display:inline");
   }
   
   /**
    * Sets the style of the button with the given ID to 'display:none'.
    * 
    * @param buttonFieldId The ID of the button field to hide.
    */
   public static final void hideButton(String buttonFieldId) {
      setButtonStyle(buttonFieldId,"display:none");
   }
   
   

   /**
    * Builds and returns a Learning Registry 'by doc id' request URL for the given LR doc ID 
    * 
    * @param lrDocId The Learning Registry doc ID for which to build the URL
    * @return Returns a Learning Registry 'by doc id' request URL for the given LR doc ID
    */
   public static String buildLearningRegistryLink(String lrDocId) {
      return DsSession.getInstance().getLrPublishNode() + "harvest/getrecord?request_ID=" + lrDocId + "&by_doc_ID=true";
   }
   
   /**
    * Clears all widgets from the widget container and displays the given error message in a simple error widget placed in the given error widget container.
    * (Say that fast!)
    * 
    * This call removes all other widgets on the display container.
    * 
    * @param errorWidgetContainer The container in which to place the widget.
    * @param errorMessage The message to display.
    */
   public static final void showSimpleErrorMessage(String errorWidgetContainer, String errorMessage) {
      showSimpleErrorMessage(errorWidgetContainer, errorMessage, true);
   }

   /**
    * Returns an appropriate length filename for messages
    * 
    * @param fileName The file name
    * @return Returns an appropriate length filename for messages
    */
   public static String getMessageFileName(String fileName) {
      if (fileName == null || fileName.trim().isEmpty()) return "";
      if (fileName.trim().length() <= MAX_FILENAME_MESSAGE_LENGTH) return fileName.trim();
      return fileName.trim().substring(0,MAX_FILENAME_MESSAGE_LENGTH) + "...";
   }
   
   /**
    * Generates a message based on the given criteria and adds it to the given message container.  
    * A DsMessageCloser is added to the ONCLICK event of the close button.
    * 
    * @param messageContainer The container on which to append the message
    * @param messageId  The ID to use for the message
    * @param messageText The text to add to the message
    * @param messageClass The class to use for the message
    * @param clsBtnPrefix The prefix to use for the close button.  (null is valid).
    * @param imgSrc The image source to put in the message.  If imgSrc is null, then no image will be added.
    * @return Returns the generated DsMessage containing the message HTML and close button ID.
    */
   public static DsMessage generateMessage(String messageContainer, String messageId, String messageText, String messageClass, String clsBtnPrefix, String imgSrc) {
      String closeBtnId = DsUtil.generateId(clsBtnPrefix);
      StringBuffer sb = new StringBuffer();
      sb.append("<div id=" + messageId + " data-alert class=\"" + messageClass + "\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");      
      sb.append(messageText);
      if (imgSrc != null && !imgSrc.trim().isEmpty()) sb.append("<img src=\"" + imgSrc + "\" style=\"display:inline\"/>");
      sb.append("<a id=\"" + closeBtnId + "\" href=\"#\" style=\"padding:11px 1px;\" class=\"close\">&times;</a>");      
      sb.append("</div>");
      sb.append("</div>");   
      sb.append("</div>");
      DsMessage dm = new DsMessage(sb.toString(), closeBtnId); 
      RootPanel.get(messageContainer).add(new HTML(dm.getHtml()));
      PageAssembler.attachHandler(dm.getCloseBtnId(),Event.ONCLICK,new DsMessageCloser(messageId));
      return dm;
   }
   
   public static native JavaScriptObject slideDownElement(com.google.gwt.dom.client.Element e, double duration, Callback cb) /*-{
			$wnd.$(e).slideDown(duration, function(){
			if(cb != undefined){
				cb.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(undefined);
			}
		});
	}-*/;
   
   public static native JavaScriptObject slideDownElement(com.google.gwt.dom.client.Element e, Callback cb) /*-{
   		$wnd.$(e).slideDown("slow", function(){
			if(cb != undefined){
				cb.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(undefined);
			}
		});
	}-*/;

	public static native JavaScriptObject fadeOutElement(com.google.gwt.dom.client.Element e, Callback cb) /*-{
		return $wnd.$(e).fadeOut("slow", function(){
			if(cb != undefined){
				cb.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(undefined);
			}
		});
	}-*/;

	public static native JavaScriptObject slideUpElement(com.google.gwt.dom.client.Element e) /*-{
		return $wnd.$(e).slideUp("slow");
	}-*/;
	
	public static native JavaScriptObject slideUpElement(com.google.gwt.dom.client.Element e, double duration, Callback cb) /*-{
		return $wnd.$(e).slideUp(duration,  function(){
			if(cb != undefined){
				cb.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(undefined);
			}
		});
	}-*/;
	
	public static native JavaScriptObject slideUpElement(com.google.gwt.dom.client.Element e, Callback cb) /*-{
		return $wnd.$(e).slideUp("slow",  function(){
			if(cb != undefined){
				cb.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(undefined);
			}
		});
	}-*/;
	
	public static native JavaScriptObject alert(String str) /*-{
		return $wnd.alert(str);
	}-*/;
	
	public static native JavaScriptObject scrollToTop(com.google.gwt.dom.client.Element e) /*-{
		return $wnd.$(e).scrollTop();
	}-*/;

}
