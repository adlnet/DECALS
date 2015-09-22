package com.eduworks.decals.ui.client.pagebuilder.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.handler.DarSearchHandler;
import com.eduworks.decals.ui.client.handler.DsHeaderHandler;
import com.eduworks.decals.ui.client.handler.DsUserTabsHandler;
import com.eduworks.decals.ui.client.handler.DsUserTabsHandler.UserTabs;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.Collection;
import com.eduworks.decals.ui.client.model.CollectionGroup;
import com.eduworks.decals.ui.client.model.CollectionItem;
import com.eduworks.decals.ui.client.model.CollectionManager;
import com.eduworks.decals.ui.client.model.CollectionUser;
import com.eduworks.decals.ui.client.model.DarResourceObjective;
import com.eduworks.decals.ui.client.model.Group;
import com.eduworks.decals.ui.client.model.GroupManager;
import com.eduworks.decals.ui.client.model.SearchHandlerParamPacket;
import com.eduworks.decals.ui.client.util.CollectionsViewBuilder;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.decals.ui.client.util.DsUtil.NavMode;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * 
 * Home screen for application users.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsUserHomeScreen extends DecalsWithGroupMgmtScreen {
  
   private static final String MCNT_SEARCH_RESULTS_CONTAINER = "myContributionsSearchResults";
   private static final String MCNT_SEARCH_BUSY = "myContributionsSearchBusy";
   private static final String MCNT_COUNTER_CONTAINER = "numberOfMyContributionsSearchResults";
   private static final String MCNT_COUNTER = "myContributionsSearchResultsCounter";
   private static final String MCNT_EMTPY_RESULTS = "emptyMyContributions";
   
   private static final String ADD_WEBPAGE_MODAL = "modalAddContent-webpage";
   private static final String ADD_FILE_MODAL = "modalAddContent-file";
   
   private static final String EDIT_CONTENT_MODAL = "modalEditContent";
   private static final String EDIT_CONTENT_FILENAME = "editContentFileName";
   private static final String EDIT_CONTENT_DESC = "editContentDescription";
   private static final String EDIT_CONTENT_FORM = "editContentForm";
   private static final String EDIT_CONTENT_RESOURCE_ID = "editContentResourceId";
   private static final String EDIT_CONTENT_BUSY_ID = "editContentBusy";   
   private static final String EDIT_CONTENT_MDFILENAME = "editContentMdFileName";   
   private static final String EDIT_CONTENT_PREVIEW_CONTAINER = "editContentPreviewContainer";   
   private static final String EDIT_CONTENT_TITLE = "editContentTitle";
   private static final String EDIT_CONTENT_KEYWORDS = "editContentKeywords";
   private static final String EDIT_CONTENT_SEC_CLASS = "editContentSecurityClassification";
   private static final String EDIT_CONTENT_SEC_LEVEL = "editContentSecurityLevel";
   private static final String EDIT_CONTENT_DIST = "editContentDistribution";
   private static final String EDIT_CONTENT_FILEFORMAT = "editContentFileFormat";
   private static final String EDIT_CONTENT_PUBLISHER = "editContentPublisher";
   private static final String EDIT_CONTENT_OWNER = "editContentOwner";
   private static final String EDIT_CONTENT_COVERAGE = "editContentCoverage";
   private static final String EDIT_CONTENT_INTERACTIVITY = "editContentInteractivity";
   private static final String EDIT_CONTENT_ENVIRONMENT = "editContentEnvironment";
   private static final String EDIT_CONTENT_SKILL = "editContentSkill";
   private static final String EDIT_CONTENT_LANGUAGE = "editContentLanguage";
   private static final String EDIT_CONTENT_FILESIZE = "editContentFileSize";
   private static final String EDIT_CONTENT_DURATION = "editContentDuration";
   private static final String EDIT_CONTENT_TECHREQ = "editContentTechnicalRequirements";
   private static final String EDIT_CONTENT_PARTOF = "editContentPartOf";
   private static final String EDIT_CONTENT_REQUIRES = "editContentRequires";
   private static final String EDIT_CONTENT_OBJ_LIST = "editContentObjectivesList";
   private static final String EDIT_CONTENT_OBJ_TITLE_PREFIX = "objTitleInput-";
   private static final String EDIT_CONTENT_OBJ_DESC_PREFIX = "objDescrInput-";
   private static final String EDIT_CONTENT_OBJ_DELETE_PREFIX = "objDelete-";
   private static final String EDIT_CONTENT_OBJ_TEXT_PREFIX = "objText-";
   
   private static final String DELETE_CONTENT_CONFIRM_MODAL = "modalDeleteContentConfirm";
   private static final String DELETE_CONTENT_FILENAME = "deleteContentFileName";
   private static final String DELETE_CONTENT_FORM = "deleteContentForm"; 
   private static final String DELETE_CONTENT_RESOURCE_ID = "deleteContentResourceId";
   private static final String DELETE_CONTENT_BUSY_ID = "deleteContentBusy";
   
   private static final String GENMD_CONTENT_CONFIRM_MODAL = "modalContentGenerateMdConfirm";
   private static final String GENMD_CONTENT_FILENAME = "contentGenerateMdFileName";
   private static final String GENMD_CONTENT_FORM = "contentGenerateMdForm"; 
   private static final String GENMD_CONTENT_RESOURCE_ID = "contentGenerateMdResourceId";   
   
   private static final String PUBLISH_CONTENT_CONFIRM_MODAL = "modalContentLrPublishConfirm";
   private static final String PUBLISH_CONTENT_FILENAME = "contentLrPublishFileName";
   private static final String PUBLISH_CONTENT_FORM = "contentLrPublishForm"; 
   private static final String PUBLISH_CONTENT_RESOURCE_ID = "contentLrPublishResourceId";
   private static final String PUBLISH_CONTENT_LR_NODE = "contentLrPublishNodeName";
   private static final String PUBLISH_CONTENT_BUSY = "publishContentBusy";  
   
   private static final String PUBLISH_SUBMIT_BTN = "contentLrPublishSubmitBtn";
   private static final String PUBLISH_CANCEL_BTN = "contentLrPublishCancelBtn";
   private static final String PUBLISH_SUCCESS_CONTAINER = "contentLrPublishSuccess";
   private static final String PUBLISH_SUCCESS_LR_ID = "contentLrPublishSuccessLrId";
   private static final String PUBLISH_FAIL_CONTAINER = "contentLrPublishFail";
   private static final String PUBLISH_FAIL_MESSAGE = "contentLrPublishErrorMessage";
   
   private static final String ADD_WEBPAGE_FORM = "addWebpageForm";
   private static final String ADD_FILE_FORM = "uploadFileForm";
   
   private static final String ADD_FILE_BUTTON = "uploadFileBtn";
   private static final String ADD_FILE_DATA = "uploadFileData";
   
   private static final String ADD_WEBPAGE_BUSY = "addWebpageBusy";
   //private static final String ADD_FILE_BUSY = "uploadFileBusy";
   
   private static final String ADD_WEBPAGE_ERROR_CONTAINER = "addWebpageErrorContainer";
   //private static final String ADD_FILE_ERROR_CONTAINER = "uploadFileErrorContainer";
   
   private static final String ADD_WEBPAGE_FAILED = "Could not add the webpage to the repository";
   
   private static final String ADD_WEBPAGE_TITLE = "addWebpageTitle";
   private static final String ADD_WEBPAGE_URL = "addWebpageUrl";
   
   private static final String MC_MESSAGES = "contributionAlertMessages";
   private static final String ADD_FILE_MESSAGE_PREFIX = "dufm-";   
   private static final String ADD_FILE_MESSAGE_CLOSE_PREFIX = "dufm-close-";
   private static final String ADD_FILE_COMPLETE_MESSAGE_PREFIX = "dufcm-";
   private static final String ADD_FILE_COMPLETE_MESSAGE_CLOSE_PREFIX = "dufcm-close-";

   private static final String ADD_FILE_BUSY_MESSAGE_IMG = "images/file-upload.gif";
   private static final String ADD_FILE_BUSY_MESSAGE_CLASS = "alert-box info round";
   private static final String ADD_FILE_COMPLETE_MESSAGE_CLASS = "alert-box success round";   
   
   private static final String MY_COLLECTIONS_CONTAINER = "userMyCollections";
   private static final String MY_CONTRIBUTIONS_CONTAINER = "userMyContributions";
   private static final String MY_GROUPS_CONTAINER = "userMyGroups";
   
   private static final String MY_COLLECTIONS_LINK = "goToMyCollections";
   private static final String MY_CONTRIBUTIONS_LINK = "goToMyContributions";
   private static final String MY_GROUPS_LINK = "goToMyGroups";   
   private static final String MY_COLLECTIONS_LINK_TEXT = "goToMyCollectionsText";
   private static final String MY_CONTRIBUTIONS_LINK_TEXT = "goToMyContributionsText";
   private static final String MY_GROUPS_LINK_TEXT = "goToMyGroupsText";   
  
   private static final String MGRP_NAV_CONTAINER = "myGroupsNavigation";
   private static final String MGRP_NONE = "emptyMyGroups";
   private static final String MGRP_BUSY = "myGroupsSearchBusy";
   private static final String MGRP_CURRENT_CONTAINER = "currentGroupContainer";
   private static final String MGRP_SELECTIONS = "myGroupsSelections";
   private static final String MGRP_MORE_SELECTIONS_CONTAINER = "myGroupsMoreSelections";
   private static final String MGRP_MORE_SELECTIONS_LINK = "myGroupsMoreSelectionsLink";
   private static final String MGRP_LESS_SELECTIONS_CONTAINER = "myGroupsLessSelections";
   private static final String MGRP_LESS_SELECTIONS_LINK = "myGroupsLessSelectionsLink";
   
   private static final String MCOL_BUSY = "myCollectionsSearchBusy";
   private static final String MCOL_NONE = "emptyMyCollections";
   private static final String MCOL_CURRENT_CONTAINER = "currentCollectionContainer";
   private static final String MCOL_SELECTIONS = "myCollectionsSelections";
   private static final String MCOL_MORE_SELECTIONS_CONTAINER = "myCollectionsMoreSelections";
   private static final String MCOL_MORE_SELECTIONS_LINK = "myCollectionsMoreSelectionsLink";
   private static final String MCOL_LESS_SELECTIONS_CONTAINER = "myCollectionsLessSelections";
   private static final String MCOL_LESS_SELECTIONS_LINK = "myCollectionsLessSelectionsLink";
   
   private static final String CCOL_ITEMS_LINK = "curColItemsLink";
   private static final String CCOL_USERS_LINK = "curColUsersLink";
   private static final String CCOL_GROUPS_LINK = "curColGroupsLink";   
   private static final String CCOL_ITEMS_LINK_TEXT = "curColItemsText";
   private static final String CCOL_USERS_LINK_TEXT = "curColUsersText";
   private static final String CCOL_GROUPS_LINK_TEXT = "curColGroupsText";
   
   private static final String CCOL_ADD_USER_LINK = "curColAddUserLink";
   private static final String CCOL_ADD_ITEM_LINK = "curColAddItemLink";
   private static final String CCOL_ADD_GROUP_LINK = "curColAddGroupLink";
   
   private static final String CCOL_CHANGED_MESSAGE = "curColChangedMessage";   
   private static final String CCOL_ITEMS_CONTAINER = "curColItems";
   private static final String CCOL_USERS_CONTAINER = "curColUsers";
   private static final String CCOL_GROUPS_CONTAINER = "curColGroups";
   private static final String CCOL_DESC_EDIT_CONTAINER = "curColDescEditContainer";
   private static final String CCOL_DESC_CONTAINER = "curColDescContainer";
   private static final String CCOL_DESC_TEXT_AREA_CONTAINER = "curColDescTextAreaContainer";   
   private static final String CCOL_EDIT_DESC = "curColDescEdit";   
   private static final String CCOL_DESC_TEXT_AREA = "curColDescTextArea";   
   private static final String CCOL_SAVE_LINK = "curColSaveLink";
   private static final String CCOL_DELETE_LINK = "curColDeleteLink";
   private static final String CCOL_RESET_LINK = "curColResetLink";   
   private static final String CCOL_LIST_NAME = "curColItemList";
   private static final String CCOL_FORM = "curColForm";   
   private static final String CCOL_EXPORT_FORM = "curColExportForm";
   private static final String CCOL_EXPORT_FORM_COLLECTION_ID = "curColId";
   private static final String CCOL_EXPORT_FORM_SESSION = "curColSessionId";
   
   
   private static final String AC_MODAL = "modalAddCollection";
   private static final String AC_FORM = "addCollectionForm";
   private static final String AC_NAME = "addCollectionName";
   private static final String AC_DESC = "addCollectionDesc";
   private static final String AC_SUBMIT_BTNS = "addCollectionSubmitButtons";
   private static final String AC_BUSY = "addCollectionBusy";
   private static final String AC_SUCCESS = "addCollectionSuccess";
   
   private static final String DC_CONFIRM_MODAL = "modalDeleteCollectionConfirm";
   private static final String DC_FORM = "deleteCollectionForm";
   private static final String DC_NAME = "deleteCollectionName";
   private static final String DC_COL_ID = "deleteCollectionId";
   private static final String DC_SUBMIT_BTNS = "deleteCollectionSubmitButtons";
   private static final String DC_BUSY = "deleteCollectionBusy";
   private static final String DC_SUCCESS = "deleteCollectionSuccess";
   
   private static final String RC_CONFIRM_MODAL = "modalResetCollectionConfirm";
   private static final String RC_FORM = "resetCollectionForm";
   private static final String RC_NAME = "resetCollectionName";
   private static final String RC_COL_ID = "resetCollectionId";
   private static final String RC_SUBMIT_BTNS = "resetCollectionSubmitButtons";
   private static final String RC_BUSY = "resetCollectionBusy";
   private static final String RC_SUCCESS = "resetCollectionSuccess";
   
   private static final String ACI_MODAL = "modalAddCollectionItem";
   private static final String ACI_COL_NAME = "addItemCollectionName";
   private static final String ACI_FORM = "addCollectionItemForm";   
   private static final String ACI_URL = "addCollectionItemUrl";
   private static final String ACI_TITLE = "addCollectionItemTitle";
   private static final String ACI_DESC = "addCollectionItemDesc";   
   private static final String ACI_EXISTS = "addCollectionItemExists";   
   private static final String ACI_SUBMIT_BTNS = "addCollectionItemSubmitButtons";
   private static final String ACI_REPLACE = "addCollectionItemReplace";   
   
   private static final String ACU_COL_NAME = "addUserCollectionName";
   private static final String ACU_MODAL = "modalAddCollectionUser";
   private static final String ACU_PICKLIST = "addCollectionUserPickList";
   private static final String ACU_INNER_CONTAINER = "addCollectionUserListContainer";
   private static final String ACU_USER_LIST_CONTAINER = "addCollectionUserList";
   private static final String ACU_CANCEL_BTN = "addCollectionUserCancelButton";
   private static final String ACU_CONFIRM = "addCollectionUserConfirm";
   private static final String ACU_FORM = "addCollectionUserForm";
   private static final String ACU_USER_NAME = "acuUserName";
   private static final String ACU_USER_ID = "acuUserId";
   private static final String ACU_CONFIRM_USER_NAME = "addCollectionUserName";
   private static final String ACU_CONFIRM_ACCESS = "addCollectionUserAccess";
   private static final String ACU_CONFIRM_USER_ID = "addCollectionUserId";
   private static final String ACU_CONFIRM_USER_FIRST_NAME = "addCollectionUserFirstName";
   private static final String ACU_CONFIRM_USER_LAST_NAME = "addCollectionUserLastNameName";
   
   private static final String ACG_COL_NAME = "addGroupCollectionName";
   private static final String ACG_MODAL = "modalAddCollectionGroup";
   private static final String ACG_PICKLIST = "addCollectionGroupPickList";
   private static final String ACG_INNER_CONTAINER = "addCollectionGroupListContainer";
   private static final String ACG_GROUP_LIST_CONTAINER = "addCollectionGroupList";
   private static final String ACG_CANCEL_BTN = "addCollectionGroupCancelButton";
   private static final String ACG_CONFIRM = "addCollectionGroupConfirm";
   private static final String ACG_FORM = "addCollectionGroupForm";
   private static final String ACG_GROUP_NAME = "acgGroupName";
   private static final String ACG_GROUP_TYPE = "acgGroupType";
   private static final String ACG_CONFIRM_GROUP_NAME = "addCollectionGroupName";
   private static final String ACG_CONFIRM_ACCESS = "addCollectionGroupAccess";
   private static final String ACG_CONFIRM_GROUP_ID = "addCollectionGroupId";
   private static final String ACG_CONFIRM_GROUPH_NAME = "addCollectionGroupHName";
   private static final String ACG_CONFIRM_GROUP_TYPE = "addCollectionGroupType";
   
   private static final String DCI_MODAL = "modalDeleteCollectionItemConfirm";
   private static final String DCI_ITEM_NAME = "delColItemItemName";
   private static final String DCI_COL_NAME = "delColItemColName";
   private static final String DCI_FORM = "delColItemForm";
   private static final String DCI_ITEM_ID = "delColItemItemId";
   private static final String DCI_COL_ID = "delColItemColId";
   
   private static final String DCU_MODAL = "modalDeleteCollectionUserConfirm";
   private static final String DCU_USER_NAME = "delColUserUserName";
   private static final String DCU_COL_NAME = "delColUserColName";
   private static final String DCU_FORM = "delColUserForm";
   private static final String DCU_USER_ID = "delColUserUserId";
   private static final String DCU_COL_ID = "delColUserColId";
   
   private static final String DCG_MODAL = "modalDeleteCollectionGroupConfirm";
   private static final String DCG_GROUP_NAME = "delColGroupGroupName";
   private static final String DCG_COL_NAME = "delColGroupColName";
   private static final String DCG_FORM = "delColGroupForm";
   private static final String DCG_GROUP_ID = "delColGroupGroupId";
   private static final String DCG_COL_ID = "delColGroupColId";
   
   private static final int NUMBER_OF_INITIAL_SELECTIONS_SHOWN = 3;
   private static final int LIST_ITEMS_PER_PAGE = 5;
   
   private boolean contributionsInitialized = false;
   private boolean collectionsViewInitialized = false;
   private boolean groupsInitialized = false;
   
   private static DarSearchHandler darSearchHandler = new DarSearchHandler();
   
   public static DarSearchHandler getDarSearchHandler(){
	   return darSearchHandler;
   }
   
   private NavMode currentColNavMode = NavMode.LESS;   
   private Collection currentCollection;
   private CollectionManager collectionManager;
   
   private HashMap<String,Collection> collectionSelectionWidgets = new HashMap<String,Collection>();
   private HashMap<String,CollectionItem> collectionItemDeleteWidgets = new HashMap<String,CollectionItem>();
   private HashMap<String,CollectionUser> collectionUserDeleteWidgets = new HashMap<String,CollectionUser>();
   private HashMap<String,CollectionGroup> collectionGroupDeleteWidgets = new HashMap<String,CollectionGroup>();
   private HashMap<String,AppUser> newCollectionUserWidgets = new HashMap<String,AppUser>();
   private HashMap<String,Group> newCollectionGroupWidgets = new HashMap<String,Group>();
   
   //Group Name Comparator
   private class GroupNameComparator implements Comparator<Group> {
      @Override
      public int compare(Group a, Group b) {      
         return a.getName().compareTo(b.getName());
      }
   }
            
   //Generates a SearchHandlerParamPacket with the needed element IDs for an DAR search...so many :(
   public static SearchHandlerParamPacket generateDarSearchParamPacket() {
      SearchHandlerParamPacket packet = new SearchHandlerParamPacket();
      packet.setResultsContainerId(MCNT_SEARCH_RESULTS_CONTAINER);
      packet.setCounterElementId(MCNT_COUNTER);
      packet.setCounterContainerElementId(MCNT_COUNTER_CONTAINER);
      packet.setSearchBusyElementId(MCNT_SEARCH_BUSY);
      packet.setEmptyContributionMessageId(MCNT_EMTPY_RESULTS);
      packet.setContributionMessageContainerId(MC_MESSAGES);
      packet.setEditContentModalId(EDIT_CONTENT_MODAL);
      packet.setEditContentFileNameId(EDIT_CONTENT_FILENAME);
      packet.setEditContentDescriptionId(EDIT_CONTENT_DESC);
      packet.setEditContentFormId(EDIT_CONTENT_FORM);
      packet.setEditContentResourceIdId(EDIT_CONTENT_RESOURCE_ID);
      packet.setEditContentBusyId(EDIT_CONTENT_BUSY_ID);
      packet.setDeleteContentFormId(DELETE_CONTENT_FORM);
      packet.setDeleteContentConfirmModalId(DELETE_CONTENT_CONFIRM_MODAL);
      packet.setDeleteContentFileNameId(DELETE_CONTENT_FILENAME);
      packet.setDeleteContentResourceIdId(DELETE_CONTENT_RESOURCE_ID);
      packet.setDeleteContentBusyId(DELETE_CONTENT_BUSY_ID);
      packet.setGenMdContentFormId(GENMD_CONTENT_FORM);
      packet.setGenMdContentConfirmModalId(GENMD_CONTENT_CONFIRM_MODAL);
      packet.setGenMdContentFileNameId(GENMD_CONTENT_FILENAME);
      packet.setGenMdContentResourceIdId(GENMD_CONTENT_RESOURCE_ID);
      packet.setEditContentMdFileNameId(EDIT_CONTENT_MDFILENAME);   
      packet.setEditContentPreviewContainerId(EDIT_CONTENT_PREVIEW_CONTAINER);      
      packet.setEditContentTitleId(EDIT_CONTENT_TITLE);
      packet.setEditContentKeywordsId(EDIT_CONTENT_KEYWORDS);
      packet.setEditContentSecClassId(EDIT_CONTENT_SEC_CLASS);
      packet.setEditContentSecLevelId(EDIT_CONTENT_SEC_LEVEL);
      packet.setEditContentDistributionId(EDIT_CONTENT_DIST);
      packet.setEditContentFileFormatId(EDIT_CONTENT_FILEFORMAT);
      packet.setEditContentPublisherId(EDIT_CONTENT_PUBLISHER);
      packet.setEditContentOwnerId(EDIT_CONTENT_OWNER);
      packet.setEditContentCoverageId(EDIT_CONTENT_COVERAGE);
      packet.setEditContentInteractivityId(EDIT_CONTENT_INTERACTIVITY);
      packet.setEditContentEnvironmentId(EDIT_CONTENT_ENVIRONMENT);
      packet.setEditContentSkillId(EDIT_CONTENT_SKILL);
      packet.setEditContentLanguageId(EDIT_CONTENT_LANGUAGE);
      packet.setEditContentFileSizeId(EDIT_CONTENT_FILESIZE);
      packet.setEditContentDurationId(EDIT_CONTENT_DURATION);
      packet.setEditContentTechReqsId(EDIT_CONTENT_TECHREQ);
      packet.setEditContentPartOfId(EDIT_CONTENT_PARTOF);
      packet.setEditContentRequiresId(EDIT_CONTENT_REQUIRES);      
      packet.setEditContentObjectiveListId(EDIT_CONTENT_OBJ_LIST);
      packet.setEditContentObjectiveTitleId(EDIT_CONTENT_OBJ_TITLE_PREFIX);
      packet.setEditContentObjectiveDescId(EDIT_CONTENT_OBJ_DESC_PREFIX);
      packet.setEditContentObjectiveDeleteId(EDIT_CONTENT_OBJ_DELETE_PREFIX);
      packet.setEditContentObjectiveTextId(EDIT_CONTENT_OBJ_TEXT_PREFIX);
      
      packet.setEditContentCompetencyListId("editContentCompetencyList");
      
      packet.setPublishContentConfirmModalId(PUBLISH_CONTENT_CONFIRM_MODAL);
      packet.setPublishContentFileNameId(PUBLISH_CONTENT_FILENAME);
      packet.setPublishContentFormId(PUBLISH_CONTENT_FORM);
      packet.setPublishContentResourceIdId(PUBLISH_CONTENT_RESOURCE_ID);
      packet.setPublishContentLrNodeId(PUBLISH_CONTENT_LR_NODE);
      packet.setPublishContentBusyId(PUBLISH_CONTENT_BUSY);
      packet.setPublishContentSubmitBtnId(PUBLISH_SUBMIT_BTN);
      packet.setPublishContentCancelBtnId(PUBLISH_CANCEL_BTN);
      packet.setPublishContentSuccessContainerId(PUBLISH_SUCCESS_CONTAINER);
      packet.setPublishContentSuccessLrId(PUBLISH_SUCCESS_LR_ID);
      packet.setPublishContentFailContainerId(PUBLISH_FAIL_CONTAINER);
      packet.setPublishContentFailMessageId(PUBLISH_FAIL_MESSAGE);
      return packet;
   }
   
   //Searches and populates the user's contributions
   private void refreshMyContributionSearchResults() {
      darSearchHandler.performDarSearchByUserDate(DsSession.getUser().getUserId(), getTemplates().getDarSearchResultWidget().getText(), generateDarSearchParamPacket());
      contributionsInitialized = true;
   }
   
   //Handle add webpage
   private void handleAddWebpage() {
      DsUtil.hideLabel(ADD_WEBPAGE_BUSY);
      PageAssembler.closePopup(ADD_WEBPAGE_MODAL);      
      refreshMyContributionSearchResults();
   }
   
   //Add webpage listener
   protected EventCallback addWebpageListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.showLabel(ADD_WEBPAGE_BUSY);
         String webpageUrl = DsUtil.getTextBoxText(ADD_WEBPAGE_URL).trim();
         String webpageTitle = DsUtil.getTextBoxText(ADD_WEBPAGE_TITLE).trim();
         DsESBApi.decalsAddWebpage(webpageUrl, webpageTitle, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleAddWebpage();}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(ADD_WEBPAGE_BUSY);
               DsUtil.showSimpleErrorMessage(ADD_WEBPAGE_ERROR_CONTAINER,ADD_WEBPAGE_FAILED);
            }
         });
      }
   };
      
   //Adds the session ID to the outgoing form
   private void addSessionIdToForm() {
      DOM.getElementById(DsESBApi.DECALS_FORM_DATA_NAME).setAttribute("value", "{ \"" + DsESBApi.SESSION_ID_KEY + "\":\"" + ESBApi.sessionId + "\"}");
   }
   
   //Gets the file name from the fully qualified file name
   private String getFileNameFromFullName(String fullFileName) {
      if (fullFileName == null) return null;
      return fullFileName.substring(fullFileName.lastIndexOf("\\") + 1);
   }
   
   //Adds the file name to the outgoing form
   private void addUploadFileNameToForm(String fileName) {
      DOM.getElementById(ADD_FILE_DATA).setAttribute("name", fileName);
   }
   
   //Adds a file busy message to the message container
   private void addFileBusyMessage(String messageId, String fileName) {
      String messageText = "Uploading: <i>" + DsUtil.getMessageFileName(fileName) + "</i>...";
      DsUtil.generateMessage(MC_MESSAGES, messageId, messageText, ADD_FILE_BUSY_MESSAGE_CLASS, ADD_FILE_MESSAGE_CLOSE_PREFIX, ADD_FILE_BUSY_MESSAGE_IMG);
   }
   
   //File upload form submit handler   
   private class FileUploadFormSubmitHandler implements SubmitHandler {      
      private String messageId;
      private String fileName;      
      public FileUploadFormSubmitHandler(String messageId, String fileName) {
         this.messageId = messageId;
         this.fileName = fileName;
      }      
      @Override
      public void onSubmit(SubmitEvent event) {
         if (fileName == null || fileName.trim().isEmpty()) event.cancel();
         addUploadFileNameToForm(fileName);         
         addSessionIdToForm();         
         PageAssembler.closePopup(ADD_FILE_MODAL);
         addFileBusyMessage(messageId,fileName);
      }
   }
   
   //Adds a file upload complete message to the message container
   private void addFileCompleteMessage(String fileName) {
      String messageText = "Upload complete: <i>" + DsUtil.getMessageFileName(fileName) + "</i>...";
      String completeMessageId = DsUtil.generateId(ADD_FILE_COMPLETE_MESSAGE_PREFIX);
      DsUtil.generateMessage(MC_MESSAGES,completeMessageId, messageText, ADD_FILE_COMPLETE_MESSAGE_CLASS, ADD_FILE_COMPLETE_MESSAGE_CLOSE_PREFIX,null);
   }
   
   //File upload form submit complete handler   
   private class FileUploadSubmitCompleteHandler implements SubmitCompleteHandler {      
      private String busyMessageId;
      private String fileName;      
      public FileUploadSubmitCompleteHandler(String busyMessageId, String fileName) {
         this.busyMessageId = busyMessageId;
         this.fileName = fileName;
      }      
      @Override
      public void onSubmitComplete(SubmitCompleteEvent event) {
         DsUtil.hideLabel(busyMessageId);
         addFileCompleteMessage(fileName);
         refreshMyContributionSearchResults();
      }
   }
   
   //Add file listener
   protected EventCallback addFileListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         //DsUtil.showLabel(ADD_FILE_BUSY);
         final FormPanel fileUploadForm = (FormPanel)PageAssembler.elementToWidget(ADD_FILE_FORM, PageAssembler.FORM);
         String uploadMessageId = DsUtil.generateId(ADD_FILE_MESSAGE_PREFIX);
         FileUpload fileUploadData = (FileUpload)PageAssembler.elementToWidget(ADD_FILE_DATA, PageAssembler.FILE);
         if (fileUploadData != null) {
            String fileName = getFileNameFromFullName(fileUploadData.getFilename());
            if (fileName != null && !fileName.trim().isEmpty()) {
               fileUploadForm.addSubmitHandler(new FileUploadFormSubmitHandler(uploadMessageId,fileName));
               fileUploadForm.addSubmitCompleteHandler(new FileUploadSubmitCompleteHandler(uploadMessageId,fileName));
               fileUploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
               fileUploadForm.setAction(ESBApi.getESBActionURL(DsESBApi.FILE_UPLOAD_SERVICE) + "?inline=true");
               fileUploadForm.submit();
            }
         }
      }
   };
   
   //attempt to keep track of descriptions being edited
   private void syncDescription() {
      if (currentCollection != null && currentCollection.isMetadataBeingChanged()) {
         currentCollection.setDescription(DsUtil.getTextAreaText(CCOL_DESC_TEXT_AREA));
      }
   }
   
   private void setCollectionExportForm(){
	   InputElement.as(DOM.getElementById(CCOL_EXPORT_FORM_SESSION)).setValue(DsESBApi.sessionId);
	   InputElement.as(DOM.getElementById(CCOL_EXPORT_FORM_COLLECTION_ID)).setValue(currentCollection.getCollectionId());
   }
   
   //collection selection event listener
   private class SelectCollectionClickListener extends EventCallback {      
      private Collection col;    
      public SelectCollectionClickListener(Collection col) {
         this.col = col;
      }      
      @Override
      public void onEvent(Event event) {  
         syncDescription();
         currentCollection = col;
         buildCurrentCollectionView();
      }
   }
   
   //registers the collection selection widgets
   private void registerCollectionSelectionWidgets() {
      for (String key:collectionSelectionWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new SelectCollectionClickListener(collectionSelectionWidgets.get(key)));
      }
   }
   
   //builds the collection navigation
   private void buildCollectionNavigation() {
      int numToShow = NUMBER_OF_INITIAL_SELECTIONS_SHOWN;
      if (NavMode.MORE.equals(currentColNavMode)) numToShow = CollectionsViewBuilder.UNLIMITED;
      collectionSelectionWidgets.clear();      
      CollectionsViewBuilder.buildCollectionNavigation(MCOL_SELECTIONS,MCOL_MORE_SELECTIONS_CONTAINER,MCOL_LESS_SELECTIONS_CONTAINER,
            collectionManager.getCollectionList(),numToShow,collectionSelectionWidgets);
      registerCollectionSelectionWidgets();
   }
   
   //show all collections navigation listener
   protected EventCallback showAllCollectionsNavListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         currentColNavMode = NavMode.MORE;
         buildCollectionNavigation();
      }
   };
   
   //show less collections navigation listener
   protected EventCallback showLessCollectionsNavListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         currentColNavMode = NavMode.LESS;
         buildCollectionNavigation();
      }
   };
   
   //handle delete collection item
   private void handleDeleteCollectionItem(CollectionItem ci) {
      DsUtil.setLabelText(DCI_COL_NAME,currentCollection.getName());
      DsUtil.setLabelText(DCI_ITEM_NAME,ci.getResourceTitle());
      DsUtil.setLabelText(DCI_ITEM_ID,ci.getResourceUrl());      
      DsUtil.setLabelText(DCI_COL_ID,currentCollection.getCollectionId());
      PageAssembler.openPopup(DCI_MODAL); 
   }
   
   //delete collection item click event listener
   private class DeleteCollectionItemClickListener extends EventCallback {      
      private CollectionItem ci;    
      public DeleteCollectionItemClickListener(CollectionItem ci) {
         this.ci = ci;
      }      
      @Override
      public void onEvent(Event event) {handleDeleteCollectionItem(ci);}
   }
   
   //register collection item delete widget event handlers
   private void registerCollectionItemDeleteWidgets() {
      for (String key:collectionItemDeleteWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new DeleteCollectionItemClickListener(collectionItemDeleteWidgets.get(key)));
      }
   } 
   
   //handle delete collection user
   private void handleDeleteCollectionUser(CollectionUser cu) {
      DsUtil.setLabelText(DCU_COL_NAME,currentCollection.getName());
      DsUtil.setLabelText(DCU_USER_NAME,cu.getFullName());
      DsUtil.setLabelText(DCU_USER_ID,cu.getUserId());      
      DsUtil.setLabelText(DCU_COL_ID,currentCollection.getCollectionId());
      PageAssembler.openPopup(DCU_MODAL); 
   }
   
   //delete collection user click event listener
   private class DeleteCollectionUserClickListener extends EventCallback {      
      private CollectionUser cu;    
      public DeleteCollectionUserClickListener(CollectionUser cu) {
         this.cu = cu;
      }      
      @Override
      public void onEvent(Event event) {handleDeleteCollectionUser(cu);}
   }
   
   //register collection user delete widget event handlers
   private void registerCollectionUserDeleteWidgets() {
      for (String key:collectionUserDeleteWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new DeleteCollectionUserClickListener(collectionUserDeleteWidgets.get(key)));
      }
   } 
   
   //delete collection group submit handler
   protected EventCallback deleteCollectionGroupSubmitHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         String groupId = DsUtil.getLabelText(DCG_GROUP_ID);
         String collectionId = DsUtil.getLabelText(DCG_COL_ID);
         collectionManager.removeCollectionGroup(collectionId,groupId);
         setCollectionAsChanged();
         buildCurrentCollectionView();         
         PageAssembler.closePopup(DCG_MODAL);  
      }
   };
   
   //handle delete collection group
   private void handleDeleteCollectionGroup(CollectionGroup cg) {      
      DsUtil.setLabelText(DCG_COL_NAME,currentCollection.getName());
      DsUtil.setLabelText(DCG_GROUP_NAME,cg.getName());
      DsUtil.setLabelText(DCG_GROUP_ID,cg.getGroupId());      
      DsUtil.setLabelText(DCG_COL_ID,currentCollection.getCollectionId());
      PageAssembler.openPopup(DCG_MODAL); 
   }
   
   //delete collection group click event listener
   private class DeleteCollectionGroupClickListener extends EventCallback {      
      private CollectionGroup cg;    
      public DeleteCollectionGroupClickListener(CollectionGroup cg) {
         this.cg = cg;
      }      
      @Override
      public void onEvent(Event event) {handleDeleteCollectionGroup(cg);}
   }
   
   //register collection group delete widget event handlers
   private void registerCollectionGroupDeleteWidgets() {
      for (String key:collectionGroupDeleteWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new DeleteCollectionGroupClickListener(collectionGroupDeleteWidgets.get(key)));
      }
   } 
   
   //builds the current collection view
   private void buildCurrentCollectionView() { 
      DsUtil.setLabelText(ACU_COL_NAME,currentCollection.getName());
      DsUtil.setLabelText(ACI_COL_NAME,currentCollection.getName());      
      DsUtil.setLabelText(ACG_COL_NAME,currentCollection.getName());
      collectionItemDeleteWidgets.clear();
      collectionUserDeleteWidgets.clear();
      collectionGroupDeleteWidgets.clear();
      CollectionsViewBuilder.populateCollectionData(currentCollection,collectionItemDeleteWidgets,collectionUserDeleteWidgets,collectionGroupDeleteWidgets);
      registerCollectionItemDeleteWidgets();
      registerCollectionUserDeleteWidgets();
      registerCollectionGroupDeleteWidgets();
      if (currentCollection.getNumberofItems() > 0 && currentCollection.sessionUserCanModify()) {
         initCollectionsSortable(CCOL_LIST_NAME);
      }
      
      setCollectionExportForm();
   }
      
   //builds the collections view
   private void buildCollectionsView() {      
      DsUtil.hideLabel(MCOL_BUSY);
      buildCollectionNavigation();
      if (collectionManager.getNumberOfCollections() <= 0) {
         DsUtil.hideLabel(MCOL_CURRENT_CONTAINER);
         DsUtil.showLabel(MCOL_NONE);
      }
      else {         
         currentCollection = collectionManager.getCollectionList().get(0);
         buildCurrentCollectionView();
         DsUtil.hideLabel(MCOL_NONE);         
         DsUtil.showLabel(MCOL_CURRENT_CONTAINER);
      }
   }
      
   //switch to my contributions view
   protected EventCallback gotoMyContributionsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (!contributionsInitialized) refreshMyContributionSearchResults();
         toggleView(MY_CONTRIBUTIONS_LINK_TEXT,MY_CONTRIBUTIONS_CONTAINER);         
      }
   };  
   
   //switch to my collections view
   protected EventCallback gotoMyCollectionsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (!collectionsViewInitialized) {
            collectionsViewInitialized = true;
            buildCollectionsView();            
         }
         toggleView(MY_COLLECTIONS_LINK_TEXT,MY_COLLECTIONS_CONTAINER);                           
      }
   };
   
   //switch to my groups view
   protected EventCallback gotoMyGroupsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (!groupsInitialized) {
            groupsInitialized = true;
            initializeGroupsView();            
         }
         toggleView(MY_GROUPS_LINK_TEXT,MY_GROUPS_CONTAINER);                  
      }
   };
   
   //toggles the view
   private void toggleView(String navTextId, String contentContainerId) {
      DsUtil.setLabelAttribute(MY_COLLECTIONS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(MY_CONTRIBUTIONS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(MY_GROUPS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(navTextId, "class", "active");
      DsUtil.hideLabel(MY_COLLECTIONS_CONTAINER);
      DsUtil.hideLabel(MY_GROUPS_CONTAINER);
      DsUtil.hideLabel(MY_CONTRIBUTIONS_CONTAINER);
      DsUtil.showLabel(contentContainerId);
   }
   
   
   //show collection items listener
   protected EventCallback showCollectionItemsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         toggleCollectionView(CCOL_ITEMS_LINK_TEXT,CCOL_ITEMS_CONTAINER,CCOL_ADD_ITEM_LINK);         
      }
   };
   
   //show collection users listener
   protected EventCallback showCollectionUsersListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         toggleCollectionView(CCOL_USERS_LINK_TEXT,CCOL_USERS_CONTAINER,CCOL_ADD_USER_LINK);         
      }
   };
   
   //show collection groups listener
   protected EventCallback showCollectionGroupsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         toggleCollectionView(CCOL_GROUPS_LINK_TEXT,CCOL_GROUPS_CONTAINER,CCOL_ADD_GROUP_LINK);         
      }
   };
   
   //toggles the collection view
   private void toggleCollectionView(String navTextId, String contentContainerId, String addButtonId) {
      DsUtil.setLabelAttribute(CCOL_ITEMS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(CCOL_USERS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(CCOL_GROUPS_LINK_TEXT, "class", "");
      DsUtil.setLabelAttribute(navTextId, "class", "active");
      DsUtil.hideLabel(CCOL_ITEMS_CONTAINER);
      DsUtil.hideLabel(CCOL_USERS_CONTAINER);
      DsUtil.hideLabel(CCOL_GROUPS_CONTAINER);      
      DsUtil.showLabel(contentContainerId);
      DsUtil.hideLabel(CCOL_ADD_USER_LINK);
      DsUtil.hideLabel(CCOL_ADD_ITEM_LINK);
      DsUtil.hideLabel(CCOL_ADD_GROUP_LINK);
      DsUtil.showLabel(addButtonId);
   }

   //initializes collections list sorting
   private final native String initCollectionsSortable(String listId) /*-{
      $wnd.initListSort(listId);
   }-*/;
   
   //Builds an item order map for the current collection based on the current 
   //order of the list items on the page
   private HashMap<String,Integer> buildCollectionItemOrderMap() {
      HashMap<String,Integer> orderMap = new HashMap<String,Integer>();
      Element listContainer = DOM.getElementById(CCOL_LIST_NAME);
      if (listContainer == null) return orderMap;      
      NodeList<Element> collectionElements = listContainer.getElementsByTagName("li");
      Element ce;
      for (int i=0;i<collectionElements.getLength();i++){
         ce = (Element)collectionElements.getItem(i);
         if (ce != null) orderMap.put(ce.getId(),new Integer(i));
      }
      return orderMap;      
   }
   
   //Builds an user access map for the current collection based on the values
   //of the access drop downs on the page
   private HashMap<String,String> buildCollectionUserAccessMap() {
      HashMap<String,String> accessMap = new HashMap<String,String>();
      for (CollectionUser cu:currentCollection.getCollectionUsers()) {
         //session user can't change his/her access so this value is just a label
         if (DsSession.getUser().getUserId().equalsIgnoreCase(cu.getUserId())) {
            accessMap.put(cu.getLocatorKey(),DsUtil.getLabelText(cu.getLocatorKey() + CollectionsViewBuilder.CCOL_ACCESS_DD_SUFFIX));
         }
         else {
            accessMap.put(cu.getLocatorKey(),DsUtil.getDropDownSelectedText(cu.getLocatorKey() + CollectionsViewBuilder.CCOL_ACCESS_DD_SUFFIX));
         }                  
      }
      return accessMap;      
   }
   
   //Builds an group access map for the current collection based on the values
   //of the access drop downs on the page
   private HashMap<String,String> buildCollectionGroupAccessMap() {
      HashMap<String,String> accessMap = new HashMap<String,String>();
      for (CollectionGroup cg:currentCollection.getCollectionGroups()) {
         accessMap.put(cg.getLocatorKey(),DsUtil.getDropDownSelectedText(cg.getLocatorKey() + CollectionsViewBuilder.CCOL_ACCESS_DD_SUFFIX));                           
      }
      return accessMap;      
   }
   
   protected EventCallback toggleViewCollectionMetadataListener = new EventCallback() {
	@Override
	public void onEvent(Event event) {
		CollectionsViewBuilder.toggleShowCollectionMetadata(currentCollection);
	}
};
   
   //show collection users listener
   protected EventCallback editCollectionDescriptionListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         currentCollection.setMetadataBeingChanged(true);
         currentCollection.setHasChanged(true);
         CollectionsViewBuilder.setUpCollectionDescriptionBeingChanged(currentCollection);
         CollectionsViewBuilder.showEditMetadata(currentCollection);
      }
   };
      
   //handle post collection save
   private void handlePostCollectionSave(JSONObject colRes) {     
      collectionManager.replaceCollection(new Collection(colRes));
      buildCollectionsView();
      CollectionsViewBuilder.generateCollectionSaveSuccessMessage();   
   }

   //current collection save handler
   protected EventCallback currentCollectionSaveHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {   
         HashMap<String,Integer> itemOrderMap = buildCollectionItemOrderMap();
         HashMap<String,String> userAccessMap = buildCollectionUserAccessMap();
         HashMap<String,String> groupAccessMap = buildCollectionGroupAccessMap();
         String description = DsUtil.getTextAreaText(CCOL_DESC_TEXT_AREA);
         String keywords = DsUtil.getTextAreaText(CollectionsViewBuilder.METADATA_EDIT_KEYWORDS);
         String coverage = InputElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_COVERAGE)).getValue();
         String environment = SelectElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_ENVIRONMENT)).getValue();
         
         Element objectiveListElement = DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_LIST).getFirstChildElement();
         
         ArrayList<DarResourceObjective> objectives = new ArrayList<DarResourceObjective>();
         while(objectiveListElement != null){
        	 if(!objectiveListElement.getId().equalsIgnoreCase("noObjectivesItem")){
	        	 Element p = Element.as(objectiveListElement.getChild(1));
	        	 objectives.add(new DarResourceObjective(p.getInnerText(), p.getAttribute("data-description")));
        	 }
        	 objectiveListElement = objectiveListElement.getNextSiblingElement();
         }
         
         collectionManager.updateCollection(currentCollection.getCollectionId(),description,itemOrderMap,userAccessMap,groupAccessMap,
        		 keywords, coverage, environment, objectives, null);    

         DsESBApi.decalsUpdateCollection(currentCollection.getCollectionId(), currentCollection.toJson(), new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handlePostCollectionSave(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.handleFailedApiCall(caught);
            }
         });         
      }
   };
   
      
   //handle add collection response
   private void handleAddCollectionResponse(JSONObject colRes) {
      syncDescription();
      collectionManager.addCollection(new Collection(colRes));
      buildCollectionsView();
      DsUtil.hideLabel(AC_BUSY);
      DsUtil.showLabel(AC_SUBMIT_BTNS);
      //DsUtil.showLabel(AC_SUCCESS);
      contributionsInitialized = false;
      PageAssembler.closePopup(AC_MODAL);
   }
   
   //add collection handler
   protected EventCallback addCollectionHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         String name = DsUtil.getTextBoxText(AC_NAME);
         name = collectionManager.generateUniqueCollectionName(name);
         String desc = DsUtil.getTextAreaText(AC_DESC);
         DsUtil.hideLabel(AC_SUBMIT_BTNS);
         DsUtil.showLabel(AC_BUSY);
         DsESBApi.decalsCreateCollection(name, desc, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleAddCollectionResponse(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(AC_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });
      }
   };
   
   //handle delete collection response
   private void handleDeleteCollectionResponse(String collectionId) {
      collectionManager.removeCollection(collectionId);
      buildCollectionsView();
      DsUtil.hideLabel(DC_BUSY);   
      DsUtil.showLabel(DC_SUCCESS);
      contributionsInitialized = false;
   }
   
   //delete collection handler
   protected EventCallback deleteCollectionHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         final String collectionId = DsUtil.getLabelText(DC_COL_ID);
         DsUtil.hideLabel(DC_SUBMIT_BTNS);
         DsUtil.showLabel(DC_BUSY);
         DsESBApi.decalsDeleteCollection(collectionId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleDeleteCollectionResponse(collectionId);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(DC_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });
      }
   };
   
   //delete collection click handler
   protected EventCallback deleteCollectionClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) { 
         DsUtil.setLabelText(DC_NAME,currentCollection.getName());
         DsUtil.setLabelText(DC_COL_ID,currentCollection.getCollectionId());
         DsUtil.hideLabel(DC_SUCCESS);
         DsUtil.showLabel(DC_SUBMIT_BTNS);
         PageAssembler.openPopup(DC_CONFIRM_MODAL);
      }
   };
   
   //handle reset collection response
   private void handleResetCollectionResponse(JSONObject colRes) {
      Collection col = new Collection(colRes);
      collectionManager.replaceCollection(col);
      currentCollection = col;
      buildCurrentCollectionView();
      DsUtil.hideLabel(RC_BUSY);   
      //DsUtil.showLabel(RC_SUCCESS);
      PageAssembler.closePopup(RC_CONFIRM_MODAL);
   }
   
   //reset collection handler
   protected EventCallback resetCollectionHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         final String collectionId = DsUtil.getLabelText(RC_COL_ID);
         DsUtil.hideLabel(RC_SUBMIT_BTNS);
         DsUtil.showLabel(RC_BUSY);
         DsESBApi.decalsGetCollection(collectionId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleResetCollectionResponse(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(RC_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });
      }
   };
   
   //reset collection click handler
   protected EventCallback resetCollectionClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) { 
         DsUtil.setLabelText(RC_NAME,currentCollection.getName());
         DsUtil.setLabelText(RC_COL_ID,currentCollection.getCollectionId());
         DsUtil.hideLabel(RC_SUCCESS);
         DsUtil.showLabel(RC_SUBMIT_BTNS);
         PageAssembler.openPopup(RC_CONFIRM_MODAL);
      }
   };
   
   //displays the collection changed message
   private void setCollectionAsChanged() {
      currentCollection.setHasChanged(true);
      //DsUtil.showLabel(CCOL_CHANGED_MESSAGE);
   }   
   
   //add collection item handler
   protected EventCallback addCollectionItemHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {         
         String aciTitle = DsUtil.getTextBoxText(ACI_TITLE);
         String aciUrl = DsUtil.getTextBoxText(ACI_URL);
         String aciDesc = DsUtil.getTextBoxText(ACI_DESC);
         boolean added = collectionManager.addCollectionItem(currentCollection.getCollectionId(),aciTitle,aciUrl,aciDesc);
         if (added) {
            setCollectionAsChanged();
            buildCurrentCollectionView(); 
            PageAssembler.closePopup(ACI_MODAL);
            refreshMyContributionSearchResults();
         }
         else {
            DsUtil.hideLabel(ACI_SUBMIT_BTNS);
            DsUtil.showLabel(ACI_EXISTS);
         }
      }
   };
   
   //add collection item handler
   protected EventCallback addCollectionItemReplaceHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         String aciTitle = DsUtil.getTextBoxText(ACI_TITLE);
         String aciUrl = DsUtil.getTextBoxText(ACI_URL);
         String aciDesc = DsUtil.getTextBoxText(ACI_DESC);
         collectionManager.overwriteCollectionItem(currentCollection.getCollectionId(),aciTitle,aciUrl,aciDesc);
         setCollectionAsChanged();
         buildCurrentCollectionView();         
         PageAssembler.closePopup(ACI_MODAL);         
      }
   };
   
   //initializes new collection group list filtering
   private final native String initNewCollectionGroupListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var newCollectionGroupOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var newCollectionGroupList = new $wnd.List(listContainer, newCollectionGroupOptions);      
   }-*/;
   
   //initializes new collection user list filtering
   private final native String initNewCollectionUserListFiltering(String listContainer, String searchField1, String searchField2, int itemsPerPage) /*-{
      var newCollectionUserOptions = {
         valueNames: [searchField1,searchField2],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var newCollectionUserList = new $wnd.List(listContainer, newCollectionUserOptions);      
   }-*/;
   
   //handle add new collection user
   private void handleAddNewCollectionUser(AppUser cu) {
      DsUtil.setLabelText(ACU_CONFIRM_USER_NAME,cu.getFullName());
      DsUtil.setLabelText(ACU_CONFIRM_USER_FIRST_NAME,cu.getFirstName());      
      DsUtil.setLabelText(ACU_CONFIRM_USER_LAST_NAME,cu.getLastName());
      DsUtil.setLabelText(ACU_CONFIRM_USER_ID,cu.getUserId());
      DsUtil.hideLabel(ACU_PICKLIST);      
      DsUtil.showLabel(ACU_CONFIRM); 
   }
   
   //add new collection user event listener
   private class AddCollectionUserClickListener extends EventCallback {      
      private AppUser cu;    
      public AddCollectionUserClickListener(AppUser cu) {
         this.cu = cu;
      }      
      @Override
      public void onEvent(Event event) {handleAddNewCollectionUser(cu);}
   }
   
   //register new collection user widget event handlers
   private void registerNewCollectionUserWidgets() {
      for (String key:newCollectionUserWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new AddCollectionUserClickListener(newCollectionUserWidgets.get(key)));
      }
   }
   
   //sets up the add collection user modal
   private void setUpAddCollectionUserModal() {
      DsUtil.hideLabel(ACU_CONFIRM);
      DsUtil.showLabel(ACU_PICKLIST);         
      DsUtil.showLabel(ACU_CANCEL_BTN);
      newCollectionUserWidgets.clear();
      ArrayList<AppUser> selectList = collectionManager.removeCollectionUsersFromUserList(currentCollection.getCollectionId(),fullUserList);
      CollectionsViewBuilder.buildNewCollectionUserList(ACU_INNER_CONTAINER,selectList,newCollectionUserWidgets);
      registerNewCollectionUserWidgets();
      initNewCollectionUserListFiltering(ACU_USER_LIST_CONTAINER,ACU_USER_NAME,ACU_USER_ID,LIST_ITEMS_PER_PAGE);
      PageAssembler.openPopup(ACU_MODAL);      
   }
   
   //add collection user click handler
   protected EventCallback addCollectionUserClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         DsESBApi.decalsGetUserList(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               DsUtil.buildUserTypeListFromReturn(fullUserList,result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
               setUpAddCollectionUserModal();
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
         });   
      }
   };
   
   //add collection item click handler
   protected EventCallback addCollectionItemClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         DsUtil.resetForm(ACI_FORM);
         DsUtil.hideLabel(ACI_EXISTS);
         DsUtil.showLabelInline(ACI_SUBMIT_BTNS);
         PageAssembler.openPopup(ACI_MODAL);
      }
   };
   
   //add collection user submit handler
   protected EventCallback addCollectionUserSubmitHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         String userId = DsUtil.getLabelText(ACU_CONFIRM_USER_ID);   
         String firstName = DsUtil.getLabelText(ACU_CONFIRM_USER_FIRST_NAME);
         String lastName = DsUtil.getLabelText(ACU_CONFIRM_USER_LAST_NAME);
         String access = DsUtil.getDropDownSelectedText(ACU_CONFIRM_ACCESS);
         collectionManager.overwriteCollectionUser(currentCollection.getCollectionId(),userId,firstName,lastName,access);
         setCollectionAsChanged();
         buildCurrentCollectionView();         
         PageAssembler.closePopup(ACU_MODAL);  
      }
   };
   
   //delete collection user submit handler
   protected EventCallback deleteCollectionUserSubmitHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         String userId = DsUtil.getLabelText(DCU_USER_ID);
         String collectionId = DsUtil.getLabelText(DCU_COL_ID);
         collectionManager.removeCollectionUser(collectionId,userId);
         setCollectionAsChanged();
         buildCurrentCollectionView();         
         PageAssembler.closePopup(DCU_MODAL);  
      }
   };
   
   //delete collection item submit handler
   protected EventCallback deleteCollectionItemSubmitHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         String resourceUrl = DsUtil.getLabelText(DCI_ITEM_ID);
         String collectionId = DsUtil.getLabelText(DCI_COL_ID);
         collectionManager.removeCollectionItem(collectionId,resourceUrl);
         setCollectionAsChanged();
         buildCurrentCollectionView();         
         PageAssembler.closePopup(DCI_MODAL);  
      }
   };
   
   //add collection group submit handler
   protected EventCallback addCollectionGroupSubmitHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {    
         String groupId = DsUtil.getLabelText(ACG_CONFIRM_GROUP_ID);   
         String groupName = DsUtil.getLabelText(ACG_CONFIRM_GROUPH_NAME);
         String groupType = DsUtil.getLabelText(ACG_CONFIRM_GROUP_TYPE);
         String access = DsUtil.getDropDownSelectedText(ACG_CONFIRM_ACCESS);
         collectionManager.overwriteCollectionGroup(currentCollection.getCollectionId(),groupId,groupName,groupType,access);
         setCollectionAsChanged();
         buildCurrentCollectionView();         
         PageAssembler.closePopup(ACG_MODAL);  
      }
   };
   
   //handle add new collection group
   private void handleAddNewCollectionGroup(Group cg) {
      DsUtil.setLabelText(ACG_CONFIRM_GROUP_NAME,cg.getName());
      DsUtil.setLabelText(ACG_CONFIRM_GROUPH_NAME,cg.getName());      
      DsUtil.setLabelText(ACG_CONFIRM_GROUP_ID,cg.getGroupId());
      DsUtil.setLabelText(ACG_CONFIRM_GROUP_TYPE,cg.getGroupType());
      DsUtil.hideLabel(ACG_PICKLIST);      
      DsUtil.showLabel(ACG_CONFIRM); 
   }
   
   //add new collection group event listener
   private class AddCollectionGroupClickListener extends EventCallback {      
      private Group cg;    
      public AddCollectionGroupClickListener(Group cg) {
         this.cg = cg;
      }      
      @Override
      public void onEvent(Event event) {handleAddNewCollectionGroup(cg);}
   }
   
   //register new collection group widget event handlers
   private void registerNewCollectionGroupWidgets() {
      for (String key:newCollectionGroupWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new AddCollectionGroupClickListener(newCollectionGroupWidgets.get(key)));
      }
   }
   
   //sets up the add collection group modal
   private void setUpAddCollectionGroupModal(ArrayList<Group> groupList) {
      DsUtil.hideLabel(ACG_CONFIRM);
      DsUtil.showLabel(ACG_PICKLIST);         
      DsUtil.showLabel(ACG_CANCEL_BTN);
      newCollectionGroupWidgets.clear();
      ArrayList<Group> selectList = collectionManager.removeCollectionGroupsFromGroupList(currentCollection.getCollectionId(),groupList);
      CollectionsViewBuilder.buildNewCollectionGroupList(ACG_INNER_CONTAINER,selectList,newCollectionGroupWidgets);
      registerNewCollectionGroupWidgets();
      initNewCollectionGroupListFiltering(ACG_GROUP_LIST_CONTAINER,ACG_GROUP_NAME,ACG_GROUP_TYPE,LIST_ITEMS_PER_PAGE);
      PageAssembler.openPopup(ACG_MODAL);      
   }
   
   //combines and sorts two group lists
   private ArrayList<Group> combineAndSortGroups(ArrayList<Group> list1, ArrayList<Group> list2) {
      ArrayList<Group> retList = new ArrayList<Group>();
      retList.addAll(list1);
      retList.addAll(list2);            
      Comparator<Group> gnc = new GroupNameComparator();
      Collections.sort(retList, gnc);
      return retList;
   }
   
   //add collection group click handler
   protected EventCallback addCollectionGroupClickHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsESBApi.decalsGetPublicGroups(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {               
               final GroupManager pgm = new GroupManager();
               pgm.initGroupList(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
               if (groupsInitialized) setUpAddCollectionGroupModal(combineAndSortGroups(pgm.getGroupList(),groupManager.getGroupList()));
               else {
                  DsESBApi.decalsGetUserPrivateGroups(new ESBCallback<ESBPacket>() {
                     @Override
                     public void onSuccess(ESBPacket result) {               
                        GroupManager pgm2 = new GroupManager();
                        pgm2.initGroupList(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
                        setUpAddCollectionGroupModal(combineAndSortGroups(pgm.getGroupList(),pgm2.getGroupList()));                        
                     }
                     @Override
                     public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
                  });
               }
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
         });   
      }
   };
   
   //initializes the group elements
   private void initGroupElements() {
      grpNavContainer = MGRP_NAV_CONTAINER;
      emptyGroupMessageContainer = MGRP_NONE;   
      groupBusyContainer = MGRP_BUSY;
      currentGroupContainer = MGRP_CURRENT_CONTAINER;
      groupSelectionContainer = MGRP_SELECTIONS;
      moreGroupsSelectionContainer = MGRP_MORE_SELECTIONS_CONTAINER;
      moreGroupsSelectionLink = MGRP_MORE_SELECTIONS_LINK;
      lessGroupsSelectionContainer = MGRP_LESS_SELECTIONS_CONTAINER;
      lessGroupsSelectionLink = MGRP_LESS_SELECTIONS_LINK;     
   }
   
   //Display handler for my contributions
   @Override
   public void display() {
	  validateSession();
      initGroupElements();
      
      instanceGroupType = GroupTypeEnum.PRIVATE;
      collectionManager = DsSession.getUserCollectionManager();
      DsUserTabsHandler.getInstance().init(getDispatcher());
      DsUserTabsHandler.getInstance().setCurrentTab(UserTabs.HOME_TAB);
      PageAssembler.ready(new HTML(getTemplates().getUserHomePanel().getText()));
      PageAssembler.buildContents();
      DsHeaderHandler dhh = new DsHeaderHandler(getDispatcher());
      dhh.setUpHeader(DsSession.getUser().getFirstName(), DsSession.getUser().getEmailAddress());
      PageAssembler.attachHandler(ADD_WEBPAGE_FORM,VALID_EVENT,addWebpageListener);
      PageAssembler.attachHandler(ADD_FILE_BUTTON,Event.ONCLICK,addFileListener);
      PageAssembler.attachHandler(MY_COLLECTIONS_LINK,Event.ONCLICK,gotoMyCollectionsListener);
      PageAssembler.attachHandler(MY_CONTRIBUTIONS_LINK,Event.ONCLICK,gotoMyContributionsListener);
      PageAssembler.attachHandler(MCOL_MORE_SELECTIONS_LINK,Event.ONCLICK,showAllCollectionsNavListener);      
      PageAssembler.attachHandler(MCOL_LESS_SELECTIONS_LINK,Event.ONCLICK,showLessCollectionsNavListener);
      PageAssembler.attachHandler(CCOL_EDIT_DESC,Event.ONCLICK,editCollectionDescriptionListener);
      PageAssembler.attachHandler(CCOL_SAVE_LINK,Event.ONCLICK,currentCollectionSaveHandler);
      PageAssembler.attachHandler(AC_FORM,VALID_EVENT,addCollectionHandler);
      PageAssembler.attachHandler(CCOL_DELETE_LINK,Event.ONCLICK,deleteCollectionClickHandler);
      PageAssembler.attachHandler(DC_FORM,VALID_EVENT,deleteCollectionHandler);
      PageAssembler.attachHandler(CCOL_RESET_LINK,Event.ONCLICK,resetCollectionClickHandler);
      PageAssembler.attachHandler(RC_FORM,VALID_EVENT,resetCollectionHandler);
      PageAssembler.attachHandler(ACI_FORM,VALID_EVENT,addCollectionItemHandler);
      PageAssembler.attachHandler(ACI_REPLACE,Event.ONCLICK,addCollectionItemReplaceHandler);
      PageAssembler.attachHandler(CCOL_ADD_USER_LINK,Event.ONCLICK,addCollectionUserClickHandler);      
      PageAssembler.attachHandler(CCOL_ADD_ITEM_LINK,Event.ONCLICK,addCollectionItemClickHandler);
      PageAssembler.attachHandler(CCOL_ADD_GROUP_LINK,Event.ONCLICK,addCollectionGroupClickHandler);
      PageAssembler.attachHandler(ACU_FORM,VALID_EVENT,addCollectionUserSubmitHandler);
      PageAssembler.attachHandler(DCU_FORM,VALID_EVENT,deleteCollectionUserSubmitHandler);
      PageAssembler.attachHandler(DCG_FORM,VALID_EVENT,deleteCollectionGroupSubmitHandler);
      PageAssembler.attachHandler(DCI_FORM,VALID_EVENT,deleteCollectionItemSubmitHandler);      
      PageAssembler.attachHandler(MY_GROUPS_LINK,Event.ONCLICK,gotoMyGroupsListener);  
      PageAssembler.attachHandler(CCOL_ITEMS_LINK,Event.ONCLICK,showCollectionItemsListener);
      PageAssembler.attachHandler(CCOL_USERS_LINK,Event.ONCLICK,showCollectionUsersListener);
      PageAssembler.attachHandler(CCOL_GROUPS_LINK,Event.ONCLICK,showCollectionGroupsListener);
      PageAssembler.attachHandler(ACG_FORM,VALID_EVENT,addCollectionGroupSubmitHandler);
      
      FormElement.as(DOM.getElementById(CCOL_EXPORT_FORM)).setAction(DsESBApi.getESBActionURL("decalsExportTrademPackage"));
      
      PageAssembler.attachHandler(CollectionsViewBuilder.METADATA_TOGGLE_ID, Event.ONCLICK, toggleViewCollectionMetadataListener);
      
      PageAssembler.attachHandler(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_LINK, Event.ONCLICK, addObjectiveListener);
      
      PageAssembler.attachHandler(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_ADD_BTN, Event.ONCLICK, addObjectiveBtnListener);
      PageAssembler.attachHandler(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_CANCEL_BTN, Event.ONCLICK, cancelAddObjectiveListener);
      
      PageAssembler.attachHandler(CollectionsViewBuilder.METADATA_EDIT_ENVIRONMENT, Event.ONCHANGE, environmentChangedListener);
      
      PageAssembler.attachHandler("editContentAddCompetency", Event.ONCLICK, alignCompetencyListener);
      PageAssembler.attachHandler("editContentAddCompetencyUri", Event.ONCHANGE, competencyUriChangedListener);
      
      attachGroupHandlers();
      refreshMyContributionSearchResults();      
   }
   
   
   	public static EventCallback competencyUriChangedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			String competencyUri = InputElement.as(DOM.getElementById("editContentAddCompetencyUri")).getValue();
			
			if(DOM.getElementById("editContentCompetencyList").getInnerHTML().contains(competencyUri)){
				DsUtil.alert("Resource is already aligned with this competency");
				return;
			}
			
			
			DOM.getElementById("editCompetencySearching").removeClassName("hidden");
			
			DsESBApi.decalsGetCompetencyInfo(competencyUri, new ESBCallback<ESBPacket>() {
				@Override
				public void onFailure(Throwable caught) {
					DsUtil.alert("Unable to Find Competency with the URI provided");
					DOM.getElementById("editCompetencySearching").addClassName("hidden");
				}

				@Override
				public void onSuccess(ESBPacket esbPacket) {
					String title = "";
					String description = "";
					
					for (String id : esbPacket.keySet()){
						JSONObject competencyObj = esbPacket.get(id).isObject();
						
						title = competencyObj.get(":competencyTitle").isArray().get(0).isString().stringValue();
						description = competencyObj.get(":competencyDescription").isArray().get(0).isString().stringValue();
					}
					
					DOM.getElementById("editContentAddCompetencyTitle").removeClassName("hidden");
					DOM.getElementById("editCompetencySearching").addClassName("hidden");
					
					InputElement.as(DOM.getElementById("editContentAddCompetencyTitle")).setValue(title);
					InputElement.as(DOM.getElementById("editContentAddCompetencyDescription")).setValue(description);
					
					DOM.getElementById("editContentAddCompetencyApply").removeAttribute("disabled");
					
					PageAssembler.attachHandler("editContentAddCompetencyApply", Event.ONCLICK, alignCompetencyBtnListener);
				}
			});
		}
	};
   
   public static void startAlignCompetency(){
	   	DOM.getElementById("editContentAddCompetencyApply").setAttribute("disabled", "disabled");
	   	DOM.getElementById("editCompetencySearching").addClassName("hidden");
	   	DOM.getElementById("editContentAddCompetencyTitle").addClassName("hidden");
	   	
	   	InputElement.as(DOM.getElementById("editContentAddCompetencyUri")).setValue("");
	   	InputElement.as(DOM.getElementById("editContentAddCompetencyTitle")).setValue("");
		
	   	DsUtil.slideDownElement(DOM.getElementById("editContentAddCompetencyWrapper"), 0.1, new Callback<Object, Object>(){
			@Override
			public void onFailure(Object reason) {}
			
			@Override
			public void onSuccess(Object result) {
				DsUtil.slideUpElement(DOM.getElementById("editContentAddCompetencyWrapper"), 0.1, new Callback<Object, Object>(){
					@Override
					public void onFailure(Object reason) {}

					@Override
					public void onSuccess(Object result) {
						DsUtil.slideDownElement(DOM.getElementById("editContentAddCompetencyWrapper"), null);
					}
				});
			}
	   	});	
	   	
		
		PageAssembler.attachHandler("editContentAddCompetencyApply", Event.ONCLICK, alignCompetencyBtnListener);
   }
   
   public static EventCallback alignCompetencyListener = new EventCallback(){
	   public void onEvent(Event event) {
			if(DOM.getElementById("editContentAddCompetencyWrapper").getStyle().getDisplay().equals("block")){
			}else{
				startAlignCompetency();
			}
		}
   };
   
   public static void createCompetencyItem(final String id, String uri, String title, String description){
	   String item = "<li id='comp-"+id+"'><span id='compDelete-"+id+"' href='#' title='Remove' class='delete' style='cursor:pointer;'></span><a id='compText-" + id +"' class='competency meta-value' title='"+ description +"' data-uri='"+uri+"'  href='"+uri+"' target='_blank'>"+title+"</a></li>";
	   
	   DOM.getElementById("editContentCompetencyList").setInnerHTML(DOM.getElementById("editContentCompetencyList").getInnerHTML() + item);
	   
	   PageAssembler.attachHandler("compDelete-"+id, Event.ONCLICK, new EventCallback(){
			@Override
			public void onEvent(Event event) {
				DsUtil.slideUpElement(DOM.getElementById("comp-"+id), new Callback<Object, Object>() {
					@Override
					public void onFailure(Object reason) {}

					@Override
					public void onSuccess(Object result) {
						DOM.getElementById("comp-"+id).removeFromParent();
						
					}
				});
			}
	   });
   }
   
   public static EventCallback alignCompetencyBtnListener = new EventCallback(){
	   public void onEvent(Event event) {
			if(DOM.getElementById("editContentAddCompetencyWrapper").getStyle().getDisplay().equals("block")){
				String competencyUri = InputElement.as(DOM.getElementById("editContentAddCompetencyUri")).getValue();
		
				if(competencyUri.isEmpty()){
					return;
				}
				
				String title = InputElement.as(DOM.getElementById("editContentAddCompetencyTitle")).getValue();
				
				if(title.isEmpty()){
					return;
				}
				
				String description = InputElement.as(DOM.getElementById("editContentAddCompetencyDescription")).getValue();
				
				String id = (DOM.getElementById("editContentCompetencyList").getChildCount()+1) +"";
				
				createCompetencyItem(id, competencyUri, title, description);
				
				DsUtil.slideUpElement(DOM.getElementById("editContentAddCompetencyWrapper"));
			}
		}
   };
   
   /*
    * Add Collection Objective Stuff
    */
   
   public static boolean addingObjective = false;
   
   public static void startAddObjective(){
	   	PageAssembler.attachHandler(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_ADD_BTN, Event.ONCLICK, addObjectiveBtnListener);
	   	
	   	InputElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_TITLE_INPUT)).setValue("");
		InputElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_DESC_INPUT)).setValue("");
		DsUtil.slideDownElement(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_CONTAINER), null);
		addingObjective = true;
   }
   
   public static EventCallback addObjectiveListener = new EventCallback(){
	   public void onEvent(Event event) {
			if(addingObjective){
			}else{
				startAddObjective();
			}
		}
   };
   
   public static String editingId = "";
   
   public static EventCallback addObjectiveBtnListener = new EventCallback(){
	   public void onEvent(Event event) {
			if(addingObjective){
				String title = InputElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_TITLE_INPUT)).getValue();
				String description = InputElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_DESC_INPUT)).getValue();
				
				if(title.isEmpty()){
					DsUtil.alert("Learning Objective Title Cannot be Empty");
					return;
				}
				if(description.isEmpty()){
					DsUtil.alert("Learning Objective Description Cannot be Empty");
					return;
				}
				
				String id = (DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_LIST).getChildCount()+1) + "temp";
				
				CollectionsViewBuilder.addObjectiveItem(id, title, description);
				DsUtil.hideLabel(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_NONE_ITEM);
				
				InputElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_TITLE_INPUT)).setValue("");
				InputElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_DESC_INPUT)).setValue("");
				DsUtil.slideUpElement(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_CONTAINER));
				addingObjective = false;
			}
		}
   };
   
   public static void cancelAddObjective(){
		DsUtil.slideUpElement(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_OBJECTIVES_CONTAINER));
		addingObjective = false;
   }
   
   public EventCallback cancelAddObjectiveListener = new EventCallback() {
	   public void onEvent(Event event) {
			if(addingObjective){
				cancelAddObjective();
			}else{
			}
		}
   };
   
   public EventCallback environmentChangedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			if(SelectElement.as(DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_ENVIRONMENT)).getValue().isEmpty()){
				DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_ENVIRONMENT).addClassName("notSelected");
			}else{
				DOM.getElementById(CollectionsViewBuilder.METADATA_EDIT_ENVIRONMENT).removeClassName("notSelected");
			}
		}
	};
   
   @Override
   public void lostFocus() {
      DsSession.getInstance().verifyUserCollectionSync();
   }
}