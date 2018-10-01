package com.vetx.starter.payload;

import com.vetx.starter.model.SeminarType;
import lombok.Data;

import java.time.Instant;

@Data
public class SeminarResponse {

  private Long id;
  private String seminarType;
  private Instant date;
  private String name;
  private UserSummary createdBy;
  private Instant creationDateTime;
}
