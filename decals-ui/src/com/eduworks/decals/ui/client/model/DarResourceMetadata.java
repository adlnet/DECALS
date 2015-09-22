package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.gwt.client.net.packet.ESBPacket;

/**
 * Handles the metadata of a DECALS application repository item
 * 
 * @author Eduworks Corporation
 *
 */
public class DarResourceMetadata {
   
   public static final String ID_KEY = "id";
   public static final String TITLE_KEY = "title_t";   
   public static final String DESC_KEY = "description_t";
   public static final String KEYWORDS_KEY = "keywords_txt";
   public static final String OBJECTIVES_KEY = "objectives_txt";
   public static final String CLASSIFICATION_KEY = "classification_t";
   public static final String SEC_LEVEL_KEY = "sec_level_t";
   public static final String DISTR_KEY = "distribution_t";
   public static final String PUBLISHER_KEY = "publisher_t";
   public static final String OWNER_KEY = "owner_t";
   public static final String COVERAGE_KEY = "coverage_t";
   public static final String INTERACT_KEY = "interact_t";
   public static final String ENVIRON_KEY = "environment_t";
   public static final String SKILL_LEVEL_KEY = "skill_level_t";
   public static final String LANGUAGE_KEY = "language_t";
   public static final String DURATION_KEY = "duration_f";
   public static final String TECH_REQS_KEY = "tech_reqs_t";
   public static final String IS_PART_OF_KEY = "is_part_of_t";
   public static final String REQUIRES_KEY = "requires_t";
   
   private String id;
   private String description;
   private String title;
   private ArrayList<String> keywords;
   private ArrayList<DarResourceObjective> objectives;
   private String classification;
   private String securityLevel;
   private String distribution;
   private String publisher;
   private String owner;
   private String coverage;
   private String interactivity;
   private String environment;
   private String skillLevel;
   private String language;
   private long duration;
   private String techRequirements;
   private String isPartOf;
   private String requires;
   private ArrayList<String> competencyIds;
   private ArrayList<DarResourceCompetency> competencyList;
   
   public DarResourceMetadata() {}
   
   public DarResourceMetadata(String id) {this.id = id;}
      
   /**
    * Generates a metadata pay load packet
    * 
    * @return Returns the generated pay load packet
    */
   public ESBPacket generateMetadataPayloadPacket() {
      ESBPacket jo = new ESBPacket(); 
      jo.put(ID_KEY,id);
      if(description != null && !description.trim().isEmpty()) jo.put(DESC_KEY,description);
      if(title != null && !title.trim().isEmpty()) jo.put(TITLE_KEY,title);
      //if(keywords != null && keywords.size() > 0) jo.put(KEYWORDS_KEY,DsUtil.buildJsonArrayStringFromStringList(keywords));
      //if(objectives != null && objectives.size() > 0) jo.put(OBJECTIVES_KEY,DarResourceObjective.buildJsonArrayStringFromObjectiveList(objectives));
      if(classification != null && !classification.trim().isEmpty()) jo.put(CLASSIFICATION_KEY,classification);
      if(securityLevel != null && !securityLevel.trim().isEmpty()) jo.put(SEC_LEVEL_KEY,securityLevel);
      if(distribution != null && !distribution.trim().isEmpty()) jo.put(DISTR_KEY,distribution);
      if(publisher != null && !publisher.trim().isEmpty()) jo.put(PUBLISHER_KEY,publisher);
      if(owner != null && !owner.trim().isEmpty()) jo.put(OWNER_KEY,owner);
      if(coverage != null && !coverage.trim().isEmpty()) jo.put(COVERAGE_KEY,coverage);
      if(interactivity != null && !interactivity.trim().isEmpty()) jo.put(INTERACT_KEY,interactivity);
      if(environment != null && !environment.trim().isEmpty()) jo.put(ENVIRON_KEY,environment);
      if(skillLevel != null && !skillLevel.trim().isEmpty()) jo.put(SKILL_LEVEL_KEY,skillLevel);
      if(language != null && !language.trim().isEmpty()) jo.put(LANGUAGE_KEY,language);
      if(duration > 0) jo.put(DURATION_KEY,duration);
      if(techRequirements != null && !techRequirements.trim().isEmpty()) jo.put(TECH_REQS_KEY,techRequirements);
      if(isPartOf != null && !isPartOf.trim().isEmpty()) jo.put(IS_PART_OF_KEY,isPartOf);
      if(requires != null && !requires.trim().isEmpty()) jo.put(REQUIRES_KEY,requires);
      
      //these need to be sent even if they are empty or else they can never be cleared...probably true for all the values :/     
      jo.put(KEYWORDS_KEY,DsUtil.buildJsonArrayFromStringList(keywords));
      jo.put(OBJECTIVES_KEY,DarResourceObjective.buildJsonArrayFromObjectiveList(objectives));
      
      jo.put("competencies_txt", DarResourceCompetency.buildJsonArrayFromCompetencyList(competencyList));
      jo.put("competencyIds_txt",  DsUtil.buildJsonArrayFromStringList(competencyIds));
      
      return jo;
   }

