package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.Collection;
import com.eduworks.decals.ui.client.model.CollectionAccess;
import com.eduworks.decals.ui.client.model.CollectionGroup;
import com.eduworks.decals.ui.client.model.CollectionItem;
import com.eduworks.decals.ui.client.model.CollectionUser;
import com.eduworks.decals.ui.client.model.DarResourceObjective;
import com.eduworks.decals.ui.client.model.Group;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsUserHomeScreen;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Assists with building collection views.
 * 
 * @author Eduworks Corporation
 *
 */
public class CollectionsViewBuilder {

   //Quite a few duplicate constants/element IDs here.  Again, need to figure out a better way of handling/storing element IDs from all pages.
   //The way it is now is very cumbersome.
   
   public static final int UNLIMITED = -1;   
   
   private static final String COL_NAV_CONTAINER = "myCollectionsNavigation";
   
   private static final String COL_UL_NAV_CLASS = "side-nav";
   private static final String COL_UL_NAV_ROLE = "navigation";
   private static final String COL_UL_NAV_TITLE = "Collection Selection";
   private static final String COL_IL_NAV_ROLE = "menuitem";
   
   private static final String COL_SEL_PREFIX = "colSel-";
   
   private static final String CCOL_CHANGED_MESSAGE = "curColChangedMessage";
      
   private static final String CCOL_MOD_COL_CONTAINER = "curColModifyButtons";
   
   private static final String CCOL_ADD_BTN_CONTAINER = "colAddBtns";
   
   private static final String CCOL_NAME = "curColName";
   private static final String CCOL_USER_COUNT = "curColUserCount";   
   private static final String CCOL_ITEM_COUNT = "curColItemCount";
   private static final String CCOL_GROUP_COUNT = "curColGroupCount";
   private static final String CCOL_DESC_CONTAINER = "curColDescContainer";
   private static final String CCOL_DESC_TEXT_AREA_CONTAINER = "curColDescTextAreaContainer";
   private static final String CCOL_DESC_EDIT_CONTAINER = "curColDescEditContainer";   
   private static final String CCOL_DESC = "curColDesc";
   private static final String CCOL_DESC_TEXT_AREA = "curColDescTextArea";   
   
   private static final String CCOL_ITEM_CONTAINER = "curColItemContainer";
   private static final String CCOL_USER_CONTAINER = "curColUserContainer";
   private static final String CCOL_GROUP_CONTAINER = "curColGroupContainer";
   
   private static final String CCOL_ITEM_LIST_NAME = "curColItemList";
   private static final String CCOL_USER_LIST_NAME = "curColUserList";
   private static final String CCOL_GROUP_LIST_NAME = "curColGroupList";
   
   private static final String CCOL_DELETE_ITEM_PREFIX = "ccolDelItem-";
   private static final String CCOL_ITEM_DETAILS_PREFIX = "ccolItemDet-";
   private static final String CCOL_DELETE_USER_PREFIX = "ccolDelUser-";
   private static final String CCOL_DELETE_GROUP_PREFIX = "ccolDelGroup-";
   
   private static final String CCOL_DELETE_ITEM_TITLE = "Delete collection item";
   private static final String CCOL_DELETE_USER_TITLE = "Delete collection user";
   private static final String CCOL_DELETE_GROUP_TITLE = "Delete collection group";
   private static final String CCOL_ITEM_DETAILS_TITLE = "Toggle item details";
   
   public static final String CCOL_ACCESS_DD_SUFFIX = "-colAccess";
   
   private static final String CCOL_SAVE_SUCCESS_MESSAGE = "curColSaveSuccess";
   
   private static final String EMPTY_CLASS = "curColEmpty";
   public static final String NON_EMPTY_CLASS = "curColNonEmpty";  
   private static final String WORD_WRAP_CLASS = "textWrap";
   
   private static final String EMPTY_DESC_TEXT = "No description has been added to this collection.";
   private static final String EMPTY_ITEM_TEXT = "No items have been added to this collection.";   
   private static final String EMPTY_GROUP_TEXT = "No groups have been given permissions on this collection.";
   
   private static final String ITEM_CLASS_TOGGLE_STR = "onmouseover=\"this.className='alert-box info radius';\" onmouseleave=\"this.className='alert-box secondary radius';\"";
   private static final String ONCHANGE_TRIGGER_CHANGE_MESSAGE = "onchange=\"document.getElementById('" + CCOL_CHANGED_MESSAGE + "').style.display='block';\"";
   
   private static final String ACU_CONTAINER = "addCollectionUserList";
   private static final String ACU_USER_NAME_CLASS = "acuUserName";
   private static final String ACU_USER_ID_CLASS = "acuUserId";
   private static final String ACU_SEARCH_PLACEHOLDER = "Find New Collection User";
   private static final String ACU_TOOLS_PREFIX = "acuTools-";
   private static final String ACU_ADD_CLASS = "acuAddBtn";
   private static final String ACU_ADD_PREFIX = "acuAdd-";
   private static final String ACU_ADD_TITLE = "Grant collection access";
   
