package com.eduworks.decals.ui.client.handler;

import java.util.ArrayList;
import java.util.HashMap;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.model.Collection;
import com.eduworks.decals.ui.client.model.DarResourceCompetency;
import com.eduworks.decals.ui.client.model.DarResourceMetadata;
import com.eduworks.decals.ui.client.model.DarResourceObjective;
import com.eduworks.decals.ui.client.model.DecalsApplicationRepositoryRecord;
import com.eduworks.decals.ui.client.model.LrPublishResponse;
import com.eduworks.decals.ui.client.model.SearchHandlerParamPacket;
import com.eduworks.decals.ui.client.model.DecalsApplicationRepositoryRecord.BasicMimeType;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.pagebuilder.DsHtmlTemplates;
import com.eduworks.decals.ui.client.pagebuilder.screen.DsUserHomeScreen;
import com.eduworks.decals.ui.client.util.CollectionsViewBuilder;
import com.eduworks.decals.ui.client.util.DarConverter;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.component.AppSettings;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.api.FLRPacketGenerator;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Handles actions (edit,metadata,publish,delete,etc.) that happen to the resources returned from the DECALS application repository search. 
 * 
 * @author Eduworks Corporation
 *
 */
public class DarResourceActionHandler {
   
   private static final String BUSY_MESSAGE_IMG = "images/file-upload.gif";
   private static final String BUSY_MESSAGE_CLASS = "alert-box info round";
   private static final String COMPLETE_MESSAGE_CLASS = "alert-box success round";
   private static final String ERROR_MESSAGE_CLASS = "alert-box alert round";
   
   private static final String START_MESSAGE_PREFIX = "darrahsm-";   
   private static final String START_MESSAGE_CLOSE_PREFIX = "darrahsm-close-";
   private static final String COMPLETE_MESSAGE_PREFIX = "darrahcm-";
   private static final String COMPLETE_MESSAGE_CLOSE_PREFIX = "darrahcm-close-";
   private static final String ERROR_MESSAGE_PREFIX = "darrahem-";
   private static final String ERROR_MESSAGE_CLOSE_PREFIX = "darrahem-close-";
   
   private static final String CLICK_TO_EDIT = "Click to edit";
   
   private static final String LR_PUBLISH_FAILED_MESSAGE = "LR Publish call failed to reach host.";
   private static final String LR_DOC_ID_UPDATE_FAILED_MESSAGE = "Could not update resource with LR document ID.";
   
   private static final String NO_PREVIEW_MESSAGE = "No preview available.";
   private static final String NO_PREVIEW_COLOR = "#A4A4A4";
   
   //all of these should be passed through the param packet...this is kind of inconsistent and cumbersome at the moment
   private static final String ARTC_PICKLIST = "addToCollectionPickList";
   private static final String ARTC_INNER_CONTAINER = "addToCollectionListContainer";
   private static final String ARTC_COL_LIST_CONTAINER = "addToCollectionList";
   private static final String ARTC_COL_NAME = "artcColName";
   private static final String ARTC_MODAL = "modalAddToCollection";
   private static final String ARTC_RESOURCE_TITLE = "addToCollectionResourceTitle";
   private static final String ARTC_CANCEL_BTN= "addToCollectionCancelButton";
   private static final String ARTC_CONFIRM = "addToCollectionConfirm";
   private static final String ARTC_FORM = "addToCollectionForm";
   private static final String ARTC_CONFIRM_RESOURCE_TITLE = "addToCollectionConfirmResourceTitle";
   private static final String ARTC_CONFIRM_COL_NAME = "addToCollectionConfirmCollectionName";
   private static final String ARTC_CONFIRM_COL_ID = "addToCollectionCollectionId";
   private static final String ARTC_CONFIRM_RESOURCE_URL = "addToCollectionResourceUrl";   
   private static final String ARTC_CONFIRM_RESOURCE_TITLE_HIDDEN = "addToCollectionResourceTitleHidden";
   private static final String ARTC_CONFIRM_RESOURCE_URL_HIDDEN = "addToCollectionResourceUrlHidden";
   private static final String ARTC_CONFIRM_RESOURCE_DESC = "addToCollectionResourceDescriptionHidden";
   private static final String ARTC_CONFIRM_BTNS = "addToCollectionConfirmButtons";
   private static final String ARTC_EXISTS= "addToCollectionExists";
   private static final String ARTC_REPLACE = "addToCollectionReplace";
   private static final String ARTC_SUCCESS = "addToCollectionSuccess";
   private static final String ARTC_BUSY = "addToCollectionBusy";  
      
   private static final String ARTC_CONFIRM_RESOURCE_ID = "addToCollectionResourceId";
   
   private static final int LIST_ITEMS_PER_PAGE = 5;
   
   private DarSearchHandler searchHandler;
   private SearchHandlerParamPacket paramPacket;
   
   private HashMap<String,Collection> addResourceCollectionWidgets = new HashMap<String,Collection>();
   
