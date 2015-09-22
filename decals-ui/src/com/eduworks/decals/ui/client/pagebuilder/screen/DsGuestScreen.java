package com.eduworks.decals.ui.client.pagebuilder.screen;

import com.google.gwt.event.dom.client.KeyCodes;
import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.handler.BasicSearchHandler;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.CommentHandlerType;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.RatingHandlerType;
import com.eduworks.decals.ui.client.model.AppUser;
import com.eduworks.decals.ui.client.model.SearchHandlerParamPacket;
import com.eduworks.decals.ui.client.pagebuilder.DecalsScreen;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Screen handling for the main DECALS guest screen.
 * 
 * @author Eduworks Corporation
 *
 */
public class DsGuestScreen extends DecalsScreen {
   
   private enum Tabs{INTERACTIVE_TAB, BASIC_TAB}
   
   private Tabs currentTab = Tabs.INTERACTIVE_TAB;
   private String userId = "";
   private String password = "";
   private BasicSearchHandler basicSearchHandler = new BasicSearchHandler();
   private InteractiveSearchHandler interactiveSearchHandler = new InteractiveSearchHandler();
   
   private static final String ASSIGNMENT_ID = "assignmentId";
   
   private static final String SEARCH_TYPE_ID = "searchType";
   private static final String INTERACTIVE_SEARCH_TYPE = "is";
   private static final String BASIC_SEARCH_TYPE = "bs";
   
   private static final String CREATE_USER_ERROR = "An error occured while trying to create the new user.";
   private static final String EMAIL_IN_USE = "This email is already in use.";
   private static final String INVALID_LOGIN = "Invalid username/password";
   private static final String INFO_RETRIEVE_FAILED = "Could not obtain user information.";
   private static final String BAD_SESSION_ID = "A valid session could not be obtained.";
   
   private static final String REGISTRATION_ERROR_CONTAINER = "registrationErrorContainer";
   private static final String LOGIN_ERROR_CONTAINER = "loginErrorContainer";
   
   private static final String REGISTER_MODAL = "modalRegister";
   
   private static final String REGISTRATION_FIRST_NAME = "firstName";
   private static final String REGISTRATION_LAST_NAME = "lastName";
   private static final String REGISTRATION_EMAIL = "newEmail";
   private static final String REGISTRATION_PASSWORD = "newPassword";
   private static final String LOGIN_USERID = "loginEmail";
   private static final String LOGIN_PASSWORD = "loginPassword";
   
   private static final String REGISTER_FORM = "register";
   private static final String LOGIN_FORM = "login";
   
   private static final String LOGIN_BUSY = "loginBusy";
   private static final String REGISTRATION_BUSY = "registrationBusy";   
   
   private static final String BS_FIELD = "basicSearchInput";  
   private static final String BS_SEARCH_ICON = "basicSearchIcon";
   private static final String BS_RESULTS = "basicSearchResults";
   private static final String BS_WHATIS = "whatIsThisBasicSearch";
   private static final String BS_COUNTER_CONTAINER = "numberOfBasicSearchResults";
   private static final String BS_COUNTER = "basicSearchResultsCounter";
   private static final String BS_BUSY = "basicSearchBusy";
   
   private static final String IS_DESC_HEADER = "interactiveSearchDescHeader";
   private static final String IS_FOOTER_TEXT = "interactiveSearchFooter";
   
   private static final String IS_WHATIS = "whatIsThis";
   
   private static final String COMMENTS_MODAL = "modalResourceComments";
   private static final String COMMENTS_TITLE = "resourceCommentsTitle";
   private static final String COMMENTS_CONTAINER = "resourceCommentsContainer";
   
   private static final String IS_FIELD = "interactiveSearchInput";
   private static final String IS_HEADER_FIELD = "interactiveSearchHeaderInput";
   private static final String IS_HEADER_SEARCH_ICON = "interactiveSearchIcon";