   private static final String ACG_CONTAINER = "addCollectionGroupList";
   private static final String ACG_GROUP_NAME_CLASS = "acgGroupName";
   private static final String ACG_GROUP_TYPE_CLASS = "acgGroupType";
   private static final String ACG_SEARCH_PLACEHOLDER = "Find New Collection Group";
   private static final String ACG_TOOLS_PREFIX = "acgTools-";
   private static final String ACG_ADD_CLASS = "acgAddBtn";
   private static final String ACG_ADD_PREFIX = "acgAdd-";
   private static final String ACG_ADD_TITLE = "Grant collection access";  
   
   private static final String ARTC_CONTAINER = "addToCollectionList";
   private static final String ARTC_COL_NAME_CLASS = "artcColName";
   private static final String ARTC_SEARCH_PLACEHOLDER = "Find a Collection";
   private static final String ARTC_TOOLS_PREFIX = "artcTools-";
   private static final String ARTC_ADD_CLASS = "artcAddBtn";
   private static final String ARTC_ADD_PREFIX = "artcAdd-";
   private static final String ARTC_ADD_TITLE = "Add resource to this collection";
   
   private static final String TOOLS_CLASS = "tools";
   
   public static final String METADATA_TOGGLE_ID = "curColMetadataToggle";
   public static final String METADATA_CONTAINER_ID = "curColMetadataContainer";
   public static final String METADATA_EDIT_CONTAINER_ID = "curColMetadataEditContainer";
   
   
   public static final String METADATA_VIEW_KEYWORDS = "curColKeywords";
   public static final String METADATA_VIEW_COVERAGE = "curColCoverage";
   public static final String METADATA_VIEW_ENVIRONMENT = "curColEnvironment";
   public static final String METADATA_VIEW_OBJECTIVES = "curColObjectives";
   
   public static final String METADATA_EDIT_KEYWORDS = "curColKeywordsTextArea";
   public static final String METADATA_EDIT_COVERAGE = "curColCoverageInput";
   public static final String METADATA_EDIT_ENVIRONMENT = "curColSelectEnvironment";
   
   public static final String METADATA_EDIT_OBJECTIVES_LIST = "curColObjectivesList";
   public static final String METADATA_EDIT_OBJECTIVES_NONE_ITEM = "noObjectivesItem";
   public static final String METADATA_EDIT_OBJECTIVES_LINK = "addObjectiveLink";
   public static final String METADATA_EDIT_OBJECTIVES_CONTAINER = "editMetadataCreateObjectiveWrapper";
   public static final String METADATA_EDIT_OBJECTIVES_ADD_BTN = "editMetadataCreateObjectiveApply";
   public static final String METADATA_EDIT_OBJECTIVES_CANCEL_BTN = "editMetadataCreateObjectiveCancel";
   public static final String METADATA_EDIT_OBJECTIVES_TITLE_INPUT = "editMetadataNewObjectiveTitle";
   public static final String METADATA_EDIT_OBJECTIVES_DESC_INPUT = "editMetadataNewObjectiveDesc";
   
   public static void showCollectionMetadataView(Collection col){
	   DsUtil.slideDownElement(DOM.getElementById(METADATA_CONTAINER_ID), null);
	   
	   Element toggle = DOM.getElementById(METADATA_TOGGLE_ID);
	   toggle.setAttribute("data-toggle", "hide");
	   toggle.setTitle("Hide Metadata");
	   toggle.setInnerText("(Hide Metadata)");
   }
   
   public static void hideCollectionMetadataView(Collection col){
	   DsUtil.slideUpElement(DOM.getElementById(METADATA_CONTAINER_ID));
	   
	   Element toggle = DOM.getElementById(METADATA_TOGGLE_ID);
	   toggle.setAttribute("data-toggle", "show");
	   toggle.setTitle("Show Metadata");
	   toggle.setInnerText("(Show Metadata)");
   }
   
   public static void toggleShowCollectionMetadata(Collection col){
	   if(DOM.getElementById(METADATA_TOGGLE_ID).getAttribute("data-toggle").equals("show")){
		   showCollectionMetadataView(col);
	   }else{
		  hideCollectionMetadataView(col);
	   }
   }
   
   public static void showEditMetadata(Collection col){
	   setUpCollectionMetadata(col);
	   
	   DsUtil.showLabel(METADATA_EDIT_CONTAINER_ID);
   }
   
   public static void hideEditMetadata(Collection col){
	   setUpCollectionMetadata(col);
	   
	   DsUtil.showLabel(METADATA_CONTAINER_ID);
   }
   
   //sets up the collection description text area
   public static void setUpCollectionDescriptionBeingChanged(Collection col) {      
      DsUtil.hideLabel(CCOL_DESC_EDIT_CONTAINER);
      DsUtil.hideLabel(CCOL_DESC_CONTAINER);
      DsUtil.showLabel(CCOL_DESC_TEXT_AREA_CONTAINER);
      DsUtil.setTextAreaText(CCOL_DESC_TEXT_AREA,col.getDescription());
   }
   
