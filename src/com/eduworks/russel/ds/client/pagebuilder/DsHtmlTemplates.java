package com.eduworks.russel.ds.client.pagebuilder;

import com.eduworks.russel.ui.client.pagebuilder.HtmlTemplates;
import com.google.gwt.resources.client.TextResource;

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
	
	@Source("template/DecalsGuestPanel.html")
   public TextResource getGuestPanel();
	
	@Source("template/DecalsSearchPanel.html")
	public TextResource getSearchPanel();
	
}
