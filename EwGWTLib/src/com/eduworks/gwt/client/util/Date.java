/*
Copyright 2012-2013 Eduworks Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.eduworks.gwt.client.util;

import com.google.gwt.core.client.JavaScriptObject;

public class Date {
	JavaScriptObject d = JavaScriptObject.createObject();
	
	public Date() {
		makeDateObject();
	}
	
	public final native void makeDateObject() /*-{
		this.@com.eduworks.gwt.client.util.Date::d = new Date();
	}-*/;
	
	public final native int getYear() /*-{
		return this.@com.eduworks.gwt.client.util.Date::d.getFullYear();
	}-*/; 
	
	public final native int getDate() /*-{
		return this.@com.eduworks.gwt.client.util.Date::d.getDate();
	}-*/;
	
	public final native int getMonth() /*-{
		return this.@com.eduworks.gwt.client.util.Date::d.getMonth();
	}-*/;
	
	public final native void setDate(int date) /*-{
		this.@com.eduworks.gwt.client.util.Date::d.setDate(date);
	}-*/;

	public final native int getTime() /*-{
	return this.@com.eduworks.gwt.client.util.Date::d.getTime();
}-*/; 
}