   public static void generateCollectionSaveSuccessMessage() {
      DsUtil.removeAllChildrenFromElement(CCOL_SAVE_SUCCESS_MESSAGE);
      StringBuffer sb = new StringBuffer();
      sb.append("<div data-alert class=\"alert-box success round\" style=\"padding:10px 20px; margin: 20px;\">");
      sb.append("Collection saved.");
      sb.append("<a href=\"#\" class=\"close\">&times;</a>");
      sb.append("</div>");
      RootPanel.get(CCOL_SAVE_SUCCESS_MESSAGE).add(new HTML(sb.toString()));   
      DsUtil.showLabel(CCOL_SAVE_SUCCESS_MESSAGE);
   }
   
   private static void setUpCollectionMetadata(Collection col){
	   RootPanel.get(METADATA_VIEW_KEYWORDS).clear();
	   if(col.getKeywords().length == 0){
		   RootPanel.get(METADATA_VIEW_KEYWORDS).add(createMetadataField(EMPTY_CLASS, "None"));
		   DsUtil.setTextAreaText(METADATA_EDIT_KEYWORDS, null);
	   }else{
		   StringBuilder sb = new StringBuilder();
		   for(String keyword : col.getKeywords()){
			   sb.append(keyword);
			   if(!keyword.equals(col.getKeywords()[col.getKeywords().length-1])){
				   sb.append(", ");
			   }
		   }
		   RootPanel.get(METADATA_VIEW_KEYWORDS).add(createMetadataField(NON_EMPTY_CLASS, sb.toString()));
		   DsUtil.setTextAreaText(METADATA_EDIT_KEYWORDS, sb.toString());
	   }
	   
	   RootPanel.get(METADATA_VIEW_OBJECTIVES).clear();
	   DOM.getElementById(METADATA_EDIT_OBJECTIVES_LIST).setInnerHTML("");
	   if(col.getObjectives().size() == 0){
		   RootPanel.get(METADATA_VIEW_OBJECTIVES).add(createMetadataField(EMPTY_CLASS, "No Objectives Set"));
		   DOM.getElementById(METADATA_EDIT_OBJECTIVES_LIST).setInnerHTML("<li id='noObjectivesItem' class='curColEmpty'>No Objectives Set</li>");
	   }else{
		   for(int i = 0; i < col.getObjectives().size(); i++){
			   DarResourceObjective objective = col.getObjectives().get(i);
			   
			   RootPanel.get(METADATA_VIEW_OBJECTIVES).add(createMetadataField(NON_EMPTY_CLASS, objective.getTitle()));
			   
			   addObjectiveItem(String.valueOf(i), objective.getTitle(), objective.getDescription());
		   }
	   }
	   
	   RootPanel.get(METADATA_VIEW_COVERAGE).clear();
	   if(col.getCoverage().isEmpty()){
		   RootPanel.get(METADATA_VIEW_COVERAGE).add(createMetadataField(EMPTY_CLASS, "No Coverage Defined"));
		   InputElement.as(DOM.getElementById(METADATA_EDIT_COVERAGE)).setValue("");
	   }else{
		   RootPanel.get(METADATA_VIEW_COVERAGE).add(createMetadataField(NON_EMPTY_CLASS, col.getCoverage()));
		   InputElement.as(DOM.getElementById(METADATA_EDIT_COVERAGE)).setValue(col.getCoverage());
	   }
	   
	   RootPanel.get(METADATA_VIEW_ENVIRONMENT).clear();
	   if(col.getEnvironment().isEmpty()){
		   RootPanel.get(METADATA_VIEW_ENVIRONMENT).add(createMetadataField(EMPTY_CLASS, "No Environment Defined"));
		   DOM.getElementById(METADATA_EDIT_ENVIRONMENT).addClassName("notSelected");
		   SelectElement.as(DOM.getElementById(METADATA_EDIT_ENVIRONMENT)).setValue("");
	   }else{
		   RootPanel.get(METADATA_VIEW_ENVIRONMENT).add(createMetadataField(NON_EMPTY_CLASS, col.getEnvironment()));
		   DOM.getElementById(METADATA_EDIT_ENVIRONMENT).removeClassName("notSelected");
		   SelectElement.as(DOM.getElementById(METADATA_EDIT_ENVIRONMENT)).setValue(col.getEnvironment());
	   }
	   
	   if(col.isMetadataBeingChanged()){
		   DsUtil.showLabel(METADATA_EDIT_CONTAINER_ID);
		   DsUtil.hideLabel(METADATA_CONTAINER_ID);
	   }else{
		   DsUtil.hideLabel(METADATA_EDIT_CONTAINER_ID);
		   
		   if(DOM.getElementById(METADATA_TOGGLE_ID).getAttribute("data-toggle").equals("hide")){
			   DsUtil.showLabel(METADATA_CONTAINER_ID);
		   }
	   }
	   
   }
   
   public static void addObjectiveItem(String id, String title, String description){
	   Element editListItem = DOM.createElement("li");
	   editListItem.addClassName(NON_EMPTY_CLASS);
	   
	   editListItem.setInnerHTML("<a id='colObjDelete-"+id+"' href='#' title='Remove' class='delete'></a>"+
			   						"<p id='colObjEdit-"+id+"' class='objective meta-value editable' data-description='"+description+"' title='"+description+"'>" + title + "</p>");
	   DOM.getElementById(METADATA_EDIT_OBJECTIVES_LIST).appendChild(editListItem);
	   
	   PageAssembler.attachHandler("colObjDelete-"+id, Event.ONCLICK, deleteObjectiveCallback);
	   PageAssembler.attachHandler("colObjEdit-"+id, Event.ONCLICK, editObjectiveCallback);
   }
   