   private final native String initAddToCollectionListFiltering(String listContainer, String searchField1, int itemsPerPage) /*-{
      var addToCollectionOptions = {
         valueNames: [searchField1],
            //page:itemsPerPage,
            plugins: [
                //$wnd.ListPagination({outerWindow: 5})
            ]
    }; 
    var addToCollecdtionList = new $wnd.List(listContainer, addToCollectionOptions);      
   }-*/;
   
   public DarResourceActionHandler(DarSearchHandler searchHandler, SearchHandlerParamPacket paramPacket) {
      this.searchHandler = searchHandler;
      this.paramPacket = paramPacket;
      
      PageAssembler.removeHandler(paramPacket.getGenMdContentFormId());
      PageAssembler.attachHandler(paramPacket.getEditContentFormId(),DecalsScreen.VALID_EVENT,editContentSubmitListener);
      PageAssembler.attachHandler(paramPacket.getDeleteContentFormId(),DecalsScreen.VALID_EVENT,deleteContentSubmitListener);
      PageAssembler.attachHandler(paramPacket.getGenMdContentFormId(),DecalsScreen.VALID_EVENT,generateMetadataContentSubmitListener);      
      PageAssembler.attachHandler(paramPacket.getPublishContentFormId(),DecalsScreen.VALID_EVENT,publishContentSubmitListener);
   }
   
   //Does a null value check - if value is null or blank then display 'Click to edit'
   private String checkDisplayValue(String value) {
      if (value == null || value.trim().isEmpty()) return CLICK_TO_EDIT;      
      return value;
   }
   
   //builds the objective entries for the edit modal
   private void buildObjectEntries(DecalsApplicationRepositoryRecord resource) {      
      DsUtil.removeAllChildrenFromElement(paramPacket.getEditContentObjectiveListId());
      if (resource.getObjectives() == null || resource.getObjectives().size() == 0) return;
      StringBuffer sb;
      int i = -1;
      for (DarResourceObjective obj:resource.getObjectives()) {
         sb = new StringBuffer();
         i++;
         sb.append("<a id=\"" + paramPacket.getEditContentObjectiveDeleteId() + i + "\" href=\"#\" title=\"Remove\" class=\"delete\"></a>");
         sb.append("<p id=\"" + paramPacket.getEditContentObjectiveTextId() + i + "\" class=\"objective meta-value editable\" ");
         sb.append("title=\"" + obj.getTitle() + " - " + obj.getDescription() + "\">" + obj.getTitle() + "</p>");
         sb.append("<input id=\"" + paramPacket.getEditContentObjectiveTitleId() + i + "\" style=\"display: none;\" type=\"text\" value=\"" + obj.getTitle() + "\">");
         sb.append("<input id=\"" + paramPacket.getEditContentObjectiveDescId() + i + "\" style=\"display: none;\" type=\"text\" value=\"" + obj.getDescription() + "\">");
         HTMLPanel objectivesHtml = new HTMLPanel("li", sb.toString());
         RootPanel.get(paramPacket.getEditContentObjectiveListId()).add(objectivesHtml);
      }
   }
   
   private void buildCompetencyList(DecalsApplicationRepositoryRecord resource){
	   DsUtil.removeAllChildrenFromElement(paramPacket.getEditContentCompetencyListId());
	   
	   if(resource.getCompetencyList() == null || resource.getCompetencyList().size() == 0)return;
	   
	   int i = 0;
	   for(DarResourceCompetency comp : resource.getCompetencyList()){
		   StringBuffer sb = new StringBuffer();
		   i++;
		   
		   sb.append("<span id=\"compDelete-" + i + "\" title=\"Remove\" class=\"delete\"></span>");
	       sb.append("<a id=\"compText-" + i + "\" class=\"competency meta-value\" href='"+comp.getCompetencyUri()+"' target='_blank'");
	       sb.append("title=\"" + comp.getDescription() + "\" data-uri='"+comp.getCompetencyUri()+"'>" + comp.getTitle() + "</pa>");
	       
	       HTMLPanel objectivesHtml = new HTMLPanel("li", sb.toString());
	       objectivesHtml.getElement().setId("comp-"+i);
	       RootPanel.get(paramPacket.getEditContentCompetencyListId()).add(objectivesHtml);
	       
	       final int x = i;
	       
	       PageAssembler.attachHandler("compDelete-"+i, Event.ONCLICK, new EventCallback(){
				@Override
				public void onEvent(Event event) {
					DsUtil.slideUpElement(DOM.getElementById("comp-"+x), new Callback<Object, Object>() {
						@Override
						public void onFailure(Object reason) {}

						@Override
						public void onSuccess(Object result) {
							DOM.getElementById("comp-"+x).removeFromParent();
						}
					});
				}
		   });
	       
	   }
	   
   }
   
