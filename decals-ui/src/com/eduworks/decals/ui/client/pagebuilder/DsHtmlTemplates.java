package com.eduworks.decals.ui.client.pagebuilder;

import com.eduworks.gwt.client.component.HtmlTemplates;
import com.google.gwt.resources.client.TextResource;

/**
 * 
 * Template loader.
 * 
 * @author Eduworks Corporation
 *
 */
public interface DsHtmlTemplates extends HtmlTemplates {
	/**
	 * getLoginWidget Retrieves the login widget source code
	 * @return TextResource
	 */
	@Source("template/DecalsGuestHeader.html")
	public TextResource getGuestHeader();
	
	@Source("template/DecalsHeader.html")
   public TextResource getHeader();
	
	@Source("template/DecalsFooter.html")
	public TextResource getFooter();
	
	@Source("template/DecalsStudentHomePanel.html")
	public TextResource getStudentHomePanel();
	
	@Source("template/DecalsTeacherHomePanel.html")
   public TextResource getTeacherHomePanel();
	
	@Source("template/DecalsUserHomePanel.html")
   public TextResource getUserHomePanel();
	
	@Source("template/DecalsAdminPanel.html")
   public TextResource getApplicationAdminPanel();
	
	@Source("template/DecalsUserManagement.html")
   public TextResource getUserManagementPanel();
	
	@Source("template/DecalsUserLRSearchPanel.html")
   public TextResource getUserLRSearchPanel();
	
	@Source("template/DecalsGuestPanel.html")
   public TextResource getGuestPanel();
	
	@Source("template/DecalsSearchPanel.html")
	public TextResource getSearchPanel();
	
	@Source("template/BasicSearchResultWidget.html")
	public TextResource getBasicSearchResultWidget();
	
	@Source("template/InteractiveSearchResultWidget.html")
   public TextResource getInteractiveSearchResultWidget();
	
	@Source("template/DarSearchResultWidget.html")
   public TextResource getDarSearchResultWidget();
	
	@Source("template/UserLRSearchResultWidget.html")
   public TextResource getUserLRSearchResultWidget();
	
	@Source("template/DecalsUserPreferencesPanel.html")
	public TextResource getUserPreferencesPanel();
}
