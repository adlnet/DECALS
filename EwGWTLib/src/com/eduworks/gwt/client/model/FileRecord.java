package com.eduworks.gwt.client.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.util.MathUtil;

public abstract class FileRecord extends Record {
	public final static String FILENAME = "fileName_t";
	public final static String DESCRIPTION = "description_t";
	public final static String MIMETYPE = "mimeType_t";
	public final static String TITLE = "title_t";
	public final static String CREATED_BY = "createdBy_t";
	public final static String PUBLISHER = "publisher_t";
	public final static String CLASSIFICATION = "classification_t";
	public final static String ENVIRONMENT = "environment_t";
	public final static String COVERAGE = "coverage_t";
	public final static String LANGUAGE = "language_t";
	public final static String TECHNICAL_REQUIREMENTS = "technicalRequirements_t";
	public final static String DISTRIBUTION = "distribution_t";
	public final static String VERSION = "version_t";
	public final static String THUMBNAIL = "thumbnail_t";
	public final static String PART_OF = "partOf_t";
	public final static String REQUIRES = "requires_t";
	public final static String OWNER = "uploadedBy_t";
	public final static String GROUPS = "sharedBy_t";
	public final static String INTERACTIVITY = "interactivity_t";
	public final static String LEVEL = "level_t";
	public final static String KEYWORDS = "keywords_t";
	public final static String OBJECTIVES = "objective_t";
	public final static String FILE_CONTENT = "fileContent_t";
	public final static String FOUO = "fouo_b";
	public final static String COMMENTS = "comments";
	public final static String RATINGS = "ratings";
	public final static String SKILL = "skill_t";
	public final static String VIEW = "view_l";
	public final static String DOWNLOADS = "downloads_l";
	public final static String FILESIZE_BYTES = "fileSizeBytes_l";
	public final static String DURATION = "duration_f";
	public final static String UPDATED_DATE = "updatedDate_l";
	public final static String UPLOAD_DATE = "uploadDate_l";
	
	public String getFieldList() {
		return FILENAME + " " + DESCRIPTION + " " + MIMETYPE + " " + TITLE + " " + CREATED_BY + " " + PUBLISHER + " " + CLASSIFICATION + " " +
			   ENVIRONMENT + " " + COVERAGE + " " + LANGUAGE + " " + TECHNICAL_REQUIREMENTS + " " + DISTRIBUTION + " " + VERSION + " " + THUMBNAIL + " " +
			   PART_OF + " " + REQUIRES + " " + OWNER + " "+ GROUPS + " " + INTERACTIVITY + " " + LEVEL + " " + KEYWORDS + " " + OBJECTIVES + " " + FILE_CONTENT + " " +
			   FOUO + " " + SKILL + " " + VIEW + " " + DOWNLOADS + " " + FILESIZE_BYTES + " " + DURATION + " " + UPLOAD_DATE + " " + UPDATED_DATE;
	}
	
	private String filename = "";
	private String description = "";
	private String mimeType = "";
	private String title = "";
	private String createdBy = "";
	private String publisher = "";
	private String classification = "";
	private String activity = "";
	private String environment = "";
	private String coverage = "";
	private String language = "";
	private String technicalRequirements = "";
	private String distribution = "";
	private String version = "";
	private String thumbnail = "";
	private String skill = "";
	private String partOf = "";
	private String requires = "";
	private String owner = "";
	private String groups = "";
	private String interactivity = "";
	private String level = "";
	private String keywords = "";
	private String updatedDateStr = "";
	private String objectives = "";
	private String fileContents = "";
	private String uploadDateStr = "";
	private Boolean fouo = false;
	private HashMap<String, CommentRecord> comments = new HashMap<String, CommentRecord>();
	private HashMap<String, RatingRecord> ratings = new HashMap<String, RatingRecord>();
	private int view = 0;
	private int downloads = 0;
	private int filesize = 0;
	private double duration = 0.0;
	private double ratingAverage = 2.5;
	
	public FileRecord() {
		
	}	
	
	public FileRecord(ESBPacket metaDataPack) {
		parseESBPacket(metaDataPack);
	}
	