   //populate the edit modal fields from the resource data
   private void populateEditModalInfo(DecalsApplicationRepositoryRecord resource) {
      DsUtil.setLabelText(paramPacket.getEditContentResourceIdId(),resource.getId());
      DsUtil.setLabelText(paramPacket.getEditContentFileNameId(),resource.getFileName());
      DsUtil.setLabelText(paramPacket.getEditContentDescriptionId(),checkDisplayValue(resource.getDescription()));
      DsUtil.setLabelText(paramPacket.getEditContentTitleId(),checkDisplayValue(resource.getTitle()));
      DsUtil.setLabelText(paramPacket.getEditContentMdFileNameId(),resource.getFileName());
      DsUtil.setLabelText(paramPacket.getEditContentKeywordsId(),checkDisplayValue(DsUtil.buildCommaStringFromStringList(resource.getKeywords())));
      buildObjectEntries(resource);
      DsUtil.setLabelText(paramPacket.getEditContentSecClassId(),checkDisplayValue(resource.getClassification()));
      DsUtil.setLabelText(paramPacket.getEditContentSecLevelId(),checkDisplayValue(resource.getSecurityLevel()));
      DsUtil.setLabelText(paramPacket.getEditContentDistributionId(),checkDisplayValue(resource.getDistribution()));
      DsUtil.setLabelText(paramPacket.getEditContentFileFormatId(),resource.getMimeType());
      DsUtil.setLabelText(paramPacket.getEditContentPublisherId(),checkDisplayValue(resource.getPublisher()));
      DsUtil.setLabelText(paramPacket.getEditContentOwnerId(),checkDisplayValue(resource.getOwner()));
      DsUtil.setLabelText(paramPacket.getEditContentCoverageId(),checkDisplayValue(resource.getCoverage()));
      DsUtil.setLabelText(paramPacket.getEditContentInteractivityId(),checkDisplayValue(resource.getInteractivity()));
      DsUtil.setLabelText(paramPacket.getEditContentEnvironmentId(),checkDisplayValue(resource.getEnvironment()));
      DsUtil.setLabelText(paramPacket.getEditContentSkillId(),checkDisplayValue(resource.getSkillLevel()));
      DsUtil.setLabelText(paramPacket.getEditContentLanguageId(),checkDisplayValue(resource.getLanguage()));
      DsUtil.setLabelText(paramPacket.getEditContentFileSizeId(),DsUtil.getNiceFileSizeString(resource.getFileSizeBytes()));
      DsUtil.setLabelText(paramPacket.getEditContentDurationId(),checkDisplayValue(DsUtil.getDurationStringFromSeconds(resource.getDuration())));
      DsUtil.setLabelText(paramPacket.getEditContentTechReqsId(),checkDisplayValue(resource.getTechRequirements()));
      DsUtil.setLabelText(paramPacket.getEditContentPartOfId(),checkDisplayValue(resource.getIsPartOf()));
      DsUtil.setLabelText(paramPacket.getEditContentRequiresId(),checkDisplayValue(resource.getRequires()));
      
      buildCompetencyList(resource);
   }
   
   //Retrieves the object element 'token' from the given objective element id...
   //objective element IDs are formatted like XXXXXXX-yy where yy is the token.
   private String getObjectiveTokenFromId(String objElemId) {
      return objElemId.substring(objElemId.lastIndexOf("-") + 1);
   }
   
   //builds the objective list
   private ArrayList<DarResourceObjective> parseObjectiveList() {
      ArrayList<DarResourceObjective> ret = new ArrayList<DarResourceObjective>();
      NodeList<Node> nl = DOM.getElementById(paramPacket.getEditContentObjectiveListId()).getChildNodes();
      String token;
      String title;
      String desc;
      for (int i=0;i<nl.getLength();i++) {
         token = getObjectiveTokenFromId(((Element)nl.getItem(i).getFirstChild()).getId());
         title = DsUtil.getElementAttributeValue(paramPacket.getEditContentObjectiveTitleId() + token,"value");
         desc  = DsUtil.getElementAttributeValue(paramPacket.getEditContentObjectiveDescId() + token,"value");
         if (title == null || desc == null || title.trim().isEmpty() || desc.trim().isEmpty()) {
            //do nothing
         }
         else {ret.add(new DarResourceObjective(title,desc));}         
      }
      return ret;
   }
   
   private ArrayList<DarResourceCompetency> parseCompetencyList(){
	   ArrayList<DarResourceCompetency> ret = new ArrayList<DarResourceCompetency>();
	   
	   NodeList<Node> nl = DOM.getElementById(paramPacket.getEditContentCompetencyListId()).getChildNodes();
	   
	   String uri;
	   String title;
	   String description;
	   for(int i = 0; i < nl.getLength(); i++){
		   Element e = ((Element)nl.getItem(i).getLastChild());
		   if(e.getTagName().equalsIgnoreCase("a")){
			   uri = e.getAttribute("data-uri");;
			   title = e.getInnerText();
			   description = e.getTitle();
			   
			   if(title != null && uri != null){
				   ret.add(new DarResourceCompetency(uri, title, description));
			   }
		   }
	   }
	   
	   return ret;
   }
   
