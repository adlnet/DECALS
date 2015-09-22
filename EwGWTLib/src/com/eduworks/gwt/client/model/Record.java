package com.eduworks.gwt.client.model;

public class Record {
	public final static String ID = "id";
	
	protected String guid = "";
	
	public void setGuid(String guid) {
		this.guid = guid; 
	}
	
	public String getGuid() {
		return guid;
	}
}