   private static final String IS_RESULTS = "interactiveSearchResults";
   private static final String IS_COUNTER_CONTAINER = "numberOfInteractiveSearchResults";
   private static final String IS_COUNTER = "interactiveSearchResultsCounter";
   private static final String IS_BUSY = "interactiveSearchBusy";
   private static final String IS_FILTER_TOOLS = "interactiveSearchResultsFilterTools";
   private static final String IS_RESULTS_SCREEN = "interactiveSearchResultsScreen";   
   //private static final String IS_BREADCRUMB_HELP = "interactiveSearchBreadCrumbsHelp";
   private static final String IS_BREADCRUMB_CONT = "interactiveSearchBreadCrumbs";
   //private static final String IS_QUESTION = "interactiveSearchQuestion";   
   private static final String IS_WORD_DEF_CONTAINER = "interactiveSearchResultsWordDefinitionContainer";   
   private static final String IS_FILTER_NAVIGATION = "intSrchFilterNavigation";   
   private static final String IS_FILTER_NAV_QUESTION = "intSrchFilterNavQuestionLabel"; 
   private static final String IS_FILTER_NAV_ANSWER_1_LI = "intSrchFilterNavAnswer1Li";
   private static final String IS_FILTER_NAV_ANSWER_2_LI = "intSrchFilterNavAnswer2Li";
   private static final String IS_FILTER_NAV_ANSWER_3_LI = "intSrchFilterNavAnswer3Li";
   private static final String IS_FILTER_NAV_LINKS = "intSrchFilterNavLinks";  
   private static final String IS_FILTER_NAV_MORE_ANSWERS = "intSrchFilterNavMoreAnswers"; 
   private static final String IS_FILTER_NAV_MORE_OPTIONS = "intSrchFilterNavMoreOptions";  
   private static final String IS_FILTER_NAV_GL_KDG_LI = "intSrchFilterNavAnswerGlKgLi";
   private static final String IS_FILTER_NAV_GL_ES_LI = "intSrchFilterNavAnswerGlEsLi";
   private static final String IS_FILTER_NAV_GL_MS_LI = "intSrchFilterNavAnswerGlMsLi";
   private static final String IS_FILTER_NAV_GL_HS_LI = "intSrchFilterNavAnswerGlHsLi";
   private static final String IS_FILTER_NAV_GL_CU_LI = "intSrchFilterNavAnswerGlCuLi";
   private static final String IS_FILTER_NAV_GL_VTP_LI = "intSrchFilterNavAnswerGlVtpLi";
   private static final String IS_FILTER_NAV_GL_APPLY_LI = "intSrchFilterNavAnswerApplyGlLi";
   private static final String IS_FILTER_NAV_GL_APPLY = "intSrchFilterNavAnswerApplyGl";   
   private static final String IS_FILTER_NAV_GL_TOGGLE_LI = "intSrchFilterNavAnswerToggleAllLi";
   private static final String IS_FILTER_NAV_GL_TOGGLE = "intSrchFilterNavAnswerToggleAll";   
   private static final String IS_FILTER_NAV_GL_KDG_CB = "intSrchGlKgCb";
   private static final String IS_FILTER_NAV_GL_ES_CB = "intSrchGlEsCb";
   private static final String IS_FILTER_NAV_GL_MS_CB = "intSrchGlMsCb";
   private static final String IS_FILTER_NAV_GL_HS_CB = "intSrchGlHsCb";
   private static final String IS_FILTER_NAV_GL_CU_CB = "intSrchGlCuCb";
   private static final String IS_FILTER_NAV_GL_VTP_CB = "intSrchGlVtpCb";   
   private static final String IS_APPLIED_GL_CONTAINER = "interactiveSearchAppliedGradeLevelsContainer";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_LINK_TXT = "intSrchFilterNavMoreOptionsTxt";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_CONTAINER = "intSrchFilterNavigationMopContainer";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_DISAMBIG_LI = "intSrchFilterNavigationMopDisambigLi";   
   private static final String IS_FILTER_NAV_MORE_OPTIONS_CAT_LI = "intSrchFilterNavigationMopRelCatLi";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_TOPIC_LI = "intSrchFilterNavigationMopRelTopicLi";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_GL_LI = "intSrchFilterNavigationMopGradeLevLi";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_DISAMBIG = "intSrchFilterNavigationMopDisambig";   
   private static final String IS_FILTER_NAV_MORE_OPTIONS_CAT = "intSrchFilterNavigationMopRelCat";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_TOPIC = "intSrchFilterNavigationMopRelTopic";
   private static final String IS_FILTER_NAV_MORE_OPTIONS_GL = "intSrchFilterNavigationMopGradeLev";
   
