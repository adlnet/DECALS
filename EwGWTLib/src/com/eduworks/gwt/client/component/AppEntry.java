package com.eduworks.gwt.client.component;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;

public class AppEntry extends AppSettings implements EntryPoint, ValueChangeHandler<String> {

   protected static final String SITE_NAME_PROP = "site.name";
   protected static final String ROOT_URL_PROP = "root.url";
   protected static final String SITE_URL_PROP = "site.url";
   protected static final String ALFRESCO_URL_PROP = "alfresco.url";
   protected static final String ESB_URL_PROP = "esb.url";
   protected static final String HELP_URL_PROP = "help.url";

   protected static final String CONTENT_STREAM = "contentStream";
   
   protected static final String DEFAULT_INSTALLATION_SETTINGS_LOC = "../js/installation.settings";
   protected static final String DEFAULT_MODULE_PROPERTIES_LOC = "../js/module.properties";

   protected static String parseProperty(String prop) {
      return prop.substring(prop.indexOf("\"") + 1, prop.lastIndexOf("\""));
   }
   
   // Parsing of installation.properties and module.properties is reserved for the entry point application.
   // The following checks to verify that property "site.name" is the first property.
   private static boolean parseApplicationProperties(String[] rawProperties) {
      if ((rawProperties[0].indexOf(SITE_NAME_PROP) != -1)) {
         for (String prop:rawProperties) {            
            if (prop.indexOf(ROOT_URL_PROP) != -1) CommunicationHub.rootURL = parseProperty(prop);
            else if (prop.indexOf(SITE_URL_PROP) != -1) CommunicationHub.siteURL = parseProperty(prop);
            else if (prop.indexOf(ESB_URL_PROP) != -1)  CommunicationHub.esbURL = parseProperty(prop);
            else if (prop.indexOf(ALFRESCO_URL_PROP) != -1)  CommunicationHub.baseURL = parseProperty(prop);
            else if (prop.indexOf(HELP_URL_PROP) != -1) {
               helpURL = parseProperty(prop);
               PageAssembler.setHelp(helpURL);
            }
            else if (prop.indexOf(SITE_NAME_PROP) != -1) {
               siteName = parseProperty(prop);
               PageAssembler.setSiteName(siteName);
            }
         }
         return true;         
      }
      return false;
   }
   
   protected static String getInstallationSettingsLoc() {return DEFAULT_INSTALLATION_SETTINGS_LOC;}
   
   protected static String getModulePropertiesLoc() {return DEFAULT_MODULE_PROPERTIES_LOC;}
   
   private static void fetchProperties(final ESBCallback<ESBPacket> callback) {
      CommunicationHub.sendHTTP(CommunicationHub.GET, getInstallationSettingsLoc(), null, false, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket ESBPacket) {
            if (parseApplicationProperties(ESBPacket.getString(CONTENT_STREAM).split("\r\n|\r|\n"))) {
               CommunicationHub.sendHTTP(CommunicationHub.GET, getModulePropertiesLoc(), null, false, callback);
            } 
            else {
               Window.alert("Invalid installation settings");
            }
         }
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Couldn't find network settings");
         }
      });
   }

   @Override
   public void onValueChange(ValueChangeEvent<String> event) {
      dispatcher.loadHistoryScreen(event.getValue());      
   }

   @Override
   public void onModuleLoad() {
      //add uncaught exception handler
      GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
         @Override
         public void onUncaughtException(Throwable e) {
            Window.alert("onUncaughtException errors");
            Window.alert(e.toString());
            if (e.getCause() != null) Window.alert(e.getCause().getMessage());
            e.printStackTrace();
         }
      });     
      
      //clear history
      dispatcher.clearHistory();
      
      //add history change handler
      History.addValueChangeHandler(this);

      //fetch properties and setup history state/screen
      fetchProperties(new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket ESBPacket) {
            PageAssembler.setBuildNumber(ESBPacket.getString(CONTENT_STREAM).substring(ESBPacket.getString(CONTENT_STREAM).lastIndexOf("=") + 1));
            dispatcher.setDefaultScreen(defaultScreen);
            History.fireCurrentHistoryState();
         }

         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Couldn't find build number");
         }
      });

   }

}
