package com.eduworks.decals.ui.client.model;

/**
 * Represents an item from the DECALS application repository (DAR)
 * 
 * @author Eduworks Corporation
 *
 */
public class DecalsApplicationRepositoryRecord extends DarResourceMetadata {
   
   public static enum ResourceType{FILE,URL}
   
   private static final int SHORT_DESC_LENGTH = 215;
   public final static int MAX_URL_DISPLAY_LENGTH = 100;
   
   public static final String UPLOADED_BY_KEY = "uploadedBy_t";
   public static final String FILESIZE_KEY = "fileSizeBytes_l";
   public static final String UPLOAD_DT_KEY = "uploadDate_l";
   public static final String UPDATED_DT_KEY = "updatedDate_l";
   public static final String TYPE_KEY = "type_t";
   public static final String FILENAME_KEY = "fileName_t";
   public static final String MIME_TYPE_KEY = "mimeType_t";
   public static final String URL_KEY = "url_t";
   public static final String LR_DOC_ID = "lrDocId_t";
   public static final String LR_PARADATA_ID = "lrParadataId_t";
   public static final String LR_PUBLISH_DATE = "lrPublishDate_l";
   
   public static final String PUBLISHED_KEY = "published_t";
   public static final String OLD_TITLE_KEY = "old_title_t";
   
   public static final String COMPETENCY_LIST_KEY = "competencies_txt";
   
   private String uploadedBy;
   private long fileSizeBytes;
   private String uploadDateStr;
   private String updateDateStr;
   private long pureUploadDate;
   private long pureUpdateDate;
   private ResourceType type;
   private String mimeType;
   private String url;
   private String fileName;
   private String lrDocId;
   private String lrParadataId;
   private String lrPublishDateStr;
   private long pureLrPublishDate;
   
   private String published;
   private String oldTitle;
   
   //TODO need to implement version...maybe
   private int version = 1;
   
   /** 
    * Enumeration containing the following possible basic mime types of this repository item
    * 
    * DEFAULT, WEBPAGE, TEXTPLAIN, HTML, AUDIO, IMAGE, VIDEO, RTF, XML, BINARY, PDF, FLASH, ZIP, JAVASCRIPT, MSOFFICE, MSWORD, MSPP, MSEXCEL, JAVA
    */
   public enum BasicMimeType {DEFAULT,WEBPAGE,TEXTPLAIN,HTML,AUDIO,IMAGE,VIDEO,RTF,XML,BINARY,PDF,FLASH,ZIP,JAVASCRIPT,MSOFFICE,MSWORD,MSPP,MSEXCEL,JAVA}
   
   /**
    * Returns this item's basic mime type as defined in the enumeration {@link DecalsApplicationRepositoryRecord.BasicMimeType}.
    * 
    * @return Returns this item's basic mime type as defined in the enumeration {@link DecalsApplicationRepositoryRecord.BasicMimeType}.
    */
   public BasicMimeType getBasicMimeType() {
      if (isUrlType()) return BasicMimeType.WEBPAGE;
      if (getMimeType().equalsIgnoreCase("text/plain")) return BasicMimeType.TEXTPLAIN;
      if (getMimeType().equalsIgnoreCase("text/html")) return BasicMimeType.HTML;
      if (getMimeType().toLowerCase().startsWith("audio")) return BasicMimeType.AUDIO;
      if (getMimeType().toLowerCase().startsWith("image")) return BasicMimeType.IMAGE;
      if (getMimeType().toLowerCase().startsWith("video")) return BasicMimeType.VIDEO;
      if (getMimeType().equalsIgnoreCase("application/rtf") || getMimeType().equalsIgnoreCase("text/richtext")) return BasicMimeType.RTF;
      if (getMimeType().equalsIgnoreCase("text/xml") || getMimeType().equalsIgnoreCase("application/xhtml+xml")) return BasicMimeType.XML;
      if (getMimeType().equalsIgnoreCase("application/octet-stream") || getMimeType().equalsIgnoreCase("application/x-ms-dos-executable")) return BasicMimeType.BINARY;
      if (getMimeType().equalsIgnoreCase("application/pdf")) return BasicMimeType.PDF;      
      if (getMimeType().equalsIgnoreCase("application/x-shockwave-flash")) return BasicMimeType.FLASH;
      if (getMimeType().equalsIgnoreCase("application/x-rar-compressed") || getMimeType().equalsIgnoreCase("application/zip")) return BasicMimeType.ZIP;      
      if (getMimeType().equalsIgnoreCase("text/javascript") || getMimeType().equalsIgnoreCase("application/x-javascript")) return BasicMimeType.JAVASCRIPT;
      if (getMimeType().equalsIgnoreCase("application/vnd.ms-office")) return BasicMimeType.MSOFFICE;      
      if (getMimeType().equalsIgnoreCase("application/vnd.ms-word")) return BasicMimeType.MSWORD;
      if (getMimeType().equalsIgnoreCase("application/vnd.ms-powerpoint")) return BasicMimeType.MSPP;
      if (getMimeType().equalsIgnoreCase("application/vnd.ms-excel")) return BasicMimeType.MSEXCEL;
      if (getMimeType().equalsIgnoreCase("text/java") || getMimeType().equalsIgnoreCase("application/java-archive")) return BasicMimeType.JAVA;
      return BasicMimeType.DEFAULT;
   }
   
