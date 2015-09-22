package com.eduworks.gwt.client.model;

import org.vectomatic.file.Blob;

public class ZipRecord extends Record {
	public static final String FILENAME = "filename";
	public static final String DATA = "data";
	
	private Blob data = null;
	private String filename = "";
	
	public ZipRecord() {
		
	}
	
	public ZipRecord(String filename, Blob data) {
		this.filename = filename;
		this.data = data;
	}
	
	public Blob getData() {
		return data;
	}
	public void setData(Blob data) {
		this.data = data;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
