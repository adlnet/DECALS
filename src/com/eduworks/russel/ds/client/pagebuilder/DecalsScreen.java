package com.eduworks.russel.ds.client.pagebuilder;

import com.eduworks.gwt.client.pagebuilder.ScreenTemplate;
import com.eduworks.russel.ds.client.DsScreenDispatch;
import com.eduworks.russel.ui.client.Constants;

public abstract class DecalsScreen extends ScreenTemplate {
	public DsScreenDispatch view(){return (DsScreenDispatch) Constants.view;}
	public DsHtmlTemplates templates(){return (DsHtmlTemplates) Constants.templates;}
	
}
