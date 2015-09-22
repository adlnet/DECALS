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

public class Browser
{

	public static final native boolean isBadIE() /*-{
		var rv = -1; // Return value assumes failure.

		if (navigator.appName == 'Microsoft Internet Explorer') {

			var ua = navigator.userAgent, re = new RegExp(
					"MSIE ([0-9]{1,}[\\.0-9]{0,})");

			if (re.exec(ua) !== null) {
				rv = parseFloat(RegExp.$1);
				return rv < 10;
			}
		} else if (navigator.appName == "Netscape") {
			return false;//rv = 11;
		}

		return false;
	}-*/;

	public static final native boolean isHtml5IE() /*-{
		var rv = -1; // Return value assumes failure.

		if (navigator.appName == 'Microsoft Internet Explorer') {

			var ua = navigator.userAgent, re = new RegExp(
					"MSIE ([0-9]{1,}[\\.0-9]{0,})");

			if (re.exec(ua) !== null) {
				rv = parseFloat(RegExp.$1);
				return rv >= 10;
			}
		} else if (navigator.appName == "Netscape") {
			/// in IE 11 the navigator.appVersion says 'trident'
			/// in Edge the navigator.appVersion does not say trident
			if (navigator.appVersion.indexOf('Trident') === -1 && navigator.appVersion.indexOf('Edge') === -1)
				return false;//rv = 12;
			else
				return true;//rv = 11;
		}

		return false;
	}-*/;

}