   /**
    * {@link DarResourceMetadata#id}
    */
   public String getId() {return id;}
   public void setId(String id) {this.id = id;}

   /**
    * {@link DarResourceMetadata#description}
    */
   public String getDescription() {return description;}
   public void setDescription(String description) {this.description = description;}
   
   /**
    * {@link DarResourceMetadata#title}
    */
   public String getTitle() {return title;}
   public void setTitle(String title) {this.title = title;}
   
   /**
    * {@link DarResourceMetadata#keywords}
    */
   public ArrayList<String> getKeywords() {return keywords;}
   public void setKeywords(ArrayList<String> keywords) {this.keywords = keywords;}

   /**
    * {@link DarResourceMetadata#objectives}
    */
   public ArrayList<DarResourceObjective> getObjectives() {return objectives;}
   public void setObjectives(ArrayList<DarResourceObjective> objectives) {this.objectives = objectives;}

   /**
    * {@link DarResourceMetadata#classification}
    */
   public String getClassification() {return classification;}
   public void setClassification(String classification) {this.classification = classification;}

   /**
    * {@link DarResourceMetadata#securityLevel}
    */
   public String getSecurityLevel() {return securityLevel;}
   public void setSecurityLevel(String securityLevel) {this.securityLevel = securityLevel;}

   /**
    * {@link DarResourceMetadata#distribution}
    */
   public String getDistribution() {return distribution;}
   public void setDistribution(String distribution) {this.distribution = distribution;}

   /**
    * {@link DarResourceMetadata#publisher}
    */
   public String getPublisher() {return publisher;}
   public void setPublisher(String publisher) {this.publisher = publisher;}

   /**
    * {@link DarResourceMetadata#owner}
    */
   public String getOwner() {return owner;}
   public void setOwner(String owner) {this.owner = owner;}

   /**
    * {@link DarResourceMetadata#coverage}
    */
   public String getCoverage() {return coverage;}
   public void setCoverage(String coverage) {this.coverage = coverage;}

   /**
    * {@link DarResourceMetadata#interactivity}
    */
   public String getInteractivity() {return interactivity;}
   public void setInteractivity(String interactivity) {this.interactivity = interactivity;}

   /**
    * {@link DarResourceMetadata#environment}
    */
   public String getEnvironment() {return environment;}
   public void setEnvironment(String environment) {this.environment = environment;}

   /**
    * {@link DarResourceMetadata#skillLevel}
    */
   public String getSkillLevel() {return skillLevel;}
   public void setSkillLevel(String skillLevel) {this.skillLevel = skillLevel;}

   /**
    * {@link DarResourceMetadata#language}
    */
   public String getLanguage() {return language;}
   public void setLanguage(String language) {this.language = language;}

   /**
    * {@link DarResourceMetadata#duration}
    */
   public long getDuration() {return duration;}
   public void setDuration(long duration) {this.duration = duration;}

   /**
    * {@link DarResourceMetadata#techRequirements}
    */
   public String getTechRequirements() {return techRequirements;}
   public void setTechRequirements(String techRequirements) {this.techRequirements = techRequirements;}

   /**
    * {@link DarResourceMetadata#isPartOf}
    */
   public String getIsPartOf() {return isPartOf;}
   public void setIsPartOf(String isPartOf) {this.isPartOf = isPartOf;}

   /**
    * {@link DarResourceMetadata#requires}
    */
   public String getRequires() {return requires;}
   public void setRequires(String requires) {this.requires = requires;}
   
   public ArrayList<DarResourceCompetency> getCompetencyList() { return competencyList; }
   public void setCompetencyList(ArrayList<DarResourceCompetency> competencyList) { this.competencyList = competencyList; }
   
   public ArrayList<String> getCompetencyIds() { return competencyIds; }
   public void setCompetencyIds(ArrayList<String> competencyIds) { this.competencyIds = competencyIds; }
   
   

}