   private static EventCallback deleteObjectiveCallback = new EventCallback() {
	   @Override
	   public void onEvent(Event event) {
		   Element target = Element.as(event.getEventTarget());
		 
		   while(!target.getTagName().equalsIgnoreCase("li")){
			   target = target.getParentElement();
		   }
		   
		   target.removeFromParent();
	   }
   };
   
   private static EventCallback editObjectiveCallback = new EventCallback() {
	   @Override
	   public void onEvent(Event event) {
		   Element target = Element.as(event.getEventTarget());
		   while(!target.getTagName().equalsIgnoreCase("p")){
			   target = target.getParentElement();
		   }
		   
		   DsUserHomeScreen.editingId = target.getId();
		   
		   String title = target.getInnerText();
		   String description = target.getAttribute("data-description");
		   
		   DsUserHomeScreen.startAddObjective();
		   InputElement.as(DOM.getElementById(METADATA_EDIT_OBJECTIVES_TITLE_INPUT)).setValue(title);
		   InputElement.as(DOM.getElementById(METADATA_EDIT_OBJECTIVES_DESC_INPUT)).setValue(description);
		   
		   PageAssembler.attachHandler(METADATA_EDIT_OBJECTIVES_ADD_BTN, Event.ONCLICK, finishEditObjectiveListener);
	   }
   };
   
   private static EventCallback finishEditObjectiveListener = new EventCallback() {
	   @Override
	   public void onEvent(Event event) {
		   String title = InputElement.as(DOM.getElementById(METADATA_EDIT_OBJECTIVES_TITLE_INPUT)).getValue();
		   String description = InputElement.as(DOM.getElementById(METADATA_EDIT_OBJECTIVES_DESC_INPUT)).getValue();
		   
		   Element objectiveElement = DOM.getElementById(DsUserHomeScreen.editingId);
		   
		   objectiveElement.setAttribute("data-description", description);
		   objectiveElement.setAttribute("title", description);
		   objectiveElement.setInnerText(title);
		   
		   DsUserHomeScreen.cancelAddObjective();
	   }
   };
   
   //sets up the collection description
   private static void setUpCollectionDescription(Collection col, boolean canModify) {
      if (col.isMetadataBeingChanged()) setUpCollectionDescriptionBeingChanged(col);
      else {
         DsUtil.hideLabel(CCOL_DESC_TEXT_AREA_CONTAINER);
         DsUtil.hideLabel(CCOL_DESC_EDIT_CONTAINER);
         DsUtil.showLabel(CCOL_DESC_CONTAINER);
         DsUtil.removeAllChildrenFromElement(CCOL_DESC);

         if (col.getDescription() == null || col.getDescription().trim().isEmpty()) {
            RootPanel.get(CCOL_DESC).add(createMetadataField(EMPTY_CLASS, EMPTY_DESC_TEXT));
            DsUtil.setTextAreaText(CCOL_DESC_TEXT_AREA,null);
         } else {
        	RootPanel.get(CCOL_DESC).add(createMetadataField(NON_EMPTY_CLASS, col.getDescription()));
            DsUtil.setTextAreaText(CCOL_DESC_TEXT_AREA,col.getDescription());
         }
          
         if (canModify) DsUtil.showLabel(CCOL_DESC_EDIT_CONTAINER);
         else DsUtil.hideLabel(CCOL_DESC_EDIT_CONTAINER);
      }
   }
   
   private static HTML createMetadataField(String cls, String text){
	   StringBuffer sb = new StringBuffer();
       sb.append("<p class=\"" + cls + "\">");
       sb.append(text);      
       sb.append("</p>");
       
       return new HTML(sb.toString());
   }
   
   //sets up the add item and user buttons
   private static void setUpAddButtons(boolean canModify) {
      if (canModify) DsUtil.showLabel(CCOL_ADD_BTN_CONTAINER);
      else DsUtil.hideLabel(CCOL_ADD_BTN_CONTAINER);
   }
   
   //sets up the collection tool bar
   private static void setUpToolBar(Collection col, boolean canModify) {
      DsUtil.setLabelText(CCOL_NAME,col.getName());
      DOM.getElementById(CCOL_NAME).setTitle(col.getName());
      DsUtil.setLabelText(CCOL_USER_COUNT,"(" + String.valueOf(col.getNumberofUsers()) + ")");
      DsUtil.setLabelText(CCOL_ITEM_COUNT,"(" + String.valueOf(col.getNumberofItems()) + ")");
      DsUtil.setLabelText(CCOL_GROUP_COUNT,"(" + String.valueOf(col.getNumberofGroups()) + ")");
      if (canModify) DsUtil.showLabel(CCOL_MOD_COL_CONTAINER);
      else DsUtil.hideLabel(CCOL_MOD_COL_CONTAINER);
   }
   
   //builds the empty item statement
   private static String buildEmptyItemStatement() {
      StringBuffer sb = new StringBuffer();
      sb.append("<p class=\"" + EMPTY_CLASS + "\">");
      sb.append(EMPTY_ITEM_TEXT);      
      sb.append("</p>");
      return sb.toString();
   }
   
