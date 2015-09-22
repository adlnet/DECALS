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

import org.vectomatic.arrays.ArrayBuffer;
import org.vectomatic.file.Blob;
import org.vectomatic.file.File;

import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;

public class BlobUtils {
	public static final native String getBlobURL(Blob data) /*-{
		if (window.webkitURL!=undefined&&window.webkitURL.createObjectURL!=undefined)
			return window.webkitURL.createObjectURL(data);
		else
			return window.URL.createObjectURL(data);
	}-*/;
	
	public static native Blob buildBlob(String typ, String contents) /*-{
		if (window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.OBlobBuilder || window.msBlobBuilder) {
			var bb = new (window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.OBlobBuilder || window.msBlobBuilder);
			bb.append(contents);
			return bb.getBlob(typ);
		} else if (window.Blob!=undefined) {
			var bb = new window.Blob([contents], { "type": typ });
			return bb;
		} else {
			Window.alert("Blob building is failing");
		}
	}-*/;

	public static native Blob buildBlob(String typ, ArrayBuffer contents) /*-{
		if (window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.OBlobBuilder || window.msBlobBuilder) {
			var bb = new (window.BlobBuilder || window.MozBlobBuilder || window.WebKitBlobBuilder || window.OBlobBuilder || window.msBlobBuilder);
			bb.append(contents);
			return bb.getBlob(typ);
		} else if (window.Blob!=undefined) {
			var bb = new window.Blob([contents], { "type":  typ });
			return bb;
		} else {
			Window.alert("Blob building is failing");
		}
	}-*/;
	
	public static native void getBinaryData(Blob data, String mimeType, ESBCallback<ESBPacket> callback) /*-{
		var fileReader = new FileReader();
		fileReader.onload = function (e) {
			callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFileSuccess(Ljava/lang/String;Ljava/lang/Object;)(mimeType, fileReader.result);
		};
		fileReader.readAsArrayBuffer(data);
	}-*/;
	
	public static native File getFile(String elementId) /*-{
		if ($wnd.document.getElementById(elementId).files===0) return;
		return $wnd.document.getElementById(elementId).files[0];
	}-*/;
}