   /**
    * Determines if automatic metadata generation should be available for this record.
    * 
    * @return Returns true if automatic metadata should be available.  Returns false otherwise.
    */
   public boolean canAutoGenerateMetadata() {
      boolean canGen = false;
      switch(getBasicMimeType()) {      
         case DEFAULT: case WEBPAGE: case TEXTPLAIN: case HTML: case RTF: case PDF: case MSOFFICE: case MSWORD: case MSPP:
            canGen = true; break;
         case AUDIO: case IMAGE: case VIDEO: case XML: case BINARY: case FLASH: case ZIP: case JAVASCRIPT: case MSEXCEL: case JAVA:
            canGen = false; break;
         default: canGen = false; break;
      }
      return canGen;      
   }
   
   /**
    * Determines if LR publishing should be available for this record.
    * 
    * @return Returns true if LR publishing should be available.  Returns false otherwise.
    */
   public boolean canPublishToLr() {
      if (getDescription() == null || getDescription().trim().isEmpty() || published.equals("true") || getTitle().equals(oldTitle)) return false;
      return true;      
   }
   
   /**
    * Returns true if the resource is a file type.  Returns false otherwise.
    * 
    * @return Returns true if the resource is a file type.  Returns false otherwise.
    */
   public boolean isFileType() {return type.equals(ResourceType.FILE);}
   
   /**
    * Returns true if the resource is a URL type.  Returns false otherwise.
    * 
    * @return Returns true if the resource is a URL type.  Returns false otherwise.
    */
   public boolean isUrlType() {return type.equals(ResourceType.URL);}
   
   /**
    * {@link DecalsApplicationRepositoryRecord#fileName}
    */
   public String getFileName() {return fileName;}
   public void setFileName(String fileName) {this.fileName = fileName;}  
   
   /**
    * {@link DecalsApplicationRepositoryRecord#uploadedBy}
    */
   public String getUploadedBy() {return uploadedBy;}
   public void setUploadedBy(String uploadedBy) {this.uploadedBy = uploadedBy;}

   /**
    * {@link DecalsApplicationRepositoryRecord#fileSizeBytes}
    */
   public long getFileSizeBytes() {return fileSizeBytes;}
   public void setFileSizeBytes(long fileSizeBytes) {this.fileSizeBytes = fileSizeBytes;}

   /**
    * {@link DecalsApplicationRepositoryRecord#uploadDateStr}
    */
   public String getUploadDateStr() {return uploadDateStr;}
   public void setUploadDateStr(String uploadDateStr) {this.uploadDateStr = uploadDateStr;}

   /**
    * {@link DecalsApplicationRepositoryRecord#updateDateStr}
    */
   public String getUpdateDateStr() {return updateDateStr;}
   public void setUpdateDateStr(String updateDateStr) {this.updateDateStr = updateDateStr;}

