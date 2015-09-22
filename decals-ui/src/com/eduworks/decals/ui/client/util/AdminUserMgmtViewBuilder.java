package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.ParadataPublicationInfo;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Helps with application administration and user management views.
 * 
 * Was going to separate these into two separate helpers but there seems like a lot of functionality overlap.
 * 
 * @author Eduworks Corporation
 *
 */
public class AdminUserMgmtViewBuilder {
   
   //Quite a few duplicate constants/element IDs here.  Again, need to figure out a better way of handling/storing element IDs from all pages.
   //The way it is now is very cumbersome.
   
   private static final String TOOLS_CLASS = "tools";
   
   private static final String ADMIN_CONTAINER = "daaAministrators";
   private static final String ADMIN_SEARCH_PLACEHOLDER = "Find Administrator";
   private static final String ADMIN_NAME_CLASS = "daaAdminName";
   private static final String ADMIN_EMAIL_CLASS = "daaAdminEmail";
   private static final String ADMIN_TOOLS_PREFIX = "daaAdminTools-";
   private static final String ADMIN_REVOKE_CLASS = "daaAdminRevoke";
   private static final String ADMIN_REVOKE_PREFIX = "daaRevokeAdmin-";
   private static final String ADMIN_REVOKE_TITLE = "Revoke administrator privileges";
   private static final String ADMIN_LAST_LOGIN_CLASS = "daaLastLogin";
   
   private static final String PPUB_CONTAINER = "daaParadataPublishings";
   private static final String PPUB_SEARCH_PLACEHOLDER = "Find Publish Records";
   private static final String PPUB_TITLE_CLASS = "daaPpubTitle";
   private static final String PPUB_URL_CLASS = "daaPpubUrl";
   private static final String PPUB_LAST_PUB_DATE_CLASS = "daaPpubDate";
   private static final String PPUB_LR_ID_CLASS = "daaPpubId";
   private static final String PPUB_TOOLS_PREFIX = "daaPPubTools-";
   private static final String PPUB_PUBLISH_CLASS = "daaPPubPublish";
   private static final String PPUB_PUBLISH_PREFIX = "daaPublishParadata-";
   private static final String PPUB_PUBLISH_TITLE = "Publish resource paradata";
   
   private static final String NEW_ADMIN_CONTAINER = "addAdminUserList";
   private static final String NEW_ADMIN_SEARCH_PLACEHOLDER = "Find New Administrator";
   private static final String NEW_ADMIN_NAME_CLASS = "addAdminUserName";
   private static final String NEW_ADMIN_USERID_CLASS = "addAdminUserId";
   private static final String NEW_ADMIN_TOOLS_PREFIX = "addAdminTools-";
   private static final String NEW_ADMIN_ADD_CLASS = "addAdminAddBtn";
   private static final String NEW_ADMIN_ADD_PREFIX = "addAdminAdd-";
   private static final String NEW_ADMIN_ADD_TITLE = "Grant administrator privileges";   
   
   private static final String USER_CONTAINER = "umUserList";
   private static final String USER_SEARCH_PLACEHOLDER = "Find User";
   private static final String USER_NAME_CLASS = "umUserName";
   private static final String USER_EMAIL_CLASS = "umUserEmail"; 
   private static final String USER_ROLES_CLASS = "umUserRoles";
   private static final String USER_CREATE_DATE_CLASS = "umUserCreateDate";
   private static final String USER_LAST_LOGIN_CLASS = "umUserLastLogin";
   private static final String USER_TOOLS_PREFIX = "umUserTools-";   
   private static final String USER_TOOLS_CLASS = "umUserTools";
   private static final String USER_RESET_PWD_PREFIX = "umUserResetUserPwd-";
   private static final String USER_RESET_PWD_TITLE = "Reset user password";   
   private static final String USER_DELETE_PREFIX = "umUserDeleteUser-";
   private static final String USER_DELETE_TITLE = "Delete user";
   
   private static final String USER_MGR_CONTAINER = "umUserMgrList";
   private static final String USER_MGR_SEARCH_PLACEHOLDER = "Find User Manager";
   private static final String USER_MGR_NAME_CLASS = "umUserMgrName";
   private static final String USER_MGR_EMAIL_CLASS = "umUserMgrEmail";
   private static final String USER_MGR_TOOLS_PREFIX = "umUserMgrTools-";
   private static final String USER_MGR_REVOKE_CLASS = "umUserMgrRevoke";
   private static final String USER_MGR_REVOKE_PREFIX = "umUserMgrRevokeAdmin-";
   private static final String USER_MGR_REVOKE_TITLE = "Revoke user manager privileges";
   private static final String USER_MGR_LAST_LOGIN_CLASS = "umUserMgrLastLogin";
   