	public void parseESBPacket(ESBPacket metaDataPack) {
		ESBPacket esbPacket;
		if (metaDataPack.containsKey("obj"))
			esbPacket = new ESBPacket(metaDataPack.get("obj").isObject());
		else
			esbPacket = metaDataPack;
		if (esbPacket.containsKey(FILENAME))
			filename = esbPacket.getString(FILENAME);
		if (esbPacket.containsKey(ID))
			guid = esbPacket.getString(ID);
		if (esbPacket.containsKey(DESCRIPTION))
			description = esbPacket.getString(DESCRIPTION);
		if (esbPacket.containsKey(MIMETYPE))
			mimeType = esbPacket.getString(MIMETYPE);
		if (esbPacket.containsKey(TITLE))
			title = esbPacket.getString(TITLE);
		if (esbPacket.containsKey(CREATED_BY))
			createdBy = esbPacket.getString(CREATED_BY);
		if (esbPacket.containsKey(PUBLISHER))
			publisher = esbPacket.getString(PUBLISHER);
		if (esbPacket.containsKey(CLASSIFICATION))
			classification = esbPacket.getString(CLASSIFICATION);
		if (esbPacket.containsKey(ENVIRONMENT))
			environment = esbPacket.getString(ENVIRONMENT);
		if (esbPacket.containsKey(COVERAGE))
			coverage = esbPacket.getString(COVERAGE);
		if (esbPacket.containsKey(LANGUAGE))
			language = esbPacket.getString(LANGUAGE);
		if (esbPacket.containsKey(TECHNICAL_REQUIREMENTS))
			technicalRequirements = esbPacket.getString(TECHNICAL_REQUIREMENTS);
		if (esbPacket.containsKey(DISTRIBUTION))
			distribution = esbPacket.getString(DISTRIBUTION);
		if (esbPacket.containsKey(VERSION))
			version = esbPacket.getString(VERSION);
		if (esbPacket.containsKey(THUMBNAIL))
			thumbnail = esbPacket.getString(THUMBNAIL);
		if (esbPacket.containsKey(PART_OF))
			partOf = esbPacket.getString(PART_OF);
		if (esbPacket.containsKey(REQUIRES))
			requires = esbPacket.getString(REQUIRES);
		if (esbPacket.containsKey(OWNER))
			owner = esbPacket.getString(OWNER);
		if (esbPacket.containsKey(GROUPS))
			groups = esbPacket.getString(GROUPS);
		if (esbPacket.containsKey(INTERACTIVITY))
			interactivity = esbPacket.getString(INTERACTIVITY);
		if (esbPacket.containsKey(LEVEL))
			level = esbPacket.getString(LEVEL);
		if (esbPacket.containsKey(KEYWORDS))
			keywords = esbPacket.getString(KEYWORDS);
		if (esbPacket.containsKey(OBJECTIVES))
			objectives = esbPacket.getString(OBJECTIVES);
		if (esbPacket.containsKey(SKILL))
			skill = esbPacket.getString(SKILL);
		if (esbPacket.containsKey(UPLOAD_DATE))
			uploadDateStr = esbPacket.getString(UPLOAD_DATE);
		if (esbPacket.containsKey(UPDATED_DATE))
	         setUpdatedDateStr(esbPacket.getString(UPDATED_DATE));
		if (esbPacket.containsKey(FOUO)) {
			if (esbPacket.get(FOUO).isBoolean()!=null)
				fouo = esbPacket.getBoolean(FOUO);
			else if (esbPacket.get(FOUO).isString()!=null)
				fouo = Boolean.parseBoolean(esbPacket.getString(FOUO));
		}
		if (esbPacket.containsKey(FILESIZE_BYTES)) {
			if (esbPacket.get(FILESIZE_BYTES).isNumber()!=null)
				filesize = esbPacket.getInteger(FILESIZE_BYTES);
			else if (esbPacket.get(FILESIZE_BYTES).isString()!=null)
				filesize = Integer.valueOf(esbPacket.getString(FILESIZE_BYTES));
		}
		if (esbPacket.containsKey(DURATION)) {
			if (esbPacket.get(DURATION).isNumber()!=null)
				duration = esbPacket.getDouble(DURATION);
			else if (esbPacket.get(DURATION).isString()!=null)
				duration = Double.valueOf(esbPacket.getString(DURATION));
		}
	}
	

