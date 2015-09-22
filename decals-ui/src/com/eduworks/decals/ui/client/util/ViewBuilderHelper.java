package com.eduworks.decals.ui.client.util;

import java.util.HashMap;

import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.Group;

/**
 * Various methods used by other view builders.
 * 
 * @author Eduworks Corporation
 *
 */
public class ViewBuilderHelper {
   
   private static final String SEARCH_BOX_CLASS = "search";
   private static final int SEARCH_BOX_WIDTH = 250;
   private static final String LIST_CLASS = "list";
   //private static final String PAGINATION_CLASS = "pagination";
   private static final String TOOLS_CLASS = "tools";
   
   //builds the standard list header
   public static String buildListHeader(String containerName, String searchPlaceHolder) {
      StringBuffer sb = new StringBuffer();
      sb.append("<div id=\"" + containerName + "\">");
      sb.append("<input class=\"" + SEARCH_BOX_CLASS + "\" placeholder=\"" + searchPlaceHolder + "\"  style=\"width:" + SEARCH_BOX_WIDTH + "px\">");
      sb.append("<br><br>");
      sb.append("<ul class=\"" + LIST_CLASS + "\">");
      return sb.toString();
   }
   
   //builds the standard list footer
   public static String buildListFooter() {
      StringBuffer sb = new StringBuffer();
      sb.append("</ul>");
      //sb.append("<ul class=\"" + PAGINATION_CLASS + "\"></ul>");
      sb.append("</div>");
      return sb.toString();
   }
   
   //generates an HTML line item for a user pick list
   public static String generateUserPickListLineItem(AppUser user, HashMap<String,AppUser> addNewWidgets, String toolsPrefix, 
         String addBtnPrefix, String nameClass, String userIdClass, String addBtnClass, String addBtnTitle) {
      return generateUserPickListLineItem(user,addNewWidgets,toolsPrefix,addBtnPrefix,nameClass,userIdClass,addBtnClass,addBtnTitle,false);
   }
   
   //generates an HTML line item for a user pick list
   public static String generateUserPickListLineItem(AppUser user, HashMap<String,AppUser> addNewWidgets, String toolsPrefix, 
         String addBtnPrefix, String nameClass, String userIdClass, String addBtnClass, String addBtnTitle, boolean multiChoice) {
      String toolsId = DsUtil.generateId(toolsPrefix);
      String addId = DsUtil.generateId(addBtnPrefix);     
      addNewWidgets.put(addId,user);
      StringBuffer sb = new StringBuffer();
      if (multiChoice) sb.append("<li>");
      else sb.append("<li onmouseover=\"document.getElementById('" + toolsId + "').style.display='block';\" onmouseleave=\"document.getElementById('" + toolsId + "').style.display='none';\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-5 columns\">");
      sb.append("<label class=\"" + nameClass + "\"><i class=\"fa fa-user\" style=\"color: #008cba;\"></i>&nbsp;&nbsp;&nbsp;");
      sb.append(user.getFullName() + "</label>");
      sb.append("</div>");
      sb.append("<div class=\"small-5 columns\">");
      sb.append("<label class=\"" + userIdClass + "\">" + user.getUserId() + "</label>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      if (multiChoice) sb.append("<div id=\"" + toolsId + "\" class=\"" + addBtnClass + "\">");
      else sb.append("<div id=\"" + toolsId + "\" class=\"" + addBtnClass + "\" style=\"display:none\">");
      sb.append("<span class=\"" + TOOLS_CLASS + "\">");
      if (multiChoice) sb.append("<input id=\"" + addId + "\" type=\"checkbox\" title=\"" + addBtnTitle + "\"><label for=\"" + addId + "\"><b>Add</b></label>");
      else sb.append("<a id=\"" + addId + "\" class=\"button tiny\" title=\"" + addBtnTitle + "\" style=\"margin: 0px\"><i class=\"fa fa-plus\"></i></a>");        
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");      
      sb.append("</div>");
      sb.append("</li>");
      return sb.toString();
   }
   
   //generates an HTML line item for a group pick list
   public static String generateGroupPickListLineItem(Group group, HashMap<String,Group> addNewWidgets, String toolsPrefix, 
         String addBtnPrefix, String nameClass, String groupTypeClass, String addBtnClass, String addBtnTitle) {
      return generateGroupPickListLineItem(group,addNewWidgets,toolsPrefix,addBtnPrefix,nameClass,groupTypeClass,addBtnClass,addBtnTitle,false);
   }
   
   //generates an HTML line item for a group pick list
   public static String generateGroupPickListLineItem(Group group, HashMap<String,Group> addNewWidgets, String toolsPrefix, 
         String addBtnPrefix, String nameClass, String groupTypeClass, String addBtnClass, String addBtnTitle, boolean multiChoice) {
      String toolsId = DsUtil.generateId(toolsPrefix);
      String addId = DsUtil.generateId(addBtnPrefix);     
      addNewWidgets.put(addId,group);
      StringBuffer sb = new StringBuffer();
      if (multiChoice) sb.append("<li>");
      else sb.append("<li onmouseover=\"document.getElementById('" + toolsId + "').style.display='block';\" onmouseleave=\"document.getElementById('" + toolsId + "').style.display='none';\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-5 columns\">");
      sb.append("<label class=\"" + nameClass + "\"><i class=\"fa fa-users\" style=\"color: #008cba;\"></i>&nbsp;&nbsp;&nbsp;");
      sb.append(group.getName() + "</label>");
      sb.append("</div>");
      sb.append("<div class=\"small-5 columns\">");
      sb.append("<label class=\"" + groupTypeClass + "\">" + group.getGroupType() + "</label>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      if (multiChoice) sb.append("<div id=\"" + toolsId + "\" class=\"" + addBtnClass + "\">");
      else sb.append("<div id=\"" + toolsId + "\" class=\"" + addBtnClass + "\" style=\"display:none\">");
      sb.append("<span class=\"" + TOOLS_CLASS + "\">");
      if (multiChoice) sb.append("<input id=\"" + addId + "\" type=\"checkbox\" title=\"" + addBtnTitle + "\"><label for=\"" + addId + "\"><b>Add</b></label>");
      else sb.append("<a id=\"" + addId + "\" class=\"button tiny\" title=\"" + addBtnTitle + "\" style=\"margin: 0px\"><i class=\"fa fa-plus\"></i></a>");        
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");      
      sb.append("</div>");
      sb.append("</li>");
      return sb.toString();
   }

}