   private static final String NEW_USER_MGR_CONTAINER = "addUserMgrUserList";
   private static final String NEW_USER_MGR_SEARCH_PLACEHOLDER = "Find New User Manager";
   private static final String NEW_USER_MGR_NAME_CLASS = "addUserMgrUserName";
   private static final String NEW_USER_MGR_USERID_CLASS = "addUserMgrUserId";
   private static final String NEW_USER_MGR_TOOLS_PREFIX = "addUserMgrTools-";
   private static final String NEW_USER_MGR_ADD_CLASS = "addUserMgrAddBtn";
   private static final String NEW_USER_MGR_ADD_PREFIX = "addUserMgrAdd-";
   private static final String NEW_USER_MGR_ADD_TITLE = "Grant user manager privileges";
   
   private static final String DUAT_CONTAINER = "duatUserList";
   private static final String DUAT_SEARCH_PLACEHOLDER = "Find Transfer User";
   private static final String DUAT_NAME_CLASS = "duatUserName";
   private static final String DUAT_USERID_CLASS = "duatUserId";
   private static final String DUAT_TOOLS_PREFIX = "duatTools-";
   private static final String DUAT_ADD_CLASS = "duatAddBtn";
   private static final String DUAT_ADD_PREFIX = "duatAdd-";
   private static final String DUAT_ADD_TITLE = "Transfer to this user ";
   