   /**
    * {@link DecalsApplicationRepositoryRecord#type}
    */
   public ResourceType getType() {return type;}
   public void setType(ResourceType type) {this.type = type;}

   /**
    * {@link DecalsApplicationRepositoryRecord#mimeType}
    */
   public String getMimeType() {return mimeType;}
   public void setMimeType(String mimeType) {this.mimeType = mimeType;}
   
   /**
    * {@link DecalsApplicationRepositoryRecord#url}
    */
   public String getUrl() {return url;}
   public void setUrl(String url) {this.url = url;}

   /**
    * {@link DecalsApplicationRepositoryRecord#lrDocId}
    */
   public String getLrDocId() {return lrDocId;}
   public void setLrDocId(String lrDocId) {this.lrDocId = lrDocId;}

   /**
    * {@link DecalsApplicationRepositoryRecord#lrParadataId}
    */
   public String getLrParadataId() {return lrParadataId;}
   public void setLrParadataId(String lrParadataId) {this.lrParadataId = lrParadataId;}
   
   /**
    * {@link DecalsApplicationRepositoryRecord#version}
    */
   public int getVersion() {return version;}
   public void setVersion(int version) {this.version = version;}

   /**
    * {@link DecalsApplicationRepositoryRecord#pureUploadDate}
    */
   public long getPureUploadDate() {return pureUploadDate;}
   public void setPureUploadDate(long pureUploadDate) {this.pureUploadDate = pureUploadDate;}

   /**
    * {@link DecalsApplicationRepositoryRecord#pureUpdateDate}
    */
   public long getPureUpdateDate() {return pureUpdateDate;}
   public void setPureUpdateDate(long pureUpdateDate) {this.pureUpdateDate = pureUpdateDate;}

   /**
    * {@link DecalsApplicationRepositoryRecord#lrPublishDateStr}
    */
   public String getLrPublishDateStr() {return lrPublishDateStr;}
   public void setLrPublishDateStr(String lrPublishDateStr) {this.lrPublishDateStr = lrPublishDateStr;}

   /**
    * {@link DecalsApplicationRepositoryRecord#pureLrPublishDate}
    */
   public long getPureLrPublishDate() {return pureLrPublishDate;}
   public void setPureLrPublishDate(long pureLrPublishDate) {this.pureLrPublishDate = pureLrPublishDate;}
  
   
   public String getPublished() { return published; }
   public void setPublished(String published) { this.published = published; }
   
   public void setOldTitle(String oldTitle) { this.oldTitle = oldTitle; }
   
   
   /**
    * Returns a truncated string of the resource URL.
    * 
    * @return Returns a truncated string of the resource URL. 
    */   
   public String getTruncatedUrl() {
      if (getUrl() == null || getUrl().trim().isEmpty()) return "";
      if (getUrl().length() <= MAX_URL_DISPLAY_LENGTH) return getUrl();
      return getUrl().substring(0,MAX_URL_DISPLAY_LENGTH) + "...";
   }
   
   /**
    * Returns a truncated version of the description.
    * 
    * @return Returns a truncated version of the description.
    */   
   public String getShortDescription() {
      if (getDescription().length() > SHORT_DESC_LENGTH) return getDescription().substring(0, SHORT_DESC_LENGTH) + "...";
      else return getDescription();
   }
   
   /**
    * Returns true if the resource has a long description.  Returns false otherwise.
    * 
    * @return  Returns true if the resource has a long description.  Returns false otherwise.
    */   
   public boolean hasLongDescription() {return (getDescription().length() > SHORT_DESC_LENGTH)?true:false;}
   
   /**
    * Returns true if the resource has been published to the LR.  Returns false otherwise.
    * 
    * @return  Returns true if the resource has been published to the LR.  Returns false otherwise.
    */
   public boolean hasBeenPubishedToLr() {
      if (getLrDocId() == null || getLrDocId().trim().isEmpty()) return false;
      return true;
   }
   
}
