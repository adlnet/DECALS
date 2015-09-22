package com.eduworks.decals.ui.client.pagebuilder.screen;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.handler.DsHeaderHandler;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.CommentHandlerType;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.RatingHandlerType;
import com.eduworks.decals.ui.client.handler.LearnerFocusedSearchHandler;
import com.eduworks.decals.ui.client.model.SearchHandlerParamPacket;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * 
 * Learning registry/interactive search for application users.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsUserLrRSearchScreen  extends DecalsScreen {
   
   private static final String ULRS_FIELD = "userLRSearchInput";
   private static final String ULRS_HEADER_FIELD = "userLRSearchHeaderInput";
   private static final String ULRS_DIV = "userLRSearch";
   private static final String ULRS_SEARCH_ICON = "searchIcon";
   
   private static final String ULRS_RESULTS = "userLRSearchResults";
   private static final String ULRS_COUNTER_CONTAINER = "numberOfUserLRSearchResults";
   private static final String ULRS_COUNTER = "userLRSearchResultsCounter";
   private static final String ULRS_BUSY = "userLRSearchBusy";
   private static final String ULRS_FILTER_TOOLS = "userLRSearchResultsFilterTools";
   private static final String ULRS_RESULTS_SCREEN = "userLRSearchResultsScreen";
   private static final String ULRS_BREADCRUMB_CONT = "userLRSearchBreadCrumbs";  
   private static final String ULRS_WORD_DEF_CONTAINER = "userLRSearchResultsWordDefinitionContainer";   
   private static final String ULRS_FILTER_NAVIGATION = "userLRSrchFilterNavigation";   
   private static final String ULRS_FILTER_NAV_QUESTION = "userLRSrchFilterNavQuestionLabel"; 
   private static final String ULRS_FILTER_NAV_ANSWER_1_LI = "userLRSrchFilterNavAnswer1Li";
   private static final String ULRS_FILTER_NAV_ANSWER_2_LI = "userLRSrchFilterNavAnswer2Li";
   private static final String ULRS_FILTER_NAV_ANSWER_3_LI = "userLRSrchFilterNavAnswer3Li";
   private static final String ULRS_FILTER_NAV_LINKS = "userLRSrchFilterNavLinks";  
   private static final String ULRS_FILTER_NAV_MORE_ANSWERS = "userLRSrchFilterNavMoreAnswers"; 
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS = "userLRSrchFilterNavMoreOptions";  
   private static final String ULRS_FILTER_NAV_GL_KDG_LI = "userLRSrchFilterNavAnswerGlKgLi";
   private static final String ULRS_FILTER_NAV_GL_ES_LI = "userLRSrchFilterNavAnswerGlEsLi";
   private static final String ULRS_FILTER_NAV_GL_MS_LI = "userLRSrchFilterNavAnswerGlMsLi";
   private static final String ULRS_FILTER_NAV_GL_HS_LI = "userLRSrchFilterNavAnswerGlHsLi";
   private static final String ULRS_FILTER_NAV_GL_CU_LI = "userLRSrchFilterNavAnswerGlCuLi";
   private static final String ULRS_FILTER_NAV_GL_VTP_LI = "userLRSrchFilterNavAnswerGlVtpLi";
   private static final String ULRS_FILTER_NAV_GL_APPLY_LI = "userLRSrchFilterNavAnswerApplyGlLi";
   private static final String ULRS_FILTER_NAV_GL_APPLY = "userLRSrchFilterNavAnswerApplyGl";   
   private static final String ULRS_FILTER_NAV_GL_TOGGLE_LI = "userLRSrchFilterNavAnswerToggleAllLi";
   private static final String ULRS_FILTER_NAV_GL_TOGGLE = "userLRSrchFilterNavAnswerToggleAll";   
   private static final String ULRS_FILTER_NAV_GL_KDG_CB = "userLRSrchGlKgCb";
   private static final String ULRS_FILTER_NAV_GL_ES_CB = "userLRSrchGlEsCb";
   private static final String ULRS_FILTER_NAV_GL_MS_CB = "userLRSrchGlMsCb";
   private static final String ULRS_FILTER_NAV_GL_HS_CB = "userLRSrchGlHsCb";
   private static final String ULRS_FILTER_NAV_GL_CU_CB = "userLRSrchGlCuCb";
   private static final String ULRS_FILTER_NAV_GL_VTP_CB = "userLRSrchGlVtpCb";   
   private static final String ULRS_APPLIED_GL_CONTAINER = "userLRSearchAppliedGradeLevelsContainer";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_LINK_TXT = "userLRSrchFilterNavMoreOptionsTxt";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_CONTAINER = "userLRSrchFilterNavigationMopContainer";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_DISAMBIG_LI = "userLRSrchFilterNavigationMopDisambigLi";   
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_CAT_LI = "userLRSrchFilterNavigationMopRelCatLi";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_TOPIC_LI = "userLRSrchFilterNavigationMopRelTopicLi";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_GL_LI = "userLRSrchFilterNavigationMopGradeLevLi";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_DISAMBIG = "userLRSrchFilterNavigationMopDisambig";   
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_CAT = "userLRSrchFilterNavigationMopRelCat";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_TOPIC = "userLRSrchFilterNavigationMopRelTopic";
   private static final String ULRS_FILTER_NAV_MORE_OPTIONS_GL = "userLRSrchFilterNavigationMopGradeLev";
   
   private static final String COMMENTS_MODAL = "modalResourceComments";
   private static final String COMMENTS_TITLE = "resourceCommentsTitle";
   private static final String COMMENTS_FORM = "resourceCommentsForm";
   private static final String COMMENTS_RESOURCE_URL = "resourceCommentsUrl";
   private static final String COMMENTS_CONTAINER = "resourceCommentsContainer";
   private static final String COMMENTS_INPUT = "resourceCommentsInput";
   private static final String COMMENTS_BUSY = "resourceCommentsBusy";
   
   private static final String DEFAULT_HEADER = "defaultHeader";
   private static final String SEARCH_HEADER = "searchHeader";
   
   private static final String ULRS_TYPING = "userLRSearchTyping";
   
   public LearnerFocusedSearchHandler userLRSearchHandler = new LearnerFocusedSearchHandler();
   
   //Generates a SearchHandlerParamPacket with the needed element IDs for an interactive search...so many :(
   private SearchHandlerParamPacket generateSearchParamPacket() {
      SearchHandlerParamPacket packet = new SearchHandlerParamPacket();
      packet.setResultsContainerId(ULRS_RESULTS);
      packet.setCounterElementId(ULRS_COUNTER);
      packet.setCounterContainerElementId(ULRS_COUNTER_CONTAINER);
      packet.setSearchBusyElementId(ULRS_BUSY);
      packet.setBreadCrumbElementId(ULRS_BREADCRUMB_CONT);
      packet.setWordDefContainerId(ULRS_WORD_DEF_CONTAINER);
      packet.setFilterNavContaierId(ULRS_FILTER_NAVIGATION);
      packet.setFilterNavQuestionId(ULRS_FILTER_NAV_QUESTION);
      packet.setFilterNavAnswer1LiId(ULRS_FILTER_NAV_ANSWER_1_LI);
      packet.setFilterNavAnswer2LiId(ULRS_FILTER_NAV_ANSWER_2_LI);
      packet.setFilterNavAnswer3LiId(ULRS_FILTER_NAV_ANSWER_3_LI);
      packet.setFilterNavLinksId(ULRS_FILTER_NAV_LINKS);
      packet.setFilterNavMoreAnswersLinkId(ULRS_FILTER_NAV_MORE_ANSWERS);
      packet.setFilterNavMoreOptionsLinkId(ULRS_FILTER_NAV_MORE_OPTIONS);
      packet.setFilterNavGrdLvlKgLiId(ULRS_FILTER_NAV_GL_KDG_LI);
      packet.setFilterNavGrdLvlEsLiId(ULRS_FILTER_NAV_GL_ES_LI);
      packet.setFilterNavGrdLvlMsLiId(ULRS_FILTER_NAV_GL_MS_LI);
      packet.setFilterNavGrdLvlHsLiId(ULRS_FILTER_NAV_GL_HS_LI);
      packet.setFilterNavGrdLvlCuLiId(ULRS_FILTER_NAV_GL_CU_LI);
      packet.setFilterNavGrdLvlVtpLiId(ULRS_FILTER_NAV_GL_VTP_LI);
      packet.setFilterNavGrdLvlApplyLiId(ULRS_FILTER_NAV_GL_APPLY_LI);
      packet.setFilterNavGrdLvlApplyLinkId(ULRS_FILTER_NAV_GL_APPLY);
      packet.setFilterNavGrdLvlKgCbId(ULRS_FILTER_NAV_GL_KDG_CB);
      packet.setFilterNavGrdLvlEsCbId(ULRS_FILTER_NAV_GL_ES_CB);
      packet.setFilterNavGrdLvlMsCbId(ULRS_FILTER_NAV_GL_MS_CB);
      packet.setFilterNavGrdLvlHsCbId(ULRS_FILTER_NAV_GL_HS_CB);
      packet.setFilterNavGrdLvlCuCbId(ULRS_FILTER_NAV_GL_CU_CB);
      packet.setFilterNavGrdLvlVtpCbId(ULRS_FILTER_NAV_GL_VTP_CB);
      packet.setAppliedGradeLevelsContainerId(ULRS_APPLIED_GL_CONTAINER);
      packet.setFilterNavMoreOptContainerId(ULRS_FILTER_NAV_MORE_OPTIONS_CONTAINER);
      packet.setFilterNavMoreOptDisambigLiId(ULRS_FILTER_NAV_MORE_OPTIONS_DISAMBIG_LI);
      packet.setFilterNavMoreOptCatLiId(ULRS_FILTER_NAV_MORE_OPTIONS_CAT_LI);
      packet.setFilterNavMoreOptTopicLiId(ULRS_FILTER_NAV_MORE_OPTIONS_TOPIC_LI);
      packet.setFilterNavMoreOptGrdLvlLiId(ULRS_FILTER_NAV_MORE_OPTIONS_GL_LI);
      packet.setFilterNavMoreOptDisambigId(ULRS_FILTER_NAV_MORE_OPTIONS_DISAMBIG);
      packet.setFilterNavMoreOptCatId(ULRS_FILTER_NAV_MORE_OPTIONS_CAT);
      packet.setFilterNavMoreOptTopicId(ULRS_FILTER_NAV_MORE_OPTIONS_TOPIC);
      packet.setFilterNavMoreOptGrdLvlId(ULRS_FILTER_NAV_MORE_OPTIONS_GL);
      packet.setFilterNavMoreOptLinkTxtId(ULRS_FILTER_NAV_MORE_OPTIONS_LINK_TXT);
      packet.setSearchTermBoxId(ULRS_HEADER_FIELD);
      packet.setFilterNavGrdLvlToggleLiId(ULRS_FILTER_NAV_GL_TOGGLE_LI);
      packet.setFilterNavGrdLvlToggleLinkId(ULRS_FILTER_NAV_GL_TOGGLE);  
      packet.setCommentModalId(COMMENTS_MODAL);
      packet.setCommentTitleId(COMMENTS_TITLE);
      packet.setCommentFormId(COMMENTS_FORM);
      packet.setCommentResourceUrlId(COMMENTS_RESOURCE_URL);
      packet.setCommentContainerId(COMMENTS_CONTAINER);
      packet.setCommentInputId(COMMENTS_INPUT);
      packet.setCommentBusyId(COMMENTS_BUSY);
      return packet;
   }
   
 //Resets the interactive search fields
   private void resetInteractiveSearch() {      
      userLRSearchHandler.clearSearch();
      DsUtil.hideLabel(ULRS_FILTER_TOOLS);
      DsUtil.hideLabel(ULRS_FILTER_NAVIGATION);      
      DsUtil.hideLabel(ULRS_RESULTS_SCREEN);
      DsUtil.hideLabel(ULRS_TYPING);
      DsUtil.hideLabel(SEARCH_HEADER);
      DsUtil.showLabel(DEFAULT_HEADER);
      DsUtil.showLabel(ULRS_DIV);
      DsUtil.setFocus(ULRS_FIELD);
      DsUtil.setTextBoxText(ULRS_FIELD,"");
      DsUtil.setTextBoxText(ULRS_HEADER_FIELD,"");
      DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(ULRS_RESULTS));
   }
   
   //Interactive search listener for middle page search box
   protected EventCallback userLRSearchListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         String value = DsUtil.getTextBoxText(ULRS_FIELD);
         DsUtil.setTextBoxText(ULRS_HEADER_FIELD, value);
         DsUtil.hideLabel(DEFAULT_HEADER);
         DsUtil.showLabel(SEARCH_HEADER);
         DsUtil.setFocus(ULRS_HEADER_FIELD);
         DsUtil.hideLabel(ULRS_DIV);
         DsUtil.showLabel(ULRS_TYPING);
         DsUtil.setTextBoxText(ULRS_FIELD,"");
      }
   };
   
   //Sets up and calls interactive search handler
   private void performLRSearch(String searchTerm) {
      DsUtil.hideLabel(ULRS_TYPING);
      DsUtil.showLabel(ULRS_FILTER_TOOLS);
      DsUtil.showLabel(ULRS_FILTER_NAVIGATION);     
      DsUtil.showLabel(ULRS_RESULTS_SCREEN);
      DsUtil.showLabel(ULRS_RESULTS);
      userLRSearchHandler.performInteractiveSearch(searchTerm, getTemplates().getUserLRSearchResultWidget().getText(),
            generateSearchParamPacket(),RatingHandlerType.MODIFY,CommentHandlerType.MODIFY,true);      
   }
   
   //Interactive search listener for header bar
   protected EventCallback userLRSearchHeaderListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
    	  if(event.getTypeInt() == Event.ONKEYDOWN){
    		  if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
	            String searchTerm = DsUtil.getTextBoxText(ULRS_HEADER_FIELD).trim();            
	            if (searchTerm == null || searchTerm.isEmpty()) resetInteractiveSearch();
	            else performLRSearch(searchTerm);
    		  }
    	  }else if(event.getTypeInt() == Event.ONCLICK){
    		  String searchTerm = DsUtil.getTextBoxText(ULRS_HEADER_FIELD).trim();            
    		  if (searchTerm == null || searchTerm.isEmpty()) resetInteractiveSearch();
	          else performLRSearch(searchTerm);
    	  }
    	  
         
      }
   };
   
   //checks the session for a cached search and attempts to rebuild it if one is found
   private void checkForCachedSearch() {
      if (DsSession.getInstance().getCachedLrSearchHandler() == null) return;
      userLRSearchHandler = (LearnerFocusedSearchHandler) DsSession.getInstance().getCachedLrSearchHandler();
      DsUtil.setTextBoxText(ULRS_HEADER_FIELD, userLRSearchHandler.getLastSearchTerm());
      DsUtil.hideLabel(DEFAULT_HEADER);
      DsUtil.showLabel(SEARCH_HEADER);
      DsUtil.setFocus(ULRS_HEADER_FIELD);
      DsUtil.hideLabel(ULRS_DIV);      
      DsUtil.setTextBoxText(ULRS_FIELD,"");
      DsUtil.hideLabel(ULRS_TYPING);
      DsUtil.showLabel(ULRS_FILTER_TOOLS);
      DsUtil.showLabel(ULRS_FILTER_NAVIGATION);     
      DsUtil.showLabel(ULRS_RESULTS_SCREEN);
      DsUtil.showLabel(ULRS_RESULTS);
      userLRSearchHandler.rebuildFromLastSearchHistory();
   }
   
   @Override
   public void display() {
	  validateSession();
      PageAssembler.ready(new HTML(getTemplates().getUserLRSearchPanel().getText()));
      PageAssembler.buildContents();
      DsHeaderHandler dhh = new DsHeaderHandler(getDispatcher());
      dhh.setUpHeader(DsSession.getUser().getFirstName(), DsSession.getUser().getEmailAddress());
      PageAssembler.attachHandler(ULRS_FIELD,Event.ONKEYDOWN,userLRSearchListener);
      PageAssembler.attachHandler(ULRS_HEADER_FIELD,Event.ONKEYDOWN,userLRSearchHeaderListener);
      PageAssembler.attachHandler(ULRS_SEARCH_ICON,Event.ONCLICK,userLRSearchHeaderListener);
      DsUtil.setFocus(ULRS_FIELD);  
      checkForCachedSearch();
   }

   @Override
   public void lostFocus() {
      if (userLRSearchHandler.getSearchHistory().size() > 0) DsSession.getInstance().setCachedLrSearchHandler(userLRSearchHandler);
      DsUtil.setTextBoxText(ULRS_HEADER_FIELD, "");
      DsUtil.hideLabel(SEARCH_HEADER);
      DsUtil.showLabel(DEFAULT_HEADER);
   }

}