   //generates an HTML line item for the given privileged user
   private static String generatePrivilegedUserLineItem(AppUser puser, HashMap<String,AppUser> revokeWidgets, String toolsPrefix, String revokePrefix, 
         String nameClass, String emailClass, String lastLoginClass, String revokeClass, String revokeTitle) {
      String toolsId = DsUtil.generateId(toolsPrefix);
      String revokeId = DsUtil.generateId(revokePrefix);     
      revokeWidgets.put(revokeId,puser);
      StringBuffer sb = new StringBuffer();
      sb.append("<li onmouseover=\"document.getElementById('" + toolsId + "').style.display='block';\" onmouseleave=\"document.getElementById('" + toolsId + "').style.display='none';\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-10 columns\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<h5 class=\"" + nameClass + "\"><i class=\"fa fa-user\" style=\"color: #008cba;\"></i>&nbsp;&nbsp;&nbsp;");
      sb.append(puser.getFullName() + "</h5>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + emailClass + "\">" + puser.getEmailAddress() + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + lastLoginClass + "\">Last Login Date: " + puser.getLastLoginDateStr() + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      sb.append("<div id=\"" + toolsId + "\" class=\"" + revokeClass + "\" style=\"display:none\">");
      sb.append("<span class=\"" + TOOLS_CLASS + "\">");      
      sb.append("<a id=\"" + revokeId + "\" class=\"button tiny alert\" title=\"" + revokeTitle + "\" ><i class=\"fa fa-trash-o\"></i></a>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");
      return sb.toString();
   }
   
   /**
    * Builds the administrator view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param adminList The list of administrators
    * @param revokeWidgets The revoke widget register
    */
   public static void buildAdministratorList(String parentPanelId, ArrayList<AppUser> adminList, HashMap<String,AppUser> revokeWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(ADMIN_CONTAINER, ADMIN_SEARCH_PLACEHOLDER));
      for (AppUser admin: adminList) {
         sb.append(generatePrivilegedUserLineItem(admin,revokeWidgets,ADMIN_TOOLS_PREFIX,ADMIN_REVOKE_PREFIX,ADMIN_NAME_CLASS,ADMIN_EMAIL_CLASS,
               ADMIN_LAST_LOGIN_CLASS,ADMIN_REVOKE_CLASS,ADMIN_REVOKE_TITLE));      
      }
      sb.append(ViewBuilderHelper.buildListFooter());
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
   /**
    * Builds the user manager view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param userManagerList The list of user mangers
    * @param revokeWidgets The revoke user manager widget register
    */
   public static void buildUserManagerList(String parentPanelId, ArrayList<AppUser> userManagerList, HashMap<String,AppUser> revokeWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(USER_MGR_CONTAINER, USER_MGR_SEARCH_PLACEHOLDER));
      for (AppUser um: userManagerList) {
         sb.append(generatePrivilegedUserLineItem(um,revokeWidgets,USER_MGR_TOOLS_PREFIX,USER_MGR_REVOKE_PREFIX,USER_MGR_NAME_CLASS,USER_MGR_EMAIL_CLASS,
               USER_MGR_LAST_LOGIN_CLASS,USER_MGR_REVOKE_CLASS,USER_MGR_REVOKE_TITLE));      
      }
      sb.append(ViewBuilderHelper.buildListFooter());
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
   //generates a display string for user roles
   private static String generateUserRolesEntry(AppUser user) {
      if (user.getRoles() == null || user.getRoles().size() == 0) return "none";      
      return DsUtil.buildCommaStringFromHashMap(user.getRoles());
   }
   
   //generates an HTML line item for the give user
   private static String generateUserLineItem(AppUser user, HashMap<String,AppUser> resetPasswordWidgets, HashMap<String,AppUser> deleteUserWidgets) {
      String toolsId = DsUtil.generateId(USER_TOOLS_PREFIX);
      String resetPwdId = DsUtil.generateId(USER_RESET_PWD_PREFIX);
      String deleteUserId = DsUtil.generateId(USER_DELETE_PREFIX);
      resetPasswordWidgets.put(resetPwdId,user);
      deleteUserWidgets.put(deleteUserId,user);
      StringBuffer sb = new StringBuffer();
      sb.append("<li onmouseover=\"document.getElementById('" + toolsId + "').style.display='block';\" onmouseleave=\"document.getElementById('" + toolsId + "').style.display='none';\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-9 columns\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<h5 class=\"" + USER_NAME_CLASS + "\"><i class=\"fa fa-user\" style=\"color: #008cba;\"></i>&nbsp;&nbsp;&nbsp;");
      sb.append(user.getFullName() + "</h5>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + USER_EMAIL_CLASS + "\">" + user.getEmailAddress() + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + USER_ROLES_CLASS + "\">Roles: " + generateUserRolesEntry(user) + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + USER_CREATE_DATE_CLASS + "\">Create Date: " + user.getCreateDateStr() + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + USER_LAST_LOGIN_CLASS + "\">Last Login Date: " + user.getLastLoginDateStr() + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"small-3 columns\">");
      sb.append("<div id=\"" + toolsId + "\" class=\"" + USER_TOOLS_CLASS + "\" style=\"display:none\">");
      sb.append("<span class=\"" + TOOLS_CLASS + "\" style=\"padding-right:10px;\">");
      sb.append("<a id=\"" + resetPwdId + "\" class=\"button tiny secondary\" title=\"" + USER_RESET_PWD_TITLE + "\" ><i class=\"fa fa-refresh\"></i></a>");
      sb.append("&nbsp;");
      sb.append("<a id=\"" + deleteUserId + "\" class=\"button tiny alert\" title=\"" + USER_DELETE_TITLE + "\" ><i class=\"fa fa-trash-o\"></i></a>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");
      return sb.toString();
   }
   
   /**
    * Builds the user view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param userList The list of users
    * @param resetPasswordWidgets The reset password widget register
    * @param deleteUserWidgets The delete user widget register
    */
   public static void buildUserList(String parentPanelId, ArrayList<AppUser> userList, HashMap<String,AppUser> resetPasswordWidgets, HashMap<String,AppUser> deleteUserWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(USER_CONTAINER, USER_SEARCH_PLACEHOLDER));      
      for (AppUser user: userList) sb.append(generateUserLineItem(user,resetPasswordWidgets,deleteUserWidgets));
      sb.append(ViewBuilderHelper.buildListFooter());
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
   //builds a learning registry link anchor tag for the given paradata publication record
   private static String buildLRLinkAnchor(ParadataPublicationInfo ppi) {
      if (ppi.getLrDocId() == null || ppi.getLrDocId().trim().isEmpty()) return "not yet published";
      StringBuffer sb = new StringBuffer();
      sb.append("<a target=\"" + ppi.getResourceTitle() + " paradata at the Learning Registry\"");
      sb.append(" href=\"" + DsUtil.buildLearningRegistryLink(ppi.getLrDocId()) + "\" ");
      sb.append(" title=\"View '" + ppi.getResourceTitle() + "' paradata in the Learning Registry\" >");
      sb.append(ppi.getLrDocId());
      sb.append("</a>");
       return sb.toString();
   }
   
   //generates an HTML line item for the give paradata publication information
   private static String generateParaPubLineItem(ParadataPublicationInfo ppi, HashMap<String,ParadataPublicationInfo> publishWidgets) {
      String toolsId = DsUtil.generateId(PPUB_TOOLS_PREFIX);
      String publishId = DsUtil.generateId(PPUB_PUBLISH_PREFIX);
      publishWidgets.put(publishId,ppi);
      StringBuffer sb = new StringBuffer();
      sb.append("<li onmouseover=\"document.getElementById('" + toolsId + "').style.display='block';\" onmouseleave=\"document.getElementById('" + toolsId + "').style.display='none';\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-10 columns\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<h5 class=\"" + PPUB_TITLE_CLASS + "\"><i class=\"fa fa-book\" style=\"color: #008cba;\"></i>&nbsp;&nbsp;&nbsp;");
      sb.append(ppi.getResourceTitle() + "</h5>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + PPUB_URL_CLASS + "\">" + ppi.getResourceUrl() + "</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + PPUB_LAST_PUB_DATE_CLASS + "\">");
      sb.append("Last Publish Date: " + ppi.getLastPublishDateStr());
      sb.append("</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-12 columns\">");
      sb.append("<label class=\"" + PPUB_LR_ID_CLASS + "\">");
      sb.append("Learning Registry ID: " + buildLRLinkAnchor(ppi));
      sb.append("</label>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      sb.append("<div id=\"" + toolsId + "\" class=\"" + PPUB_PUBLISH_CLASS + "\" style=\"display:none\">");
      sb.append("<span class=\"" + TOOLS_CLASS + "\">");
      sb.append("<a id=\"" + publishId + "\" class=\"button tiny\" title=\"" + PPUB_PUBLISH_TITLE + "\" ><i class=\"fa fa-globe\"></i></a>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");      
      return sb.toString();
   }
   
   /**
    * Builds the paradata publishing information view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param paraPubInfoList The list of paradata publication info
    * @param publishWidgets The publish widget register
    */
   public static void buildParadataPublishingInfoList(String parentPanelId, ArrayList<ParadataPublicationInfo> paraPubInfoList, HashMap<String,ParadataPublicationInfo> publishWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(PPUB_CONTAINER, PPUB_SEARCH_PLACEHOLDER));      
      for (ParadataPublicationInfo ppi: paraPubInfoList) sb.append(generateParaPubLineItem(ppi,publishWidgets));
      sb.append(ViewBuilderHelper.buildListFooter());      
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
   /**
    * Builds the add new administrator view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param addAdminUserList The list of potential new administrators
    * @param newAdminWidgets The new administrator widget register
    */
   public static void buildNewAdminUserList(String parentPanelId, ArrayList<AppUser> addAdminUserList, HashMap<String,AppUser> newAdminWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(NEW_ADMIN_CONTAINER, NEW_ADMIN_SEARCH_PLACEHOLDER));      
      for (AppUser pna: addAdminUserList) {
         sb.append(ViewBuilderHelper.generateUserPickListLineItem(pna,newAdminWidgets,NEW_ADMIN_TOOLS_PREFIX,NEW_ADMIN_ADD_PREFIX, 
               NEW_ADMIN_NAME_CLASS,NEW_ADMIN_USERID_CLASS,NEW_ADMIN_ADD_CLASS,NEW_ADMIN_ADD_TITLE)); 
      }            
      sb.append(ViewBuilderHelper.buildListFooter());     
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
   /**
    * Builds the add new user manager view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param addUserMgrUserList The list of potential new user managers
    * @param newUserMgrWidgets The new user manager widget register
    */
   public static void buildNewUserManagerUserList(String parentPanelId, ArrayList<AppUser> addUserMgrUserList, HashMap<String,AppUser> newUserMgrWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(NEW_USER_MGR_CONTAINER, NEW_USER_MGR_SEARCH_PLACEHOLDER));      
      for (AppUser pnum: addUserMgrUserList) {
         sb.append(ViewBuilderHelper.generateUserPickListLineItem(pnum,newUserMgrWidgets,NEW_USER_MGR_TOOLS_PREFIX,NEW_USER_MGR_ADD_PREFIX, 
               NEW_USER_MGR_NAME_CLASS,NEW_USER_MGR_USERID_CLASS,NEW_USER_MGR_ADD_CLASS,NEW_USER_MGR_ADD_TITLE)); 
      }            
      sb.append(ViewBuilderHelper.buildListFooter());     
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }

   /**
    * Builds the delete user transfer resources view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param deleteUserTransferUserList The list of potential transfers
    * @param deleteUserTransferWidgets The delete user transfer widget register
    */
   public static void buildNewDeleteUserTransferUserList(String parentPanelId, ArrayList<AppUser> deleteUserTransferUserList, HashMap<String,AppUser> deleteUserTransferWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(DUAT_CONTAINER, DUAT_SEARCH_PLACEHOLDER));      
      for (AppUser tu: deleteUserTransferUserList) {
         sb.append(ViewBuilderHelper.generateUserPickListLineItem(tu,deleteUserTransferWidgets,DUAT_TOOLS_PREFIX,DUAT_ADD_PREFIX, 
               DUAT_NAME_CLASS,DUAT_USERID_CLASS,DUAT_ADD_CLASS,DUAT_ADD_TITLE)); 
      }            
      sb.append(ViewBuilderHelper.buildListFooter());     
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
}
