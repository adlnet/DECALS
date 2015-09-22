package com.eduworks.gwt.client.util;

import com.google.gwt.json.client.JSONArray;

public class JSONUtils {
	public static native JSONArray sort(JSONArray ja) /*-{
		if (ja!=null) {
			ja["jsArray"].sort();
			return ja;
		} else
			return null;
	}-*/;
	
}
