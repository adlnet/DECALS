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

import java.util.Vector;

import org.vectomatic.file.Blob;
import org.vectomatic.file.File;

import com.eduworks.gwt.client.model.ZipRecord;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Zip
{	
	@SuppressWarnings("rawtypes")
	private static final Vector createVector() {
		return new Vector();
	}
	
	private static final ZipRecord createZipRecord(String filename, Blob data) {
		return new ZipRecord(filename, data);
	}
	
	public static final native void grabEntries(File zipBlob, AsyncCallback<Vector<ZipRecord>> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		// use a BlobReader to read the zip from a Blob object
		zip.createReader(new zip.BlobReader(zipBlob), 
						 function(zipReader) {
							// get all entries from the zip
						 	zipReader.getEntries(function(entries) {
					 								var zipEntries = @com.eduworks.gwt.client.util.Zip::createVector()();
					 								for (var i=0;i<entries.length;i++)
					 									zipEntries.@java.util.Vector::add(Ljava/lang/Object;)(@com.eduworks.gwt.client.util.Zip::createZipRecord(Ljava/lang/String;Lorg/vectomatic/file/Blob;)(entries[i].filename, entries[i]));
					 								callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(zipEntries);
						  						 });
						},
						function(error) {
						  alert(error);
						});
	}-*/;

	public static final native void grabEntries(Blob zipBlob, AsyncCallback<Vector<ZipRecord>> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		// use a BlobReader to read the zip from a Blob object
		zip.createReader(new zip.BlobReader(zipBlob), 
						 function(zipReader) {
							// get all entries from the zip
						 	zipReader.getEntries(function(entries) {
					 								var zipEntries = @com.eduworks.gwt.client.util.Zip::createVector()();
					 								for (var i=0;i<entries.length;i++)
					 									zipEntries.@java.util.Vector::add(Ljava/lang/Object;)(@com.eduworks.gwt.client.util.Zip::createZipRecord(Ljava/lang/String;Lorg/vectomatic/file/Blob;)(entries[i].filename, entries[i]));
					 								callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(zipEntries);
						  						 });
						},
						function(error) {
						  alert(error);
						});
	}-*/;
	
	public static final native void inflateEntry(ZipRecord zipEntry, boolean binary, AsyncCallback<ZipRecord> callback) /*-{
		var readFunc;
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		 
		if (binary)
			readFunc = new zip.BlobWriter();
		else
			readFunc = new zip.TextWriter();
			
		zipEntry.@com.eduworks.gwt.client.model.ZipRecord::getData()().getData(readFunc, function (data) {
										var ap = @com.eduworks.gwt.client.util.Zip::createZipRecord(Ljava/lang/String;Lorg/vectomatic/file/Blob;)(zipEntry.@com.eduworks.gwt.client.model.ZipRecord::getFilename()(), data);
				    					callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(ap);
								    	if (readFunc.close!=null)
									    	readFunc.close(function () {});
									},
									function (currentProgress, totalProgress) {
									
									});
	}-*/;
	
	//TODO Zip needs updated to work
	public static final native JavaScriptObject getZipFileWriter(AsyncCallback<Vector<ZipRecord>> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		// use a BlobWriter to store the zip into a Blob object
		zip.createWriter(new zip.BlobWriter(), 
						 function(writer) {
							var ap = {};
							ap["jsObject"] = {};
							ap["jsObject"]["zipWriter"] = writer;
							callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(ap);
						 }, 
						 function(error) {
  							// onerror callback
						 });
	}-*/;

	//TODO Zip needs updated to work
	public static final native void addFileToZipBlob(JavaScriptObject zipWriter, String filename, Blob filedata, AsyncCallback<Vector<ZipRecord>> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		zipWriter.add(filename, 
				      new zip.BlobReader(filedata), 
				      function () {
				      	var ap = {};
				      	ap["jsObject"] = {};
				      	ap["jsObject"]["zipWriter"] = zipWriter;
				      	callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(ap);
				      },
				      function() {
				      	//progress
				      }); 
	}-*/;
	
	//TODO Zip needs updated to work
	public static final native void getZipBlobLocalURL(JavaScriptObject zipWriter, AsyncCallback<Vector<ZipRecord>> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		var createObjURL = window.webkitURL.createObjectURL || window.URL.createObjectURL;
		
		zipWriter.close(function(blob) {
							var ap = {};
							ap["jsObject"] = {};
							ap["jsObject"]["zipURL"] = createObjURL(blob);
							callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(ap);
						});
	}-*/;
	
	//TODO Zip needs updated to work
	public static final native void getZipBlob(JavaScriptObject zipWriter, AsyncCallback<Vector<ZipRecord>> callback) /*-{
		var zip = $wnd.zip;
		zip.workerScriptsPath = "js/";
		
		zipWriter.close(function(blob) {
							var ap = {};
							ap["jsObject"] = {};
							ap["jsObject"]["zipBlob"] = blob;
							callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(ap);
						});
	}-*/;
	
	//TODO Zip needs updated to work
	public static final native Vector<ZipRecord> getZipObject(Blob b) /*-{
		var acc = null; 
		var unzip = new $wnd.JSUnzip(b);
		if (unzip.isZipFile()) {
			unzip.readEntries();
			acc = unzip.entries;
		}
		return acc;
	}-*/;
	
	//TODO Zip needs updated to work
	public static final native String getZipEntryFilename(ZipRecord obj) /*-{
		var acc = ""
		if (obj!=null&&obj.fileName!=null)
			if (obj.fileName.indexOf("/")!=-1) acc = obj.fileName.substring(obj.fileName.lastIndexOf("/")+1);
			else acc = obj.fileName;
		return acc;
	}-*/;
	
}
