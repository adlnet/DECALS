package com.eduworks.decals.ui.client.handler;

import com.eduworks.decals.ui.client.DsScreenDispatch;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.Event;

/**
 * Handler for application user tabs
 * 
 * @author Eduworks Corporation
 *
 */
public class DsUserTabsHandler {
   
   private static final String HOME_TAB_ID = "userHomeTab";
   private static final String HOME_TAB_LINK_ID = "userHomeTabLink";
   
   private static final String LR_SEARCH_TAB_ID = "userLRSearchTab";
   private static final String LR_SEARCH_TAB_LINK_ID = "userLRSearchTabLink";
   
   public enum UserTabs{HOME_TAB, LR_SEARCH_TAB, NO_TAB}
   
   private static final String TAB_CLASS = "tab";
   
   private UserTabs currentTab; 
   private DsScreenDispatch dispatcher;
   
   private static final DsUserTabsHandler INSTANCE = new DsUserTabsHandler();  
   
   private DsUserTabsHandler() {}

   public static DsUserTabsHandler getInstance() {return INSTANCE;}

   public UserTabs getCurrentTab() {return currentTab;}
   public void setCurrentTab(UserTabs currentTab) {this.currentTab = currentTab;}
   
   public void setHomeTabAsActive() {
      currentTab = UserTabs.HOME_TAB;
      dispatcher.loadUserHomeScreen();      
   }
   
   private void setLRSearchTabAsActive() {
      currentTab = UserTabs.LR_SEARCH_TAB;
      dispatcher.loadUserLRSearchScreen();      
   }
         
   public void setAsNoTabsActive() {
      currentTab = UserTabs.NO_TAB;
      DsUtil.setLabelAttribute(HOME_TAB_ID, "class", TAB_CLASS);
      DsUtil.setLabelAttribute(LR_SEARCH_TAB_ID, "class", TAB_CLASS);
   }
   
   protected EventCallback homeTabListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (UserTabs.HOME_TAB.equals(currentTab)) return;         
         setHomeTabAsActive();
      }     
   };
   
   protected EventCallback lrSearchTabListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         if (UserTabs.LR_SEARCH_TAB.equals(currentTab)) return;
         setLRSearchTabAsActive();
      }     
   };  
   
   public void init(DsScreenDispatch dispatcher) {
      this.dispatcher = dispatcher;
      PageAssembler.attachHandler(HOME_TAB_ID,Event.ONCLICK,homeTabListener);
      PageAssembler.attachHandler(HOME_TAB_LINK_ID,Event.ONCLICK,homeTabListener);
      PageAssembler.attachHandler(LR_SEARCH_TAB_ID,Event.ONCLICK,lrSearchTabListener);
      PageAssembler.attachHandler(LR_SEARCH_TAB_LINK_ID,Event.ONCLICK,lrSearchTabListener);  
   }   

}
