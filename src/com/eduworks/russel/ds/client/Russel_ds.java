package com.eduworks.russel.ds.client;

import com.eduworks.russel.ds.client.pagebuilder.DsHtmlTemplates;
import com.eduworks.russel.ui.client.Russel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Russel_ds extends Russel implements EntryPoint, ValueChangeHandler<String> {

	public void onModuleLoad() {
		view = new DsScreenDispatch();
		templates = GWT.create(DsHtmlTemplates.class);
		handlers = new DsEventHandlers();
	}
}
