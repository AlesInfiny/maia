package com.dressca.cms.announcement.applicationcore.exception;

import lombok.Data;

@Data
public class AnnouncementValidationError {
  private final String fieldName;
  private final String messageCode;
}