   private static final String IS_DIV = "interactiveSearch";
   private static final String BS_DIV = "basicSearch";
   private static final String IS_TYPING = "interactiveSearchTyping";
   
   private static final String IS_TAB_LINK = "interactiveSearchTabLink";
   private static final String IS_TAB = "interactiveTab";   
   private static final String BS_TAB_LINK = "basicSearchTabLink";
   private static final String BS_TAB = "basicTab";
   
   private static final String DEFAULT_HEADER = "defaultHeader";
   private static final String SEARCH_HEADER = "searchHeader";
   
   @SuppressWarnings("unused")
   private boolean haveBasicSearchResults = false;
   private boolean haveInteractiveSearchResults = false;
   
   //Hides both possible login busy images
   private void hideLoginBusyImages() {
      DsUtil.hideLabel(LOGIN_BUSY);
      DsUtil.hideLabel(REGISTRATION_BUSY);
   }
   
   //Handles a bad login/null session ID
   private void handleBadLogin (ESBPacket result) {
      if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().containsKey(DsESBApi.MSG_KEY)) {
         if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().get(DsESBApi.MSG_KEY).toString().equalsIgnoreCase(DsESBApi.INVALID_USERNAME_MSG)) {            
            DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,INVALID_LOGIN);
         }
         else DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,BAD_SESSION_ID);
      }
      else DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,BAD_SESSION_ID);
   }
   
   //sets up and sends the user to the home page
   private void sendToUserHomePage() {
      hideLoginBusyImages();      
      PageAssembler.closePopup(REGISTER_MODAL);
      PageAssembler.setTemplate(getTemplates().getHeader().getText(), getTemplates().getFooter().getText(), CONTENT_PANE);
      //DsUtil.setUpAppropriateHomePage(getDispatcher());
      getDispatcher().loadUserHomeScreen();
   }
   
   //initialize application settings
   private void initApplicationSettings() {
      DsESBApi.decalsGetApplicationSettings(new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {         
            DsSession.getInstance().setSessionState(DsSession.SessionState.LOGGED_IN);
            DsSession.getInstance().parseAppSettings(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());      
            DsSession.getInstance().buildUserGroupsAndCollections();
            DsSession.getInstance().setCachedLrSearchHandler(null);
            sendToUserHomePage();
         }
         @Override
         public void onFailure(Throwable caught) {  
            hideLoginBusyImages();
            DsUtil.handleFailedApiCall(caught);
         }
      });     
   }
   
   //Handles a login request
   private void handleLogin(ESBPacket result) {      
      String sessionId = result.getPayloadString();
      if (sessionId == null) {
         hideLoginBusyImages();
         handleBadLogin(result);
      }
      else {
         ESBApi.setSessionId(sessionId);
         ESBApi.username = userId;
         DsESBApi.decalsGetUser(userId, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               DsSession.getInstance().setSessionUser(new AppUser(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject()));
               initApplicationSettings();
            }
            @Override
            public void onFailure(Throwable caught) {
               hideLoginBusyImages();
               DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,INFO_RETRIEVE_FAILED);
            }
         });                
      }
   }
   
   //Initiates a ESB login
   private void doLogin() {      
      DsESBApi.decalsLogin(userId, password, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {handleLogin(result);}
         @Override
         public void onFailure(Throwable caught) {
            hideLoginBusyImages();
            DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,INVALID_LOGIN);
         }
      });
   }
   
   //Handles account create request
   private void handleUserCreateResponse(ESBPacket result) {
      if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().containsKey(DsESBApi.MSG_KEY)) {
         if (result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject().get(DsESBApi.MSG_KEY).toString().equalsIgnoreCase(DsESBApi.TRUE_MSG)) doLogin();
         else {
            DsUtil.hideLabel(REGISTRATION_BUSY);
            DsUtil.showSimpleErrorMessage(REGISTRATION_ERROR_CONTAINER,EMAIL_IN_USE);
         }
      }
      else {
         DsUtil.hideLabel(REGISTRATION_BUSY);
         DsUtil.showSimpleErrorMessage(REGISTRATION_ERROR_CONTAINER,CREATE_USER_ERROR);
      }
   }
   
   //Account create request listener
   protected EventCallback createAccountListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.showLabel(REGISTRATION_BUSY);
         String firstName = DsUtil.getTextBoxText(REGISTRATION_FIRST_NAME);
         String lastName = DsUtil.getTextBoxText(REGISTRATION_LAST_NAME);
         String email = DsUtil.getTextBoxText(REGISTRATION_EMAIL);
         userId = email;
         password = DsUtil.getTextBoxText(REGISTRATION_PASSWORD);
         DsESBApi.decalsCreateUser(email, password, firstName, lastName, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleUserCreateResponse(result);}
            @Override
            public void onFailure(Throwable caught) {
               DsUtil.hideLabel(REGISTRATION_BUSY);
               DsUtil.handleFailedApiCall(caught);
            }
         });
      }
   };
   
   //Login listener
   protected EventCallback loginListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         DsUtil.showLabel(LOGIN_BUSY);
         userId = DsUtil.getTextBoxText(LOGIN_USERID);
         password = DsUtil.getTextBoxText(LOGIN_PASSWORD);
         doLogin();
      }
   };
   
   //Resets the basic search fields
   private void resetBasicSearch() {
      DsUtil.hideLabel(BS_RESULTS);
      DsUtil.hideLabel(BS_COUNTER_CONTAINER);
      DsUtil.showLabel(BS_WHATIS);
      DsUtil.setTextBoxText(BS_FIELD,"");
      DsUtil.setFocus(BS_FIELD);
      DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(BS_RESULTS));
      haveBasicSearchResults = false;
   }
   
   //Sets up and calls basic search handler
   private void performBasicSearch(String searchTerm) {
      haveBasicSearchResults = true;
      DsUtil.showLabel(BS_RESULTS);
      DsUtil.hideLabel(BS_WHATIS);
      basicSearchHandler.performBasicSearch(1, searchTerm, BS_RESULTS, getTemplates().getBasicSearchResultWidget().getText(), BS_COUNTER, BS_COUNTER_CONTAINER, BS_BUSY);
   }
   
   //Basic search request listener
   protected EventCallback basicSearchListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
    	  if(event.getTypeInt() == Event.ONKEYDOWN){
    		  if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
    			  String basicSearchTerm = DsUtil.getTextBoxText(BS_FIELD).trim();            
    			  if (basicSearchTerm == null || basicSearchTerm.trim().isEmpty()) resetBasicSearch();
    			  else performBasicSearch(basicSearchTerm);        
    		  }
    	  }else if (event.getTypeInt() == Event.ONCLICK){
    		  String basicSearchTerm = DsUtil.getTextBoxText(BS_FIELD).trim();            
			  if (basicSearchTerm == null || basicSearchTerm.trim().isEmpty()) resetBasicSearch();
			  else performBasicSearch(basicSearchTerm);
    	  }
      }
   };
   
   //Resets the interactive search fields
   private void resetInteractiveSearch() {      
      interactiveSearchHandler.clearSearch();
      DsUtil.hideLabel(IS_FILTER_TOOLS);
      DsUtil.hideLabel(IS_FILTER_NAVIGATION);      
      DsUtil.hideLabel(IS_RESULTS_SCREEN);
      DsUtil.hideLabel(IS_TYPING);
      DsUtil.hideLabel(SEARCH_HEADER);
      DsUtil.showLabel(DEFAULT_HEADER);
      DsUtil.showLabel(IS_DIV);
      DsUtil.setFocus(IS_FIELD);
      DsUtil.setTextBoxText(IS_FIELD,"");
      DsUtil.setTextBoxText(IS_HEADER_FIELD,"");
      DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(IS_RESULTS));
      haveInteractiveSearchResults = false;
   }
   
   //Generates a SearchHandlerParamPacket with the needed element IDs for an interactive search...so many :(
   private SearchHandlerParamPacket generateInteractiveSearchParamPacket() {
      SearchHandlerParamPacket packet = new SearchHandlerParamPacket();
      packet.setResultsContainerId(IS_RESULTS);
      packet.setCounterElementId(IS_COUNTER);
      packet.setCounterContainerElementId(IS_COUNTER_CONTAINER);
      packet.setSearchBusyElementId(IS_BUSY);
      packet.setBreadCrumbElementId(IS_BREADCRUMB_CONT);
      packet.setWordDefContainerId(IS_WORD_DEF_CONTAINER);
      packet.setFilterNavContaierId(IS_FILTER_NAVIGATION);
      packet.setFilterNavQuestionId(IS_FILTER_NAV_QUESTION);
      packet.setFilterNavAnswer1LiId(IS_FILTER_NAV_ANSWER_1_LI);
      packet.setFilterNavAnswer2LiId(IS_FILTER_NAV_ANSWER_2_LI);
      packet.setFilterNavAnswer3LiId(IS_FILTER_NAV_ANSWER_3_LI);
      packet.setFilterNavLinksId(IS_FILTER_NAV_LINKS);
      packet.setFilterNavMoreAnswersLinkId(IS_FILTER_NAV_MORE_ANSWERS);
      packet.setFilterNavMoreOptionsLinkId(IS_FILTER_NAV_MORE_OPTIONS);
      packet.setFilterNavGrdLvlKgLiId(IS_FILTER_NAV_GL_KDG_LI);
      packet.setFilterNavGrdLvlEsLiId(IS_FILTER_NAV_GL_ES_LI);
      packet.setFilterNavGrdLvlMsLiId(IS_FILTER_NAV_GL_MS_LI);
      packet.setFilterNavGrdLvlHsLiId(IS_FILTER_NAV_GL_HS_LI);
      packet.setFilterNavGrdLvlCuLiId(IS_FILTER_NAV_GL_CU_LI);
      packet.setFilterNavGrdLvlVtpLiId(IS_FILTER_NAV_GL_VTP_LI);
      packet.setFilterNavGrdLvlApplyLiId(IS_FILTER_NAV_GL_APPLY_LI);
      packet.setFilterNavGrdLvlApplyLinkId(IS_FILTER_NAV_GL_APPLY);
      packet.setFilterNavGrdLvlKgCbId(IS_FILTER_NAV_GL_KDG_CB);
      packet.setFilterNavGrdLvlEsCbId(IS_FILTER_NAV_GL_ES_CB);
      packet.setFilterNavGrdLvlMsCbId(IS_FILTER_NAV_GL_MS_CB);
      packet.setFilterNavGrdLvlHsCbId(IS_FILTER_NAV_GL_HS_CB);
      packet.setFilterNavGrdLvlCuCbId(IS_FILTER_NAV_GL_CU_CB);
      packet.setFilterNavGrdLvlVtpCbId(IS_FILTER_NAV_GL_VTP_CB);
      packet.setAppliedGradeLevelsContainerId(IS_APPLIED_GL_CONTAINER);
      packet.setFilterNavMoreOptContainerId(IS_FILTER_NAV_MORE_OPTIONS_CONTAINER);
      packet.setFilterNavMoreOptDisambigLiId(IS_FILTER_NAV_MORE_OPTIONS_DISAMBIG_LI);
      packet.setFilterNavMoreOptCatLiId(IS_FILTER_NAV_MORE_OPTIONS_CAT_LI);
      packet.setFilterNavMoreOptTopicLiId(IS_FILTER_NAV_MORE_OPTIONS_TOPIC_LI);
      packet.setFilterNavMoreOptGrdLvlLiId(IS_FILTER_NAV_MORE_OPTIONS_GL_LI);
      packet.setFilterNavMoreOptDisambigId(IS_FILTER_NAV_MORE_OPTIONS_DISAMBIG);
      packet.setFilterNavMoreOptCatId(IS_FILTER_NAV_MORE_OPTIONS_CAT);
      packet.setFilterNavMoreOptTopicId(IS_FILTER_NAV_MORE_OPTIONS_TOPIC);
      packet.setFilterNavMoreOptGrdLvlId(IS_FILTER_NAV_MORE_OPTIONS_GL);
      packet.setFilterNavMoreOptLinkTxtId(IS_FILTER_NAV_MORE_OPTIONS_LINK_TXT);
      packet.setSearchTermBoxId(IS_HEADER_FIELD);
      packet.setFilterNavGrdLvlToggleLiId(IS_FILTER_NAV_GL_TOGGLE_LI);
      packet.setFilterNavGrdLvlToggleLinkId(IS_FILTER_NAV_GL_TOGGLE);   
      packet.setCommentModalId(COMMENTS_MODAL);
      packet.setCommentTitleId(COMMENTS_TITLE);
      packet.setCommentContainerId(COMMENTS_CONTAINER);
      return packet;
   }
   
   //Sets up and calls interactive search handler
   private void performInteractiveSearch(String searchTerm) {
      haveInteractiveSearchResults = true;      
      DsUtil.hideLabel(IS_TYPING);
      //DsUtil.hideLabel(IS_BREADCRUMB_CONT);
      DsUtil.showLabel(IS_FILTER_TOOLS);
      DsUtil.showLabel(IS_FILTER_NAVIGATION);     
      DsUtil.showLabel(IS_RESULTS_SCREEN);
      DsUtil.showLabel(IS_RESULTS);
      //DsUtil.showLabel(IS_QUESTION);
      //DsUtil.showLabel(IS_BREADCRUMB_HELP);
      interactiveSearchHandler.performInteractiveSearch(searchTerm, getTemplates().getInteractiveSearchResultWidget().getText(),
            generateInteractiveSearchParamPacket(),RatingHandlerType.VIEW,CommentHandlerType.VIEW,false);      
   }
   
   //Interactive search listener for header bar
   protected EventCallback interactiveSearchHeaderListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
    	if(event.getTypeInt() == Event.ONKEYDOWN){
    		if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
    			String interactiveSearchTerm = DsUtil.getTextBoxText(IS_HEADER_FIELD).trim();            
    			if (interactiveSearchTerm == null || interactiveSearchTerm.isEmpty()) resetInteractiveSearch();
    			else performInteractiveSearch(interactiveSearchTerm);
    		}
    	}else if(event.getTypeInt() == Event.ONCLICK){
    		String interactiveSearchTerm = DsUtil.getTextBoxText(IS_HEADER_FIELD).trim();            
    		if (interactiveSearchTerm == null || interactiveSearchTerm.isEmpty()) resetInteractiveSearch();
    		else performInteractiveSearch(interactiveSearchTerm);
    	}
      }
   };
   
   //Interactive search listener for middle page search box
   protected EventCallback interactiveSearchListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         String value = DsUtil.getTextBoxText(IS_FIELD);
         DsUtil.setTextBoxText(IS_HEADER_FIELD, value);
         DsUtil.hideLabel(DEFAULT_HEADER);
         DsUtil.showLabel(SEARCH_HEADER);
         DsUtil.setFocus(IS_HEADER_FIELD);
         DsUtil.hideLabel(IS_DIV);
         DsUtil.showLabel(IS_TYPING);
         DsUtil.setTextBoxText(IS_FIELD,"");
      }
   };
   
   //Sets the interactive search tab as the active tab
   private void setInterativeTabAsActiveTab() {
      currentTab = Tabs.INTERACTIVE_TAB;
      DsUtil.hideLabel(BS_DIV);
      String isSearchText = DsUtil.getTextBoxText(IS_HEADER_FIELD);
      if (isSearchText == null || isSearchText.trim().isEmpty()) {
         DsUtil.hideLabel(SEARCH_HEADER);
         DsUtil.showLabel(DEFAULT_HEADER);
         DsUtil.showLabel(IS_DIV);
         DsUtil.setFocus(IS_FIELD);            
      }
      else{
         DsUtil.hideLabel(DEFAULT_HEADER);
         DsUtil.showLabel(SEARCH_HEADER);            
         DsUtil.setFocus(IS_HEADER_FIELD);
         if (haveInteractiveSearchResults) {
            DsUtil.showLabel(IS_FILTER_TOOLS);
            DsUtil.showLabel(IS_FILTER_NAVIGATION);               
            DsUtil.showLabel(IS_RESULTS_SCREEN);
         }
         else DsUtil.showLabel(IS_TYPING);
      }      
   }
   
   //Interactive search tab listener and handler
   protected EventCallback interactiveTabListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (Tabs.INTERACTIVE_TAB.equals(currentTab)) return;
         setInterativeTabAsActiveTab();
      }
   };
   
   //Sets the basic search tab as the active tab
   private void setBasicTabAsActiveTab() {
      currentTab = Tabs.BASIC_TAB;
      DsUtil.hideLabel(SEARCH_HEADER);
      DsUtil.showLabel(DEFAULT_HEADER);
      DsUtil.hideLabel(IS_DIV);
      DsUtil.hideLabel(IS_TYPING);
      DsUtil.hideLabel(IS_FILTER_TOOLS);
      DsUtil.hideLabel(IS_FILTER_NAVIGATION);         
      DsUtil.hideLabel(IS_RESULTS_SCREEN);
      DsUtil.showLabel(BS_DIV);
      DsUtil.setFocus(BS_FIELD);
   }
   
   //Basic search tab listener and handler
   protected EventCallback basicTabListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (Tabs.BASIC_TAB.equals(currentTab)) return;
         setBasicTabAsActiveTab();
      }
   };
   
   //Attempts to retrieve the assignment ID from the query string
   private void getAssignmentIdFromQueryString() {
      String queryString = Window.Location.getQueryString();      
      if (queryString != null && !queryString.trim().isEmpty()) {
         queryString = queryString.substring(1); 
         String[] qps = queryString.split("&");
         String[] aip;
         for (String qp:qps) {
            if (qp.startsWith(ASSIGNMENT_ID + "=")) {
               aip = qp.split("=");
               DsSession.getInstance().setAssignmentId(aip[1]);
            }
         }
      }
   }
   
   //Attempts to retrieve the search type from the query string
   private void getSearchTypeFromQueryString() {
      DsSession.getInstance().setSessionSearchType(DsSession.SearchType.DUAL);
      String queryString = Window.Location.getQueryString();      
      if (queryString != null && !queryString.trim().isEmpty()) {
         queryString = queryString.substring(1); 
         String[] qps = queryString.split("&");
         String[] st;
         for (String qp:qps) {
            if (qp.startsWith(SEARCH_TYPE_ID + "=")) {
               st = qp.split("=");
               if (INTERACTIVE_SEARCH_TYPE.equalsIgnoreCase(st[1])) DsSession.getInstance().setSessionSearchType(DsSession.SearchType.INTERACTIVE);
               else if (BASIC_SEARCH_TYPE.equalsIgnoreCase(st[1])) DsSession.getInstance().setSessionSearchType(DsSession.SearchType.BASIC);               
            }
         }
      }      
   }
   
   //Set up the search tabs based on search type - being used for Memphis evaluation
   private void setUpSearchTabs() {
      if (DsSession.SearchType.DUAL.equals(DsSession.getInstance().getSessionSearchType())) {
         //DUAL search is the default search configuration...do nothing
      }
      else if (DsSession.SearchType.INTERACTIVE.equals(DsSession.getInstance().getSessionSearchType())) {         
         setInterativeTabAsActiveTab();
         DsUtil.hideLabel(BS_TAB);
         DsUtil.setLabelAttribute(IS_TAB,"class","tab active");
         DsUtil.setAnchorText(IS_TAB_LINK,"Search");
         DsUtil.hideLabel(IS_WHATIS);         
         DsUtil.setLabelText(IS_DESC_HEADER,"Access a Wide Range of Learning Registry Resources");
         DsUtil.hideLabel(IS_FOOTER_TEXT);
      } 
      else if (DsSession.SearchType.BASIC.equals(DsSession.getInstance().getSessionSearchType())) {
         setBasicTabAsActiveTab();
         DsUtil.hideLabel(IS_TAB);
         DsUtil.setLabelAttribute(BS_TAB,"class","tab active");
         DsUtil.setAnchorText(BS_TAB_LINK,"Search");
         DsUtil.hideLabel(BS_WHATIS);
      }      
   }

   //Display handler for guest screen display
   @Override
   public void display() {
      //TODO make magnifying glass work for search button
      PageAssembler.setTemplate(getTemplates().getGuestHeader().getText(), getTemplates().getFooter().getText(), CONTENT_PANE);
      PageAssembler.ready(new HTML(getTemplates().getGuestPanel().getText()));
      PageAssembler.buildContents();      
      currentTab = Tabs.INTERACTIVE_TAB;
      PageAssembler.attachHandler(IS_TAB_LINK,Event.ONCLICK,interactiveTabListener);
      PageAssembler.attachHandler(BS_TAB_LINK,Event.ONCLICK,basicTabListener);
      PageAssembler.attachHandler(IS_TAB,Event.ONCLICK,interactiveTabListener);
      PageAssembler.attachHandler(BS_TAB,Event.ONCLICK,basicTabListener);
      PageAssembler.attachHandler(REGISTER_FORM,VALID_EVENT,createAccountListener);
      PageAssembler.attachHandler(LOGIN_FORM,VALID_EVENT,loginListener);
      PageAssembler.attachHandler(BS_FIELD,Event.ONKEYDOWN,basicSearchListener);
      PageAssembler.attachHandler(BS_SEARCH_ICON, Event.ONCLICK, basicSearchListener);
      
      PageAssembler.attachHandler(IS_HEADER_FIELD,Event.ONKEYDOWN,interactiveSearchHeaderListener);
      PageAssembler.attachHandler(IS_HEADER_SEARCH_ICON, Event.ONCLICK, interactiveSearchHeaderListener);
      
      PageAssembler.attachHandler(IS_FIELD,Event.ONKEYDOWN,interactiveSearchListener);
      getAssignmentIdFromQueryString();
      getSearchTypeFromQueryString();
      setUpSearchTabs();
      DsUtil.sendTrackingMessage("Entered guest screen");      
      //TODO make magnifying glass active
   }
   
   @Override
   public void lostFocus() {}
}
