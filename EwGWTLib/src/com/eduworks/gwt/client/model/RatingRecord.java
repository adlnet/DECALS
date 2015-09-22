package com.eduworks.gwt.client.model;

import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.google.gwt.core.client.JsDate;

public class RatingRecord extends Record {
	public final static String CREATE_DATE = "createDate";
	public final static String CREATED_BY = "createdBy";
	public final static String FILE_GUID = "fileGuid";
	public final static String RATING = "rating";
	public final static String UPDATED_BY = "updatedBy";
	public final static String UPDATE_DATE = "updateDate";
	
	private JsDate createDate = null;
	private JsDate updateDate = null;
	private String createdBy = "";
	private String updatedBy = "";
	private int rating = 0;
	private String fileGuid = "";
	
	public RatingRecord() {}
	
	public RatingRecord(ESBPacket commentRecord) {
		parseESBPacket(commentRecord);
	}
	
	public void parseESBPacket(ESBPacket esbPacket) {
		if (esbPacket.containsKey(RATING))
			rating = esbPacket.getInteger(RATING);
		if (esbPacket.containsKey(CREATE_DATE))
			createDate = JsDate.create(JsDate.parse(esbPacket.getString(CREATE_DATE)));
		if (esbPacket.containsKey(CREATED_BY))
			createdBy = esbPacket.getString(CREATED_BY);
		if (esbPacket.containsKey(ID))
			guid = esbPacket.getString(ID);
		if (esbPacket.containsKey(UPDATE_DATE)) 
			updateDate = JsDate.create(JsDate.parse(esbPacket.getString(UPDATE_DATE)));
		if (esbPacket.containsKey(UPDATED_BY))
			updatedBy = esbPacket.getString(UPDATED_BY);
		if (esbPacket.containsKey(FILE_GUID))
			fileGuid = esbPacket.getString(FILE_GUID);
	}

	public JsDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(JsDate createDate) {
		this.createDate = createDate;
	}

	public JsDate getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(JsDate updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFileGuid() {
		return fileGuid;
	}

	public void setFileGuid(String fileGuid) {
		this.fileGuid = fileGuid;
	}
}
