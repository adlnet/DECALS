package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.Group;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Assists with building group views.
 * 
 * @author Eduworks Corporation
 *
 */
public class GroupsViewBuilder {
   
   //Quite a few duplicate constants/element IDs here.  Again, need to figure out a better way of handling/storing element IDs from all pages.
   //The way it is now is very cumbersome.
   
   public static final int UNLIMITED = -1;   
   
   private static final String TOOLS_CLASS = "tools";
   
   private static final String GRP_UL_NAV_CLASS = "side-nav";
   private static final String GRP_UL_NAV_ROLE = "navigation";
   private static final String GRP_UL_NAV_TITLE = "Group Selection";
   private static final String GRP_IL_NAV_ROLE = "menuitem";
   
   private static final String GRP_SEL_PREFIX = "grpSel-";
   
   private static final String EMPTY_CLASS = "curGrpEmpty";
   private static final String EMPTY_USER_TEXT = "No users have been added to this group.";
   
   private static final String GRP_USER_CONTAINER = "cgrpUsers";
   private static final String GRP_USER_SEARCH_PLACEHOLDER = "Find Group User";
   private static final String GRP_USER_NAME_CLASS = "cgrpUserName";
   private static final String GRP_USER_EMAIL_CLASS = "cgrpUserEmail";
   private static final String GRP_USER_TOOLS_PREFIX = "cgrpUserTools-";
   private static final String GRP_USER_REMOVE_CLASS = "cgrpUserRemove";
   private static final String GRP_USER_REMOVE_PREFIX = "cgrpUserRemove-";
   private static final String GRP_USER_REMOVE_TITLE = "Remove group user";
   
   private static final String AGU_CONTAINER = "addGroupUserList";
   private static final String AGU_USER_NAME_CLASS = "aguUserName";
   private static final String AGU_USER_ID_CLASS = "aguUserId";
   private static final String AGU_SEARCH_PLACEHOLDER = "Find New Group Users";
   private static final String AGU_TOOLS_PREFIX = "acuTools-";
   private static final String AGU_ADD_CLASS = "acuAddBtn";
   private static final String AGU_ADD_PREFIX = "acuAdd-";
   private static final String AGU_ADD_TITLE = "Add to group";
   
   
   //builds the empty user statement
   private static String buildEmptyUserStatement() {
      StringBuffer sb = new StringBuffer();
      sb.append("<p class=\"" + EMPTY_CLASS + "\">");
      sb.append(EMPTY_USER_TEXT);      
      sb.append("</p>");      
      return sb.toString();
   }
   
