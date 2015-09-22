package com.eduworks.decals.ui.client.util;

import com.eduworks.decals.ui.client.model.DecalsApplicationRepositoryRecord;
import com.eduworks.gwt.client.model.FLRRecord;

/**
 * Helper class for converting DECALS application repository records
 * 
 * @author Eduworks Corporation
 *
 */
public class DarConverter {
   
   /**
    * Converts a {@link com.eduworks.decals.ui.client.model.DecalsApplicationRepositoryRecord} to a {@link com.eduworks.gwt.client.model.FLRRecord}
    * 
    * @param darRecord The record to convert.
    * @return Returns the converted record.
    */
   public static final FLRRecord toFlrRecord(DecalsApplicationRepositoryRecord darRecord) {
      FLRRecord flrRecord = new FLRRecord();
      flrRecord.setFilename(darRecord.getFileName());
      flrRecord.setDescription(darRecord.getDescription());
      flrRecord.setMimeType(darRecord.getMimeType());
      flrRecord.setTitle(darRecord.getTitle());
      flrRecord.setCreateBy(darRecord.getUploadedBy());
      flrRecord.setPublisher(darRecord.getPublisher());
      flrRecord.setClassification(darRecord.getClassification());
      flrRecord.setEnvironment(darRecord.getEnvironment());
      flrRecord.setCoverage(darRecord.getCoverage());
      flrRecord.setLanguage(darRecord.getLanguage());
      flrRecord.setTechnicalRequirements(darRecord.getTechRequirements());
      flrRecord.setDistribution(darRecord.getDistribution());
      flrRecord.setVersion(String.valueOf(darRecord.getVersion()));
      flrRecord.setSkill(darRecord.getSkillLevel());
      flrRecord.setPartOf(darRecord.getIsPartOf());
      flrRecord.setRequires(darRecord.getRequires());
      flrRecord.setOwner(darRecord.getOwner());
      flrRecord.setInteractivity(darRecord.getInteractivity());
      flrRecord.setLevel(darRecord.getSecurityLevel());
      flrRecord.setKeywords(DsUtil.buildCommaStringFromStringList(darRecord.getKeywords()));
      flrRecord.setUpdatedDateStr(String.valueOf(darRecord.getPureUpdateDate()));
      flrRecord.setUploadDateStr(String.valueOf(darRecord.getPureUploadDate()));
      flrRecord.setFlrDocId(darRecord.getLrDocId());
      flrRecord.setFlrParadataId(darRecord.getLrParadataId());
      flrRecord.setFlrResourceLocator(darRecord.getUrl());
      //TODO add paradata stuff
      return flrRecord;
   }

}