	public String getUploadDateStr() {
	   return uploadDateStr;
	}
	
	public void setUploadDateStr(String dateStr) {
		uploadDateStr = dateStr;
	}
	
	/**
	 *Levr stores the date as a long I believe; 
	 */
	//TODO verify this
	public Date getUploadDate() {
	   try {
	      Date d = new Date();
	      d.setTime(Long.valueOf(getUploadDateStr()));
	      return d;
	   }
	   catch (Exception e) {
	      return null;
	   }
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGroups() {
		return groups;
	}
	
	public void setGroups(String groups) {
		this.groups = groups;
	}
	
	public String getInteractivity() {
		return interactivity;
	}

	public void setInteractivity(String interactivity) {
		this.interactivity = interactivity;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getRequires() {
		return requires;
	}

	public void setRequires(String requires) {
		this.requires = requires;
	}
	
	public String getPartOf() {
		return partOf;
	}

	public void setPartOf(String partOf) {
		this.partOf = partOf;
	}	
	
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getThumbnail() {
		return this.thumbnail;
	}
	
	public void setFOUO(Boolean fouo) {
		this.fouo = fouo;
	}
	
	public Boolean getFOUO() {
		return this.fouo;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setRating(double rating) {
		this.ratingAverage = rating;
	}
	
	public double getRating() {
		return this.ratingAverage;
	}
	
	public HashMap<String, CommentRecord> getComments() {
		return this.comments;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public String getMimeType() {
		return this.mimeType;
	}
	
	public void setCreateBy(String createBy) {
		this.createdBy = createBy;
	}
	
	public String getCreateBy() {
		return this.createdBy;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public int getDownloads() {
		return downloads;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public HashMap<String, RatingRecord> getRatings() {
		return ratings;
	}

	public void setRatings(HashMap<String, RatingRecord> ratings) {
		this.ratings = ratings;
	}
	
	public void addRatings(RatingRecord ratingRecord) {
		this.ratings.put(ratingRecord.getGuid(), ratingRecord);
	}
	
	public void parseRatings(ESBPacket esbPacket) {
		ESBPacket ratingsObject = new ESBPacket(esbPacket.getObject("obj"));
		Set<String> ratings = ratingsObject.keySet();
		int totalRatings = 0;
		for (Iterator<String> ratingPointer = ratings.iterator(); ratingPointer.hasNext();) {
			String ratingGuid = ratingPointer.next();
			RatingRecord rr = new RatingRecord(ratingsObject.getObject(ratingGuid));
			rr.setGuid(ratingGuid);
			totalRatings += rr.getRating();
			this.ratings.put(rr.getGuid(), rr);
		}
		if (ratings.size()>0)
			this.ratingAverage = MathUtil.roundNumber(totalRatings / ratings.size(), 2); 
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getTechnicalRequirements() {
		return technicalRequirements;
	}

	public void setTechnicalRequirements(String technicalRequirements) {
		this.technicalRequirements = technicalRequirements;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getFilesize() {
		return filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public void setComments(HashMap<String, CommentRecord> comments) {
		this.comments = comments;
	}
	
	public void addComments(CommentRecord commentRecord) {
		this.comments.put(commentRecord.getGuid(), commentRecord);
	}
	
	public void parseComments(ESBPacket esbPacket) {
		ESBPacket commentsObject = new ESBPacket(esbPacket.getObject("obj"));
		Set<String> comments = commentsObject.keySet();
		for (Iterator<String> commentPointer = comments.iterator(); commentPointer.hasNext();) {
			String commentGuid = commentPointer.next();
			CommentRecord cr = new CommentRecord(commentsObject.getObject(commentGuid));
			cr.setGuid(commentGuid);
			this.comments.put(cr.getGuid(), cr);
		}
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getFileContents() {
		return fileContents;
	}

	public void setFileContents(String fileContents) {
		this.fileContents = fileContents;
	}
	
	@Override
	public String toString() {
		ESBPacket esbPacket = new ESBPacket();
		esbPacket.put(FILENAME, filename);
		esbPacket.put(DESCRIPTION, description);
		esbPacket.put(MIMETYPE, mimeType);
		esbPacket.put(TITLE, title);
		esbPacket.put(CREATED_BY, createdBy);
		esbPacket.put(PUBLISHER, publisher);
		esbPacket.put(CLASSIFICATION, classification);
		esbPacket.put(ENVIRONMENT, environment);
		esbPacket.put(COVERAGE, coverage);
		esbPacket.put(LANGUAGE, language);
		esbPacket.put(TECHNICAL_REQUIREMENTS, technicalRequirements);
		esbPacket.put(DISTRIBUTION, distribution);
		esbPacket.put(VERSION, version);
		esbPacket.put(THUMBNAIL, thumbnail);
		esbPacket.put(PART_OF, partOf);
		esbPacket.put(REQUIRES, requires);
		esbPacket.put(OWNER, owner);
		esbPacket.put(GROUPS, groups);
		esbPacket.put(INTERACTIVITY, interactivity);
		esbPacket.put(LEVEL, level);
		esbPacket.put(KEYWORDS, keywords);
		esbPacket.put(OBJECTIVES, objectives);
		esbPacket.put(FILE_CONTENT, fileContents);
		esbPacket.put(FOUO, fouo);
		esbPacket.put(SKILL, skill);
		esbPacket.put(VIEW, view);
		esbPacket.put(DOWNLOADS, downloads);
		esbPacket.put(FILESIZE_BYTES,filesize);
		esbPacket.put(DURATION, duration);
		esbPacket.put(ID, guid);
		esbPacket.put(UPLOAD_DATE, uploadDateStr);
		esbPacket.put(UPDATED_DATE, updatedDateStr);
		return esbPacket.toString();
	}
	
	public ESBPacket toObject() {
		ESBPacket esbPacket = new ESBPacket();
		esbPacket.put(FILENAME, filename);
		esbPacket.put(DESCRIPTION, description);
		esbPacket.put(MIMETYPE, mimeType);
		esbPacket.put(TITLE, title);
		esbPacket.put(CREATED_BY, createdBy);
		esbPacket.put(PUBLISHER, publisher);
		esbPacket.put(CLASSIFICATION, classification);
		esbPacket.put(ENVIRONMENT, environment);
		esbPacket.put(COVERAGE, coverage);
		esbPacket.put(LANGUAGE, language);
		esbPacket.put(TECHNICAL_REQUIREMENTS, technicalRequirements);
		esbPacket.put(DISTRIBUTION, distribution);
		esbPacket.put(VERSION, version);
		esbPacket.put(THUMBNAIL, thumbnail);
		esbPacket.put(PART_OF, partOf);
		esbPacket.put(REQUIRES, requires);
		esbPacket.put(OWNER, owner);
		esbPacket.put(GROUPS, groups);
		esbPacket.put(INTERACTIVITY, interactivity);
		esbPacket.put(LEVEL, level);
		esbPacket.put(KEYWORDS, keywords);
		esbPacket.put(OBJECTIVES, objectives);
		esbPacket.put(FILE_CONTENT, fileContents);
		esbPacket.put(FOUO, fouo);
		esbPacket.put(SKILL, skill);
		esbPacket.put(VIEW, view);
		esbPacket.put(DOWNLOADS, downloads);
		esbPacket.put(FILESIZE_BYTES,filesize);
		esbPacket.put(ID, guid);
		esbPacket.put(DURATION, duration);
		esbPacket.put(UPLOAD_DATE, uploadDateStr);
		esbPacket.put(UPDATED_DATE, updatedDateStr);
		return esbPacket;
	}

	public String getUpdatedDateStr() {
		return updatedDateStr;
	}

	public void setUpdatedDateStr(String updatedDateStr) {
		this.updatedDateStr = updatedDateStr;
	}
}
