package com.eduworks.gwt.client.util;

import java.util.UUID;

public class MathUtil {
	public final static native double roundNumber(double num, int places) /*-{
	    var result = Math.round(num * Math.pow(10, places))
	            / Math.pow(10, places);
	    return result;
	}-*/;

	
	public static native String generateUUID() /*-{
		var timeNow = Date.now();
		var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, 
																  function (c) {
																  	var r = (timeNow + Math.random() * 16) % 16 | 0;
																  	timeNow = Math.floor(timeNow/16);
																  	return ((c=='x')? r : (r & 0x7 | 0x8)).toString(16);
																  });
		return uuid;
	}-*/;
}