   //builds a collection item line item
   private static String buildCollectionItemLineItem(CollectionItem ci, boolean canModify, HashMap<String,CollectionItem> collectionItemDeleteWidgets) {
      String deleteItemId = DsUtil.generateId(CCOL_DELETE_ITEM_PREFIX);
      String itemDetailsId = DsUtil.generateId(CCOL_ITEM_DETAILS_PREFIX);  
      collectionItemDeleteWidgets.put(deleteItemId,ci);
      StringBuffer sb = new StringBuffer();
      sb.append("<li id=\"" + ci.getLocatorKey() + "\">");
      sb.append("<div data-alert class=\"alert-box secondary radius\" style=\"margin:5px; padding:5px 20px;\" " + ITEM_CLASS_TOGGLE_STR + " >");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-4 columns\" style=\"padding:0px;font-size: 12px\">");
      sb.append("<b>" + ci.getTruncatedResourceTitle() + "</b>");
      sb.append("</div>");
      sb.append("<div class=\"small-6 columns\" style=\"padding:0px;font-size: 12px\">");
      sb.append("<a title=\"" + ci.getResourceTitle() + "\" target=\"" + ci.getResourceTitle() + "\" href=\"" + ci.getResourceUrl() + "\">");
      sb.append("<i style=\"color:#008cba;\">" + ci.getTruncatedResourceUrl() + "</i></a>");
      sb.append("</div>");
      sb.append("<div class=\"small-1 columns\" style=\"padding:0px;font-size: 12px\">");
      sb.append("<a onclick=\"toggleDivSlow('" + itemDetailsId + "');\" title=\"" + CCOL_ITEM_DETAILS_TITLE + "\">");
      sb.append("<i style=\"color:#008cba;font-size:1.2em;\" class=\"fa fa-book\"></i></a>");
      if (canModify) {
         sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
         sb.append("<a id=\"" + deleteItemId + "\" title=\"" + CCOL_DELETE_ITEM_TITLE + "\">");
         sb.append("<i style=\"color:red;font-size:1.2em\" class=\"fa fa-times-circle\"></i></a>");
      }      
      sb.append("</div>");
      sb.append("</div>");
      sb.append("<div id=\"" + itemDetailsId + "\" style=\"display:none\" >");
      sb.append("<hr>");     
      sb.append("<div class=\"row\" style=\"padding: 0px 10px; font-size: 12px\">");
      sb.append("<span class=\"" + WORD_WRAP_CLASS + "\"><b>" + ci.getResourceTitle() + "</b></span>");
      sb.append("</div>");
      sb.append("<div class=\"row\" style=\"padding: 0px 10px; font-size: 12px\">");
      sb.append("<span class=\"" + WORD_WRAP_CLASS + "\">");
      sb.append("<a title=\"" + ci.getResourceTitle() +  "\" style=\"color:green\" target=\"" + ci.getResourceTitle() + "\" href=\"" + ci.getResourceUrl() + "\">");
      sb.append("<i>" + ci.getResourceUrl() + "</i></a>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("<div class=\"row\" style=\"padding: 0px 10px; font-size: 12px\">");
      sb.append("<span class=\"" + WORD_WRAP_CLASS + "\">");
      sb.append(ci.getResourceDescription());
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");
      return sb.toString();     
   }
   
   //builds the item view 
   private static void buildItemView(Collection col, boolean canModify, HashMap<String,CollectionItem> collectionItemDeleteWidgets) {
      DsUtil.removeAllChildrenFromElement(CCOL_ITEM_CONTAINER);
      String itemText;
      if (col.getNumberofItems() == 0) itemText = buildEmptyItemStatement();
      else {
         StringBuffer sb = new StringBuffer();
         sb.append("<ul id=\"" + CCOL_ITEM_LIST_NAME + "\">");
         for (CollectionItem ci: col.getCollectionItems()) sb.append(buildCollectionItemLineItem(ci,canModify,collectionItemDeleteWidgets));
         sb.append("</ul>");
         itemText = sb.toString();          
      }
      RootPanel.get(CCOL_ITEM_CONTAINER).add(new HTML(itemText));
   }
   
   //builds the user access section
   private static String buildUserAccess(CollectionUser cu, boolean canModify) {
      StringBuffer sb = new StringBuffer();
      String ddId = cu.getLocatorKey() + CCOL_ACCESS_DD_SUFFIX;
      if (!canModify || DsSession.getUser().getUserId().equalsIgnoreCase(cu.getUserId())) {         
         sb.append("<span id=\"" + ddId + "\" style=\"font-size:14px;\">");
         sb.append("<i>" + cu.getAccess() + "</i>");
         sb.append("</span>");         
      }
      else {                  
         sb.append("<select id=\"" + ddId + "\" style=\"margin: 0px;\" " + ONCHANGE_TRIGGER_CHANGE_MESSAGE + ">");
         sb.append("<option value=\"" + CollectionAccess.VIEW_ACCESS + "\" ");
         if (CollectionAccess.VIEW_ACCESS.equalsIgnoreCase(cu.getAccess())) sb.append("selected=\"selected\"");
         sb.append(">" + CollectionAccess.VIEW_ACCESS + "</option>");
         sb.append("<option value=\"" + CollectionAccess.MODIFY_ACCESS + "\" ");
         if (CollectionAccess.MODIFY_ACCESS.equalsIgnoreCase(cu.getAccess())) sb.append("selected=\"selected\"");
         sb.append(">" + CollectionAccess.MODIFY_ACCESS + "</option>");         
         sb.append("</select>");
      }
      return sb.toString();
   }
   
   //builds a collection user line item - don't add delete button to current user
   private static String buildCollectionUserLineItem(CollectionUser cu, boolean canModify, HashMap<String,CollectionUser> collectionUserDeleteWidgets) {
      String deleteUserId = DsUtil.generateId(CCOL_DELETE_USER_PREFIX);
      collectionUserDeleteWidgets.put(deleteUserId,cu);
      StringBuffer sb = new StringBuffer();
      sb.append("<li>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-5 columns\">");
      sb.append("<i class=\"fa fa-user\" style=\"color:#008cba;font-size:1.5em;\"></i>");
      sb.append("<span class=\"" + WORD_WRAP_CLASS + "\">");
      sb.append("&nbsp;&nbsp;&nbsp;<b>" + cu.getFullName() + "</b>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("<div class=\"small-4 columns\">");
      sb.append("<span class=\"" + WORD_WRAP_CLASS + "\" style=\"font-size:14px;color:#666\">");
      sb.append("<i>" + cu.getEmailAddress() + "</i>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      sb.append(buildUserAccess(cu,canModify));
      sb.append("</div>");
      sb.append("<div class=\"small-1 columns\">");
      if (canModify && !DsSession.getUser().getUserId().equalsIgnoreCase(cu.getUserId())) {
         sb.append("<a id=\"" + deleteUserId + "\" title=\"" + CCOL_DELETE_USER_TITLE + "\">");
         sb.append("<i style=\"color:red;font-size:1.2em\" class=\"fa fa-times-circle\"></i></a>");
      }
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");      
      return sb.toString();
   }
      
   //builds the user view -- should never have empty users... 
   private static void buildUserView(Collection col, boolean canModify, HashMap<String,CollectionUser> collectionUserDeleteWidgets) {
      DsUtil.removeAllChildrenFromElement(CCOL_USER_CONTAINER);
      StringBuffer sb = new StringBuffer();
      sb.append("<ul id=\"" + CCOL_USER_LIST_NAME + "\">");
      for (CollectionUser cu: col.getCollectionUsers()) sb.append(buildCollectionUserLineItem(cu,canModify,collectionUserDeleteWidgets));
      sb.append("</ul>");
      RootPanel.get(CCOL_USER_CONTAINER).add(new HTML(sb.toString()));
   }
   
   //builds the empty group statement
   private static String buildEmptyGroupStatement() {
      StringBuffer sb = new StringBuffer();
      sb.append("<p class=\"" + EMPTY_CLASS + "\">");
      sb.append(EMPTY_GROUP_TEXT);      
      sb.append("</p>");
      return sb.toString();
   }
   
   //builds the group access section
   private static String buildGroupAccess(CollectionGroup cg, boolean canModify) {
      StringBuffer sb = new StringBuffer();
      String ddId = cg.getLocatorKey() + CCOL_ACCESS_DD_SUFFIX;
      if (!canModify) {         
         sb.append("<span id=\"" + ddId + "\" style=\"font-size:14px;\">");
         sb.append("<i>" + cg.getAccess() + "</i>");
         sb.append("</span>");         
      }
      else {                  
         sb.append("<select id=\"" + ddId + "\" style=\"margin: 0px;\" " + ONCHANGE_TRIGGER_CHANGE_MESSAGE + ">");
         sb.append("<option value=\"" + CollectionAccess.VIEW_ACCESS + "\" ");
         if (CollectionAccess.VIEW_ACCESS.equalsIgnoreCase(cg.getAccess())) sb.append("selected=\"selected\"");
         sb.append(">" + CollectionAccess.VIEW_ACCESS + "</option>");
         sb.append("<option value=\"" + CollectionAccess.MODIFY_ACCESS + "\" ");
         if (CollectionAccess.MODIFY_ACCESS.equalsIgnoreCase(cg.getAccess())) sb.append("selected=\"selected\"");
         sb.append(">" + CollectionAccess.MODIFY_ACCESS + "</option>");         
         sb.append("</select>");
      }
      return sb.toString();
   }
   
   //builds a collection group line item
   private static String buildCollectionGroupLineItem(CollectionGroup cg, boolean canModify, HashMap<String,CollectionGroup> collectionGroupDeleteWidgets) {
      String deleteGroupId = DsUtil.generateId(CCOL_DELETE_GROUP_PREFIX);
      collectionGroupDeleteWidgets.put(deleteGroupId,cg);
      StringBuffer sb = new StringBuffer();
      sb.append("<li>");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-5 columns\">");
      sb.append("<i class=\"fa fa-users\" style=\"color:#008cba;font-size:1.5em;\"></i>");
      sb.append("<span class=\"" + WORD_WRAP_CLASS + "\">");
      sb.append("&nbsp;&nbsp;&nbsp;<b>" + cg.getName() + "</b>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("<div class=\"small-4 columns\">");
      sb.append("<span class=\"" + WORD_WRAP_CLASS + "\" style=\"font-size:14px;color:#666\">");
      sb.append("<i>" + cg.getGroupType() + "</i>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      sb.append(buildGroupAccess(cg,canModify));
      sb.append("</div>");
      sb.append("<div class=\"small-1 columns\">");
      if (canModify) {
         sb.append("<a id=\"" + deleteGroupId + "\" title=\"" + CCOL_DELETE_GROUP_TITLE + "\">");
         sb.append("<i style=\"color:red;font-size:1.2em\" class=\"fa fa-times-circle\"></i></a>");
      }
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");      
      return sb.toString();
   }
  
   //builds the group view
   private static void buildGroupView(Collection col, boolean canModify, HashMap<String,CollectionGroup> collectionGroupDeleteWidgets) {
      DsUtil.removeAllChildrenFromElement(CCOL_GROUP_CONTAINER);
      String groupText;
      if (col.getNumberofGroups() == 0) groupText = buildEmptyGroupStatement();
      else {
         StringBuffer sb = new StringBuffer();
         sb.append("<ul id=\"" + CCOL_GROUP_LIST_NAME + "\">");
         for (CollectionGroup cg: col.getCollectionGroups()) sb.append(buildCollectionGroupLineItem(cg,canModify,collectionGroupDeleteWidgets));
         sb.append("</ul>");
         groupText = sb.toString();
      }      
      RootPanel.get(CCOL_GROUP_CONTAINER).add(new HTML(groupText));
   }
   
   /**
    * Populates the collection data into the page view
    * 
    * @param col The collection to use for population
    * @param collectionItemDeleteWidgets The collection item delete widget register
    * @param collectionUserDeleteWidgets The collection user delete widget register
    * @param collectionGroupDeleteWidgets The collection group delete widget register
    */
   public static void populateCollectionData(Collection col, HashMap<String,CollectionItem> collectionItemDeleteWidgets, HashMap<String,CollectionUser> collectionUserDeleteWidgets, 
         HashMap<String,CollectionGroup> collectionGroupDeleteWidgets) {
      boolean canModify = col.sessionUserCanModify();
      if (col.getHasChanged()) DsUtil.showLabel(CCOL_CHANGED_MESSAGE);
      else DsUtil.hideLabel(CCOL_CHANGED_MESSAGE);
      DsUtil.hideLabel(CCOL_SAVE_SUCCESS_MESSAGE);
      setUpToolBar(col,canModify);
      setUpCollectionDescription(col,canModify);
      setUpCollectionMetadata(col);
      setUpAddButtons(canModify);
      buildItemView(col,canModify,collectionItemDeleteWidgets);
      buildUserView(col,canModify,collectionUserDeleteWidgets);
      buildGroupView(col,canModify,collectionGroupDeleteWidgets);
   }
   
   //builds the collection selection line item
   private static String buildCollectionSelectionLineItem(Collection col, HashMap<String,Collection> selectWidgets) {
      String selectId = DsUtil.generateId(COL_SEL_PREFIX);
      selectWidgets.put(selectId,col);
      StringBuffer sb = new StringBuffer();
      sb.append("<li role=\"" + COL_IL_NAV_ROLE + "\">");
      sb.append("<a id=\"" + selectId + "\">");
      sb.append("<i class=\"fa fa-chevron-circle-right\"></i> ");
      sb.append(col.getName());
      sb.append("</a>");
      sb.append("</li>");
      return sb.toString();
   }
   
   //handle the more/less links
   private static void handleMoreLessLinks(String showMorePanelId, String showLessPanelId, long collectionSize, long numberOfEntries) {
      if (collectionSize <= numberOfEntries) {
         DsUtil.hideLabel(showMorePanelId);
         DsUtil.hideLabel(showLessPanelId);
      }
      else if (numberOfEntries == UNLIMITED || collectionSize == numberOfEntries) {
         DsUtil.hideLabel(showMorePanelId);
         DsUtil.showLabel(showLessPanelId);
      }
      else {
         DsUtil.hideLabel(showLessPanelId);
         DsUtil.showLabel(showMorePanelId);
      }
   }
   
   /**
    * Builds the collection navigation view HTML and attaches it to the given parent panel.
    * 
    * If {@link CollectionsViewBuilder#UNLIMITED} is passed as the number of entries, navigation pointers
    * for all collections will be added.
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param showMorePanelId The ID of the 'show more' panel
    * @param showLessPanelId The ID of the 'show less' panel
    * @param collectionList The collection list
    * @param numberOfEntries The number collections to show navigation pointers
    * @param selectWidgets The select collection widget register
    */
   public static void buildCollectionNavigation(String parentPanelId, String showMorePanelId, String showLessPanelId, ArrayList<Collection> collectionList, int numberOfEntries, 
         HashMap<String,Collection> selectWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);
      if (collectionList.size() <= 0) DsUtil.hideLabel(COL_NAV_CONTAINER);
      else {
         DsUtil.showLabel(COL_NAV_CONTAINER);
         int toNum = numberOfEntries;
         if (numberOfEntries == UNLIMITED || numberOfEntries >= collectionList.size()) toNum = collectionList.size();
         StringBuffer sb = new StringBuffer();      
         sb.append("<ul class=\"" + COL_UL_NAV_CLASS + "\" role=\"" + COL_UL_NAV_ROLE + "\" title=\"" + COL_UL_NAV_TITLE + "\">");            
         for (int i=0;i<toNum;i++) sb.append(buildCollectionSelectionLineItem(collectionList.get(i),selectWidgets));      
         sb.append("</ul>");
         RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
         handleMoreLessLinks(showMorePanelId,showLessPanelId,collectionList.size(),numberOfEntries);
      }
   }
      
   /**
    * Builds the add new collection user view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param addCollectionUserList The list of potential new collection users
    * @param newCollectionUserWidgets The new collection user widget register
    */
   public static void buildNewCollectionUserList(String parentPanelId, ArrayList<AppUser> addCollectionUserList, HashMap<String,AppUser> newCollectionUserWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(ACU_CONTAINER, ACU_SEARCH_PLACEHOLDER));      
      for (AppUser pnu: addCollectionUserList) {
         sb.append(ViewBuilderHelper.generateUserPickListLineItem(pnu,newCollectionUserWidgets,ACU_TOOLS_PREFIX,ACU_ADD_PREFIX, 
               ACU_USER_NAME_CLASS,ACU_USER_ID_CLASS,ACU_ADD_CLASS,ACU_ADD_TITLE)); 
      }            
      sb.append(ViewBuilderHelper.buildListFooter());     
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
   /**
    * Builds the add new collection group view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param addCollectionGroupList The list of potential new collection groups
    * @param newCollectionGroupWidgets The new collection group widget register
    */
   public static void buildNewCollectionGroupList(String parentPanelId, ArrayList<Group> addCollectionGroupList, HashMap<String,Group> newCollectionGroupWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(ACG_CONTAINER, ACG_SEARCH_PLACEHOLDER));      
      for (Group png: addCollectionGroupList) {
         sb.append(ViewBuilderHelper.generateGroupPickListLineItem(png,newCollectionGroupWidgets,ACG_TOOLS_PREFIX,ACG_ADD_PREFIX, 
               ACG_GROUP_NAME_CLASS,ACG_GROUP_TYPE_CLASS,ACG_ADD_CLASS,ACG_ADD_TITLE)); 
      }            
      sb.append(ViewBuilderHelper.buildListFooter());     
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
   //generates a collection pick list line item
   private static String generateCollectionPickListLineItem(Collection col, HashMap<String,Collection> addResourceCollectionWidgets) {
      String toolsId = DsUtil.generateId(ARTC_TOOLS_PREFIX);
      String addId = DsUtil.generateId(ARTC_ADD_PREFIX);     
      addResourceCollectionWidgets.put(addId,col);
      StringBuffer sb = new StringBuffer();
      sb.append("<li onmouseover=\"document.getElementById('" + toolsId + "').style.display='block';\" onmouseleave=\"document.getElementById('" + toolsId + "').style.display='none';\">");
      sb.append("<div class=\"row\">");
      sb.append("<div class=\"small-10 columns\">");
      sb.append("<label class=\"" + ARTC_COL_NAME_CLASS + "\"><i class=\"fa fa-briefcase\" style=\"color: #008cba;\"></i>&nbsp;&nbsp;&nbsp;");
      sb.append(col.getName() + "</label>");
      sb.append("</div>");
      sb.append("<div class=\"small-2 columns\">");
      sb.append("<div id=\"" + toolsId + "\" class=\"" + ARTC_ADD_CLASS + "\" style=\"display:none\">");
      sb.append("<span class=\"" + TOOLS_CLASS + "\">");
      sb.append("<a id=\"" + addId + "\" class=\"button tiny\" title=\"" + ARTC_ADD_TITLE + "\" style=\"margin: 0px\"><i class=\"fa fa-plus\"></i></a>");
      sb.append("</span>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</div>");
      sb.append("</li>");
      return sb.toString();
   }
   
   /**
    * Builds the add resource to collection view HTML and attaches it to the given parent panel
    * 
    * @param parentPanelId The ID of the parent panel on which to attach the HTML
    * @param addResourceCollectionList The list of collections
    * @param addResourceCollectionWidgets The new collection user widget register
    */
   public static void buildAddResourceCollectionList(String parentPanelId, ArrayList<Collection> addResourceCollectionList, HashMap<String,Collection> addResourceCollectionWidgets) {
      DsUtil.removeAllChildrenFromElement(parentPanelId);      
      StringBuffer sb = new StringBuffer();
      sb.append(ViewBuilderHelper.buildListHeader(ARTC_CONTAINER, ARTC_SEARCH_PLACEHOLDER));      
      for (Collection col: addResourceCollectionList) {
         sb.append(generateCollectionPickListLineItem(col,addResourceCollectionWidgets)); 
      }            
      sb.append(ViewBuilderHelper.buildListFooter());     
      RootPanel.get(parentPanelId).add(new HTML(sb.toString()));
   }
   
}