   //generates an HTML line item for the given group user
   private static String generateGroupUserLineItem(AppUser guser, HashMap<String,AppUser> removeWidgets, String toolsPrefix, String revokePrefix, 
         String nameClass, String emailClass, String removeClass, String removeTitle) {
      String toolsId = DsUtil.generateId(toolsPrefix);
      String removeId = DsUtil.generateId(revokePrefix);     
      removeWidgets.put(removeId,guser);
      StringBuffer sb = new StringBuffer();
      sb.append("<li onmouseover=\"document.getElementById('" + toolsId + "').style.display='block';\" onmouseleave=\"document.getElementById('" + toolsId + "').style.display='none';\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-10 columns\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<h5 class=\"" + nameClass + "\"><i class=\"fa fa-user\" style=\"color: #008cba;\"></i>&nbsp;&nbsp;&nbsp;");
      sb.append(guser.getFullName() + "</h5>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + emailClass + "\">" + guser.getEmailAddress() + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      sb.append("<div id=\"" + toolsId + "\" class=\"" + removeClass + "\" style=\"display:none\">");
      sb.append("<span class=\"" + TOOLS_CLASS + "\">");      
      sb.append("<a id=\"" + removeId + "\" class=\"button tiny alert\" title=\"" + removeTitle + "\" ><i class=\"fa fa-trash-o\"></i></a>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");
      return sb.toString();
   }
   
   /**
    * Populates the group data into the page view within the given parent element
    * 
    * @param parentElemId The ID of the parent element
    * @param group The group to use for population
    * @param groupUserDeleteWidgets The group user delete widget register
    */
   public static void populateGroupData(String parentElemId, Group group, HashMap<String,AppUser> groupUserDeleteWidgets) {
      DsUtil.removeAllChildrenFromElement(parentElemId);      
      if (group.getNumberofUsers() <= 0) RootPanel.get(parentElemId).add(new HTML(buildEmptyUserStatement()));
      else {
         StringBuffer sb = new StringBuffer();
         sb.append(ViewBuilderHelper.buildListHeader(GRP_USER_CONTAINER, GRP_USER_SEARCH_PLACEHOLDER));
         for (AppUser gu: group.getGroupUsers()) {
            sb.append(generateGroupUserLineItem(gu,groupUserDeleteWidgets,GRP_USER_TOOLS_PREFIX,GRP_USER_REMOVE_PREFIX,GRP_USER_NAME_CLASS,GRP_USER_EMAIL_CLASS,
                  GRP_USER_REMOVE_CLASS,GRP_USER_REMOVE_TITLE));      
         }
         sb.append(ViewBuilderHelper.buildListFooter());
         RootPanel.get(parentElemId).add(new HTML(sb.toString()));
      }
      
   }
   
   //builds the group selection line item
   private static String buildGroupSelectionLineItem(Group group, HashMap<String,Group> selectWidgets) {
      String selectId = DsUtil.generateId(GRP_SEL_PREFIX);
      selectWidgets.put(selectId,group);
      StringBuffer sb = new StringBuffer();
      sb.append("<li role=\"" + GRP_IL_NAV_ROLE + "\">");
      sb.append("<a id=\"" + selectId + "\">");
      sb.append("<i class=\"fa fa-chevron-circle-right\"></i> ");
      sb.append(group.getName());
      sb.append("</a>");
      sb.append("</li>");
      return sb.toString();
   }
   
   //handle the more/less links
   private static void handleMoreLessLinks(String showMorePanelId, String showLessPanelId, long groupSize, long numberOfEntries) {
      if (groupSize <= numberOfEntries) {
         DsUtil.hideLabel(showMorePanelId);
         DsUtil.hideLabel(showLessPanelId);
      }
      else if (numberOfEntries == UNLIMITED || groupSize == numberOfEntries) {
         DsUtil.hideLabel(showMorePanelId);
         DsUtil.showLabel(showLessPanelId);
      }
      else {
         DsUtil.hideLabel(showLessPanelId);
         DsUtil.showLabel(showMorePanelId);
      }
   }
   
   /**
    * Builds the group navigation view HTML and attaches it to the given parent panel.
    * 
    * If {@link GroupsViewBuilder#UNLIMITED} is passed as the number of entries, navigation pointers
    * for all groups will be added.
    * 
    * @param navContainer The ID of the main navigation container
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param showMorePanelId The ID of the 'show more' panel
    * @param showLessPanelId The ID of the 'show less' panel
    * @param groupList The group list
    * @param numberOfEntries The number groups to show navigation pointers
    * @param selectWidgets The select group widget register
    */
   public static void buildGroupNavigation(String navContainer, String parentPanelId, String showMorePanelId, String showLessPanelId, ArrayList<Group> groupList, int numberOfEntries, 
         HashMap<String,Group> selectWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);
      if (groupList.size() <= 0) DsUtil.hideLabel(navContainer);
      else {
         DsUtil.showLabel(navContainer);
         int toNum = numberOfEntries;
         if (numberOfEntries == UNLIMITED || numberOfEntries >= groupList.size()) toNum = groupList.size();
         StringBuffer sb = new StringBuffer();      
         sb.append("<ul class=\"" + GRP_UL_NAV_CLASS + "\" role=\"" + GRP_UL_NAV_ROLE + "\" title=\"" + GRP_UL_NAV_TITLE + "\">");            
         for (int i=0;i<toNum;i++) sb.append(buildGroupSelectionLineItem(groupList.get(i),selectWidgets));      
         sb.append("</ul>");
         RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
         handleMoreLessLinks(showMorePanelId,showLessPanelId,groupList.size(),numberOfEntries);
      }
   }
   
   /**
    * Builds the add new group user view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param addGroupUserList The list of potential new group users
    * @param newGroupUserWidgets The new group user widget register
    */
   public static void buildNewGroupUserList(String parentPanelId, ArrayList<AppUser> addGroupUserList, HashMap<String,AppUser> newGroupUserWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(AGU_CONTAINER, AGU_SEARCH_PLACEHOLDER));      
      for (AppUser pnu: addGroupUserList) {
         sb.append(ViewBuilderHelper.generateUserPickListLineItem(pnu,newGroupUserWidgets,AGU_TOOLS_PREFIX,AGU_ADD_PREFIX, 
               AGU_USER_NAME_CLASS,AGU_USER_ID_CLASS,AGU_ADD_CLASS,AGU_ADD_TITLE,true)); 
      }            
      sb.append(ViewBuilderHelper.buildListFooter());     
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
}
