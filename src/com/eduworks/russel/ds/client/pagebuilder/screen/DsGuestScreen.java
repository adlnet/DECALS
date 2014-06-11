package com.eduworks.russel.ds.client.pagebuilder.screen;

import com.google.gwt.json.client.JSONObject;

import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.russel.ds.client.DsSession;
import com.eduworks.russel.ds.client.DsUtil;
import com.eduworks.russel.ds.client.pagebuilder.DecalsScreen;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class DsGuestScreen extends DecalsScreen {
   
   private String username = "";
   private String password = "";
   
   private static final String EMAIL_IN_USE = "The given email has already been registered.";
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
   private static final String LOGIN_USERNAME = "loginEmail";
   private static final String LOGIN_PASSWORD = "loginPassword";
   
   private static final String REGISTER_FORM = "register";
   private static final String LOGIN_FORM = "login";
   
   private static final String VALID_EVENT = "valid";
   
   private static final String FIRST_NAME_KEY = "firstName";
   private static final String LAST_NAME_KEY = "lastName";
   
   private void parseUserInfo(JSONObject userInfo) {
      try {
         DsSession.getInstance().setFirstName(userInfo.get(FIRST_NAME_KEY).isString().stringValue());
         DsSession.getInstance().setLastName(userInfo.get(LAST_NAME_KEY).isString().stringValue());
      }
      catch (Exception e) {
         DsSession.getInstance().setFirstName(ESBApi.username);
      }
   }
   
   private void handleLogin(ESBPacket result) {
      String sessionId = result.getPayloadString();
      if (sessionId == null) DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,BAD_SESSION_ID);
      else {
         ESBApi.sessionId = sessionId;
         ESBApi.username = username;
         ESBApi.getUser(new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               parseUserInfo(result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject());
               PageAssembler.closePopup(REGISTER_MODAL);
               PageAssembler.setTemplate(templates().getHeader().getText(), templates().getFooter().getText(), CONTENT_PANE);
               DsUtil.setUpAppropriateHomePage(view());
            }
            @Override
            public void onFailure(Throwable caught) {DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,INFO_RETRIEVE_FAILED);}
            });                
      }
   }
   
   private void doLogin() {
      ESBApi.login(username, password, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {handleLogin(result);}
         @Override
         public void onFailure(Throwable caught) {DsUtil.showSimpleErrorMessage(LOGIN_ERROR_CONTAINER,INVALID_LOGIN);}
         });
   }
   
   private void handleUserCreate(ESBPacket result) {      
      if (result.getPayloadString().equalsIgnoreCase("true")) {
         String firstName = ((TextBox)PageAssembler.elementToWidget(REGISTRATION_FIRST_NAME, PageAssembler.TEXT)).getText();
         String lastName = ((TextBox)PageAssembler.elementToWidget(REGISTRATION_LAST_NAME, PageAssembler.TEXT)).getText();
         ESBApi.updateUserAtCreate(firstName, lastName, username, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {doLogin();}
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
            });         
      }
      else {         
         DsUtil.showSimpleErrorMessage(REGISTRATION_ERROR_CONTAINER,EMAIL_IN_USE);
      }      
   }
   
   protected EventCallback createAccountListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         username = ((TextBox)PageAssembler.elementToWidget(REGISTRATION_EMAIL, PageAssembler.TEXT)).getText();
         password = ((TextBox)PageAssembler.elementToWidget(REGISTRATION_PASSWORD, PageAssembler.TEXT)).getText();
         ESBApi.createUser(username, password, new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {handleUserCreate(result);}
            @Override
            public void onFailure(Throwable caught) {DsUtil.handleFailedApiCall(caught);}
            });
      }
   };
   
   protected EventCallback loginListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         username = ((TextBox)PageAssembler.elementToWidget(LOGIN_USERNAME, PageAssembler.TEXT)).getText();
         password = ((TextBox)PageAssembler.elementToWidget(LOGIN_PASSWORD, PageAssembler.TEXT)).getText();
         doLogin();
      }
   };

   @Override
   public void display() {
      PageAssembler.setTemplate(templates().getGuestHeader().getText(), templates().getFooter().getText(), CONTENT_PANE);
      PageAssembler.ready(new HTML(templates().getGuestPanel().getText()));
      PageAssembler.buildContents();      
      PageAssembler.attachHandler(REGISTER_FORM,VALID_EVENT,createAccountListener);
      PageAssembler.attachHandler(LOGIN_FORM,VALID_EVENT,loginListener);
   }
   
   @Override
   public void lostFocus() {}
}
