package com.eduworks.gwt.client.model;

import java.util.Vector;

import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.google.gwt.core.client.JsDate;

public class ThreeDRRecord extends FileRecord {
	public static String FEEDBACK = "feedback";
	
	public String feedback = "";

	public ThreeDRRecord () {
		
	}
	
	public ThreeDRRecord (ESBPacket esbPacket) {
		super.parseESBPacket(esbPacket);
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}


	public final native static String getADLdate(JsDate date) /*-{
		var fixed = (date.getMonth()+1) + "/"+date.getDate()+"/"+date.getFullYear()+" "+date.getHours()+":"+date.getMinutes()+":00";
		return fixed;
	}-*/;
	
	private final native String getId0() /*-{ 
		var acc = "";
		if (this.PID!=null)
			acc = this.PID;
		return acc;
	}-*/;
	
	private final native String getTitle0() /*-{ 
		var acc="";
		if (this.Title!=null)
			acc = this.Title;
		return acc; 
	}-*/;
	
	private final native String getDataLink0() /*-{ 
		var acc="";
		if (this.DataLink!=null)
			acc = this.DataLink;
		return acc; 
	}-*/;
	
	private final native String getDescription0() /*-{ 
		var acc="";
		if (this.Description!=null)
			acc = this.Description;
		return acc; 
	}-*/;
	
	private final native String getFormat0() /*-{ 
		var acc="";
		if (this.Format!=null)
			acc = this.Format;
		return acc; 
	}-*/;
	
	private final native String getKeywords0() /*-{ 
		var acc="";
		if (this.Keywords!=null)
			acc = this.Keywords;
		return acc; 
	}-*/;
	
	private final native String getOwner0() /*-{ 
		var acc="";
		if (this.DeveloperName!=null && this.DeveloperName != "")
			acc = "Developer: "+this.DeveloperName;
		if (this.ArtistName != null && this.ArtistName != "" && acc != "")
			acc += " / Artist: "+this.ArtistName;
		else if (this.ArtistName != null && this.ArtistName != "")
			acc = "Artist: "+this.ArtistName;
		return acc; 
	}-*/;
	
	private final native String getVersion0() /*-{ 
		var acc="";
		if (this.Revision!=null)
			acc = this.Revision;
		return acc; 
	}-*/;
	
	private final native String getPublisher0() /*-{ 
		var acc="";
		if (this.SponsorName!=null)
			acc = this.SponsorName;
		return acc; 
	}-*/;
	
	private final native String getCreateDate0() /*-{ 
		var acc="";
		if (this.UploadedDate!=null)
			acc = this.UploadedDate;
		return acc; 
	}-*/;
	
	private final native String getThumbnail0() /*-{ 
		var acc="";
		if (this._ThumbnailLink!=null)
			acc = this._ThumbnailLink;
		return acc; 
	}-*/;
	
	private final native String getScreenshot0() /*-{ 
		var acc="";
		if (this._ScreenshotLink!=null)
			acc = this._ScreenshotLink;
		return acc; 
	}-*/;
	
	private final native String getComment0() /*-{ 
		var acc="";
		if (this.ReviewText!=null && this.ReviewText != "")
			acc = this.ReviewText;
		return acc; 
	}-*/;

	// Adl3DRPacket public methods

	public final native void setMimeType(String mimeType) /*-{
		this.mimeType = mimeType;
	}-*/;
	
	
	public final String getDataLink() {
		String acc = getDataLink0();
		return acc;
	}
	
	public final String getComment() {
		String acc = getComment0();
		return acc;
	}

	public Vector<ThreeDRRecord> getSearchRecords() {
		// TODO Auto-generated method stub
		return null;
	}

}
