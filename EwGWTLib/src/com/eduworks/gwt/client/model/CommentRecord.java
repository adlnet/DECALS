package com.eduworks.gwt.client.model;

import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.google.gwt.core.client.JsDate;

public class CommentRecord extends Record {
	public final static String COMMENT = "comment";
	public final static String CREATE_DATE = "createDate";
	public final static String CREATED_BY = "createdBy";
	public final static String FILE_GUID = "fileGuid";
	public final static String UPDATE_DATE = "updateDate";
	public final static String UPDATED_BY = "updatedBy";
	
	private String comment = "";
	private JsDate createDate = null;
	private String createdBy = "";
	private JsDate updateDate = null;
	private String updatedBy = "";
	private String fileGuid = "";
	
	public CommentRecord() {}
	
	public CommentRecord(ESBPacket commentRecord) {
		parseESBPacket(commentRecord);
	}
	
	public void parseESBPacket(ESBPacket esbPacket) {
		if (esbPacket.containsKey(COMMENT))
			comment = esbPacket.getString(COMMENT);
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
	
	public String getComment() {
		return comment;
	}

	public String getCreatedBy() {
		return createdBy;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updateBy) {
		this.updatedBy = updateBy;
	}

	public String getFileGuid() {
		return fileGuid;
	}

	public void setFileGuid(String fileGuid) {
		this.fileGuid = fileGuid;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