   private ArrayList<String> parseCompetencyIds(){
	   ArrayList<String> ret = new ArrayList<String>();
	   
	   NodeList<Node> nl = DOM.getElementById(paramPacket.getEditContentCompetencyListId()).getChildNodes();
	   
	   for(int i = 0; i < nl.getLength(); i++){
		   Element e = ((Element)nl.getItem(i).getLastChild());
		   if(e.getTagName().equalsIgnoreCase("a")){
			   ret.add(e.getAttribute("data-uri"));
		   }
	   }
	   
	   return ret;
   }
   
   //Does a 'Click to edit' check.
   private String checkCTE(String value) {
      if (CLICK_TO_EDIT.equalsIgnoreCase(value)) return null;
      return value;
   }
   
   //generate metadata object from field forms
   private DarResourceMetadata generateMetadataFromForm() {
      String resourceId = DsUtil.getLabelText(paramPacket.getEditContentResourceIdId());
      DarResourceMetadata drm = new DarResourceMetadata(resourceId);
      drm.setDescription(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentDescriptionId())));
      drm.setTitle(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentTitleId())));
      drm.setKeywords(DsUtil.buildStringListFromCommaString(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentKeywordsId()))));
      drm.setObjectives(parseObjectiveList());
      drm.setClassification(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentSecClassId())));
      drm.setSecurityLevel(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentSecLevelId())));
      drm.setDistribution(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentDistributionId())));
      drm.setPublisher(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentPublisherId())));
      drm.setOwner(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentOwnerId())));
      drm.setCoverage(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentCoverageId())));
      drm.setInteractivity(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentInteractivityId())));
      drm.setEnvironment(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentEnvironmentId())));
      drm.setSkillLevel(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentSkillId())));
      drm.setLanguage(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentLanguageId())));
      drm.setDuration(DsUtil.getSecondsFromDurationString(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentDurationId()))));
      drm.setTechRequirements(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentTechReqsId())));
      drm.setIsPartOf(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentPartOfId())));
      drm.setRequires(checkCTE(DsUtil.getLabelText(paramPacket.getEditContentRequiresId())));
      
      drm.setCompetencyList(parseCompetencyList());
      drm.setCompetencyIds(parseCompetencyIds());
      
      return drm;
   }
   
   //shows the LR publish error 
   private void showLrPublishError(String errorMessage) {
      DsUtil.setLabelText(paramPacket.getPublishContentFailMessageId(),errorMessage);
      DsUtil.showLabel(paramPacket.getPublishContentFailContainerId());         
   }
   
   // handles the response from the LR publish
   private void handleLrPublishResponse(final LrPublishResponse lrResponse, String resourceId, final long searchSyncId) {
      if (lrResponse.isOk()) {
         DsESBApi.decalsUpdateDarResourceLRPublishData(resourceId,lrResponse.getDocId(),new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {               
               DsUtil.hideLabel(paramPacket.getPublishContentBusyId());
               DsUtil.setLabelText(paramPacket.getPublishContentSuccessLrId(),lrResponse.getDocId());
               DsUtil.showLabel(paramPacket.getPublishContentSuccessContainerId());         
               searchHandler.refreshCurrentSearch(searchSyncId);
            }
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(paramPacket.getPublishContentBusyId());
               showLrPublishError(LR_DOC_ID_UPDATE_FAILED_MESSAGE);
            }
         });        
      }
      else {
         DsUtil.hideLabel(paramPacket.getPublishContentBusyId());
         showLrPublishError(lrResponse.getErrorMessage());
      }
   }
   
   //publish content submit listener
   protected EventCallback publishContentSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.showLabel(paramPacket.getPublishContentBusyId());
         final long searchSyncId = searchHandler.getCurrentSyncId();
         final String resourceId = DsUtil.getLabelText(paramPacket.getPublishContentResourceIdId());
         ESBPacket publishPacket = FLRPacketGenerator.buildFlrNsdlPacket(DarConverter.toFlrRecord(searchHandler.getDarResource(resourceId)), 
               DsSession.getInstance().getLrPublishFromNode(), DsSession.getInstance().getLrPublishSubmitter(), DsSession.getInstance().getLrPublishCurator());
         DsUtil.hideButton(paramPacket.getPublishContentSubmitBtnId());
         DsUtil.hideButton(paramPacket.getPublishContentCancelBtnId());
         DsESBApi.decalsPublishToLR(publishPacket, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {               
               handleLrPublishResponse(new LrPublishResponse(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject()), resourceId, searchSyncId);
            }
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(paramPacket.getPublishContentBusyId());
               showLrPublishError(LR_PUBLISH_FAILED_MESSAGE);
            }
         });      
      }
   };
   
   //edit content submit listener
   protected EventCallback editContentSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.showLabel(paramPacket.getEditContentBusyId());
         final long searchSyncId = searchHandler.getCurrentSyncId();  
         DsESBApi.decalsUpdateDarMetadata(generateMetadataFromForm(), new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               DsUtil.hideLabel(paramPacket.getEditContentBusyId());
               PageAssembler.closePopup(paramPacket.getEditContentModalId());
               searchHandler.refreshCurrentSearch(searchSyncId);
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
         });
      }
   };
   
   //removes the content from the LR if necessary
   //moving this to levr
//   private void removeContentFromLr(String resourceId) {
//      DecalsApplicationRepositoryRecord resource = searchHandler.getDarResource(resourceId);
//      if (!resource.hasBeenPubishedToLr()) return;
//      ESBPacket deletePacket = FLRPacketGenerator.buildFlrDeleteNsdlPacket(DarConverter.toFlrRecord(resource), 
//            DsSession.getInstance().getLrPublishFromNode(), DsSession.getInstance().getLrPublishSubmitter(), DsSession.getInstance().getLrPublishCurator());
//      DsESBApi.decalsPublishToLR(deletePacket, new ESBCallback<ESBPacket>() {
//         @Override
//         public void onSuccess(ESBPacket result) {}
//         @Override
//         public void onFailure(Throwable caught) {}
//      });
//   }
   
   //delete content submit listener
   protected EventCallback deleteContentSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.showLabel(paramPacket.getDeleteContentBusyId());
         final long searchSyncId = searchHandler.getCurrentSyncId();
         final String resourceId = DsUtil.getLabelText(paramPacket.getDeleteContentResourceIdId());
         DecalsApplicationRepositoryRecord di = searchHandler.getDarResource(resourceId);
         final String resourceUrl = di.getUrl();
         final boolean isFileType = di.isFileType();
         DsESBApi.decalsDeleteDarResource(resourceId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               //removeContentFromLr(resourceId);
               DsUtil.hideLabel(paramPacket.getDeleteContentBusyId());
               PageAssembler.closePopup(paramPacket.getDeleteContentConfirmModalId());
               searchHandler.refreshCurrentSearch(searchSyncId);
               if (isFileType) DsSession.removeItemFromAllCollections(resourceUrl);
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
         });
      }
   };
    
   //Adds a generate metadata start message to the message container
   private void addGenerateMetadataStartMessage(String messageId, String fileName) {
      String messageText = "Generating: <i>" + DsUtil.getMessageFileName(fileName) + "</i>...";
      DsUtil.generateMessage(paramPacket.getContributionMessageContainerId(),messageId, messageText, BUSY_MESSAGE_CLASS, START_MESSAGE_CLOSE_PREFIX, BUSY_MESSAGE_IMG);      
   }
   
   //Adds a generate metadata complete message to the message container
   private void addGenerateMetadataCompleteMessage(String messageId, String fileName) {
      String messageText = "Complete: <i>" + DsUtil.getMessageFileName(fileName) + "</i>...";
      DsUtil.generateMessage(paramPacket.getContributionMessageContainerId(),messageId, messageText, COMPLETE_MESSAGE_CLASS, COMPLETE_MESSAGE_CLOSE_PREFIX, null);      
   }
   
   //Adds a generate metadata error message to the message container
   private void addGenerateMetadataErrorMessage(String messageId, String fileName) {
      String messageText = "Error generating metadata: <i>" + DsUtil.getMessageFileName(fileName) + "</i>...";
      DsUtil.generateMessage(paramPacket.getContributionMessageContainerId(),messageId, messageText, ERROR_MESSAGE_CLASS, ERROR_MESSAGE_CLOSE_PREFIX, null);      
   }
   
   //generate content metadata submit listener
   protected EventCallback generateMetadataContentSubmitListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         String resourceId = DsUtil.getLabelText(paramPacket.getGenMdContentResourceIdId());  
         final DecalsApplicationRepositoryRecord resource = searchHandler.getDarResource(resourceId);
         final long searchSyncId = searchHandler.getCurrentSyncId();
         final String startMessageId = DsUtil.generateId(START_MESSAGE_PREFIX);
         addGenerateMetadataStartMessage(startMessageId, resource.getFileName());
         PageAssembler.closePopup(paramPacket.getGenMdContentConfirmModalId());
         DsESBApi.decalsGenerateDarResourceMetadata(resourceId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               DsUtil.hideLabel(startMessageId);
               addGenerateMetadataCompleteMessage(DsUtil.generateId(COMPLETE_MESSAGE_PREFIX),resource.getFileName());
               searchHandler.refreshCurrentSearch(searchSyncId);
            }
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(startMessageId);
               addGenerateMetadataErrorMessage(DsUtil.generateId(ERROR_MESSAGE_PREFIX),resource.getFileName());
            }
         });
      }
   };
   
   //add the no preview available statement
   private void addNoPreviewAvailableHtml() {
      RootPanel.get(paramPacket.getEditContentPreviewContainerId()).add(new HTML("<p><i style=\"color:" + NO_PREVIEW_COLOR + "\">" + NO_PREVIEW_MESSAGE +"</i></p>"));
   }
   
   //generates an iframe for a url resource
   private void generateUrlIframe(DecalsApplicationRepositoryRecord resource) {
      StringBuffer sb = new StringBuffer();
      sb.append("<iframe src=\"" + resource.getUrl() + "\" style=\"width:100%;\">");
      sb.append("<p>Your browser does not support iframes.</p>");
      sb.append("</iframe>");
      RootPanel.get(paramPacket.getEditContentPreviewContainerId()).add(new HTML(sb.toString()));
   }
   
   //populate with basic text data 
   private void generateTextData(DecalsApplicationRepositoryRecord resource) {
      DsESBApi.decalsGetDarResource(resource.getId(), new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket esbPacket) {
            DOM.getElementById(paramPacket.getEditContentPreviewContainerId()).setInnerHTML(esbPacket.getContentString());            
         }
         @Override
         public void onFailure(Throwable caught) {addNoPreviewAvailableHtml();}
      });     
   }   
   
   //generate a video control
   private void generateVideoControl(DecalsApplicationRepositoryRecord resource) {
      String htmlString = "<video controls=\"controls\"><source src=\"" + resource.getUrl() + "\" type=\"" + resource.getMimeType() + "\"></source></video>";         
      RootPanel.get(paramPacket.getEditContentPreviewContainerId()).getElement().setInnerHTML(htmlString);
   }
   
   //generate an audio control
   private void generateAudioControl(DecalsApplicationRepositoryRecord resource) {
      String htmlString = "<audio controls=\"controls\"><source src=\"" + resource.getUrl() + "\" type=\"" + resource.getMimeType() + "\"></source></audio>";         
      RootPanel.get(paramPacket.getEditContentPreviewContainerId()).getElement().setInnerHTML(htmlString);
   }
   
   //populate the content preview
   private void populateContentPreview(DecalsApplicationRepositoryRecord resource) {
      DsUtil.removeAllChildrenFromElement(paramPacket.getEditContentPreviewContainerId());
      if (resource.getBasicMimeType().equals(BasicMimeType.IMAGE)) {
         RootPanel.get(paramPacket.getEditContentPreviewContainerId()).add(new Image(resource.getUrl()));
      }
      else if (resource.isUrlType()) generateUrlIframe(resource);
      else if (resource.getBasicMimeType().equals(BasicMimeType.TEXTPLAIN) || resource.getBasicMimeType().equals(BasicMimeType.RTF)) generateTextData(resource);
      else if (resource.getBasicMimeType().equals(BasicMimeType.VIDEO)) generateVideoControl(resource);
      else if (resource.getBasicMimeType().equals(BasicMimeType.AUDIO)) generateAudioControl(resource);
      else addNoPreviewAvailableHtml();
   }
   
   //handle edit
   private void handleEditResource(String widgetId, String resourceId) { 
      DecalsApplicationRepositoryRecord resource = searchHandler.getDarResource(resourceId);
      populateEditModalInfo(resource);
      populateContentPreview(resource);
      PageAssembler.openPopup(paramPacket.getEditContentModalId());        
   }
   
   //handle delete
   private void handleDeleteResource(String widgetId, String resourceId) {
      DecalsApplicationRepositoryRecord resource = searchHandler.getDarResource(resourceId);
      DsUtil.setLabelText(paramPacket.getDeleteContentResourceIdId(),resourceId);
      DsUtil.setLabelText(paramPacket.getDeleteContentFileNameId(),resource.getTitle());
      PageAssembler.openPopup(paramPacket.getDeleteContentConfirmModalId());
   }
   
   //handle generate metadata
   private void handleGenerateMetadataForResource(String widgetId, String resourceId) {
      DecalsApplicationRepositoryRecord resource = searchHandler.getDarResource(resourceId);
      DsUtil.setLabelText(paramPacket.getGenMdContentResourceIdId(),resourceId);
      DsUtil.setLabelText(paramPacket.getGenMdContentFileNameId(),resource.getTitle());
      PageAssembler.openPopup(paramPacket.getGenMdContentConfirmModalId());
   }
   
   //handle publish
   private void handlePublishResource(String widgetId, String resourceId) {  
      DecalsApplicationRepositoryRecord resource = searchHandler.getDarResource(resourceId);
      DsUtil.setLabelText(paramPacket.getPublishContentResourceIdId(),resourceId);
      DsUtil.setLabelText(paramPacket.getPublishContentFileNameId(),resource.getTitle());
      DsUtil.setLabelText(paramPacket.getPublishContentLrNodeId(),DsSession.getInstance().getLrPublishNode());
      DsUtil.setButtonStyle(paramPacket.getPublishContentSubmitBtnId(),"display:inline");
      DsUtil.setButtonStyle(paramPacket.getPublishContentCancelBtnId(),"display:inline");      
      DsUtil.hideLabel(paramPacket.getPublishContentSuccessContainerId());
      DsUtil.hideLabel(paramPacket.getPublishContentFailContainerId());
      PageAssembler.openPopup(paramPacket.getPublishContentConfirmModalId());
   }
   
   //edit click event listener
   private class EditClickListener extends EventCallback {      
      private String widgetId;    
      private String resourceId;
      public EditClickListener(String widgetId, String resourceId) {
         this.widgetId = widgetId;
         this.resourceId = resourceId;
      }      
      @Override
      public void onEvent(Event event) {handleEditResource(widgetId,resourceId);}
   }
   
   //publish click event listener
   private class PublishClickListener extends EventCallback {      
      private String widgetId;      
      private String resourceId;
      public PublishClickListener(String widgetId, String resourceId) {
         this.widgetId = widgetId;
         this.resourceId = resourceId;
      }      
      @Override
      public void onEvent(Event event) {handlePublishResource(widgetId,resourceId);}
   }
   
   //delete click event listener
   private class DeleteClickListener extends EventCallback {      
      private String widgetId;
      private String resourceId;
      public DeleteClickListener(String widgetId, String resourceId) {
         this.widgetId = widgetId;
         this.resourceId = resourceId;
      }      
      @Override
      public void onEvent(Event event) {handleDeleteResource(widgetId,resourceId);}
   }
   
   //generate metadata click event listener
   private class GenerateMetadataClickListener extends EventCallback {      
      private String widgetId;      
      private String resourceId;
      public GenerateMetadataClickListener(String widgetId, String resourceId) {
         this.widgetId = widgetId;
         this.resourceId = resourceId;
      }       
      @Override
      public void onEvent(Event event) {handleGenerateMetadataForResource(widgetId,resourceId);}
   }  
   
   /**
    * Add an edit click handler to the widget with the given ID for the resource with the given ID.
    * 
    * @param widgetId The widget ID
    * @param resourceId The resource ID
    */
   public void addEditClickHandler(String widgetId, String resourceId) {
      if (widgetId != null && resourceId != null) PageAssembler.attachHandler(widgetId,Event.ONCLICK, new EditClickListener(widgetId,resourceId));
   }
   
   /**
    * Add a publish click handler to the widget with the given ID for the resource with the given ID.
    * 
    * @param widgetId The widget ID
    * @param resourceId The resource ID
    */
   public void addPublishClickHandler(String widgetId, String resourceId) {
      if (widgetId != null && resourceId != null) PageAssembler.attachHandler(widgetId,Event.ONCLICK, new PublishClickListener(widgetId,resourceId));
   }
   
   /**
    * Add a delete click handler to the widget with the given ID for the resource with the given ID.
    * 
    * @param widgetId The widget ID
    * @param resourceId The resource ID
    */
   public void addDeleteClickHandler(String widgetId, String resourceId) {
      if (widgetId != null && resourceId != null) PageAssembler.attachHandler(widgetId,Event.ONCLICK, new DeleteClickListener(widgetId,resourceId));
   }
   
   /**
    * Add a generate metadata click handler to the widget with the given ID for the resource with the given ID.
    * 
    * @param widgetId The widget ID
    * @param resourceId The resource ID
    */   
   public void addGenerateMetadataClickHandler(String widgetId, String resourceId) {
      if (widgetId != null && resourceId != null) PageAssembler.attachHandler(widgetId,Event.ONCLICK, new GenerateMetadataClickListener(widgetId,resourceId));
   }
   
   //handle add to collection response 
   private void handleAddToCollectionResponse(JSONObject colRes) {
      Collection col = new Collection(colRes);
      DsSession.getUserCollectionManager().replaceCollection(col);
      DsUtil.hideLabel(ARTC_BUSY);
      DsUtil.showLabel(ARTC_SUCCESS);
      
      DsUserHomeScreen.getDarSearchHandler().performDarSearchByUserDate(DsSession.getUser().getUserId(), ((DsHtmlTemplates) AppSettings.templates).getDarSearchResultWidget().getText(), DsUserHomeScreen.generateDarSearchParamPacket());
   }
   
   //handle add to collection submit
   protected EventCallback addToCollectionSubmitHandler = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.hideLabel(ARTC_CONFIRM_BTNS);
         DsUtil.showLabel(ARTC_BUSY);
         String collectionId = DsUtil.getLabelText(ARTC_CONFIRM_COL_ID);
         String resourceTitle = DsUtil.getLabelText(ARTC_CONFIRM_RESOURCE_TITLE_HIDDEN);
         String resourceUrl = DsUtil.getLabelText(ARTC_CONFIRM_RESOURCE_URL_HIDDEN);
         String resourceDescription = DsUtil.getLabelText(ARTC_CONFIRM_RESOURCE_DESC);
         String resourceId = DsUtil.getLabelText(ARTC_CONFIRM_RESOURCE_ID);
         
         DsESBApi.decalsAddCollectionItem(collectionId,resourceId,resourceUrl,resourceTitle,resourceDescription, null, "0",new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {       
               handleAddToCollectionResponse(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());            
            }
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(ARTC_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });         
      }
   };
   
   //handle add resource collection chosen
   private void handleAddResourceCollectionChosen(Collection col, DecalsApplicationRepositoryRecord sr) {
      DsUtil.setLabelText(ARTC_CONFIRM_RESOURCE_TITLE,sr.getTitle());
      DsUtil.setLabelText(ARTC_CONFIRM_COL_NAME,col.getName());
      DsUtil.setLabelText(ARTC_CONFIRM_RESOURCE_URL,sr.getUrl());
      DsUtil.setLabelText(ARTC_CONFIRM_COL_ID,col.getCollectionId());
      DsUtil.setLabelText(ARTC_CONFIRM_RESOURCE_TITLE_HIDDEN,sr.getTitle());
      DsUtil.setLabelText(ARTC_CONFIRM_RESOURCE_URL_HIDDEN,sr.getUrl());
      DsUtil.setLabelText(ARTC_CONFIRM_RESOURCE_ID, sr.getId());
      if (sr.getDescription() == null || sr.getDescription().trim().isEmpty()) DsUtil.setLabelText(ARTC_CONFIRM_RESOURCE_DESC,sr.getTitle());         
      else DsUtil.setLabelText(ARTC_CONFIRM_RESOURCE_DESC,sr.getDescription());
      DsUtil.hideLabel(ARTC_PICKLIST);      
      DsUtil.hideLabel(ARTC_BUSY);
      DsUtil.showLabel(ARTC_CONFIRM); 
      DsUtil.showLabelInline(ARTC_CONFIRM_BTNS);     
      PageAssembler.attachHandler(ARTC_FORM,DecalsScreen.VALID_EVENT,addToCollectionSubmitHandler);     
   }
   
   //add resource collection click event listener
   private class AddResourceCollectionClickListener extends EventCallback {      
      private Collection col;    
      private DecalsApplicationRepositoryRecord sr;
      public AddResourceCollectionClickListener(Collection col, DecalsApplicationRepositoryRecord sr) {
         this.col = col;
         this.sr = sr;
      }      
      @Override
      public void onEvent(Event event) {handleAddResourceCollectionChosen(col,sr);}
   }
   
   //register add resource collection widget even handlers
   private void registerAddResourceCollectionWidgets(DecalsApplicationRepositoryRecord sr) {
      for (String key:addResourceCollectionWidgets.keySet()) {
         PageAssembler.attachHandler(key,Event.ONCLICK,new AddResourceCollectionClickListener(addResourceCollectionWidgets.get(key),sr));
      }
   }
   
   //handle add to collection
   private void handleAddToCollection(String widgetId, DecalsApplicationRepositoryRecord sr) {
      DsUtil.hideLabel(ARTC_CONFIRM);
      DsUtil.hideLabel(ARTC_EXISTS);
      DsUtil.hideLabel(ARTC_SUCCESS);
      DsUtil.showLabel(ARTC_PICKLIST);         
      DsUtil.showLabel(ARTC_CANCEL_BTN);
      DsUtil.setLabelText(ARTC_RESOURCE_TITLE,sr.getTitle());
      addResourceCollectionWidgets.clear();
      CollectionsViewBuilder.buildAddResourceCollectionList(ARTC_INNER_CONTAINER,DsSession.getUserModifiableCollections(),addResourceCollectionWidgets);
      registerAddResourceCollectionWidgets(sr);
      initAddToCollectionListFiltering(ARTC_COL_LIST_CONTAINER,ARTC_COL_NAME,LIST_ITEMS_PER_PAGE);
      PageAssembler.openPopup(ARTC_MODAL);
   }
   
   //add to collection click event listener
   private class AddToCollectionClickListener extends EventCallback {      
      private String widgetId;    
      private DecalsApplicationRepositoryRecord sr;
      
      public AddToCollectionClickListener(String widgetId, DecalsApplicationRepositoryRecord sr) {
         this.widgetId = widgetId;
         this.sr = sr;         
      }      
      @Override
      public void onEvent(Event event) {handleAddToCollection(widgetId,sr);}
   }
   
   /**
    * Adds a add to collection click listener for the given widget ID
    * 
    * @param widgetId The widget ID 
    * @param sr The search result
    */
   public void addAddToCollectionClickListener(String widgetId, DecalsApplicationRepositoryRecord sr) {
      if (widgetId != null && sr != null) PageAssembler.attachHandler(widgetId,Event.ONCLICK, new AddToCollectionClickListener(widgetId,sr));
   }
   
}
