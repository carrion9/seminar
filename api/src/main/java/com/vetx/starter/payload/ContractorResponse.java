package com.vetx.starter.payload;

import lombok.Data;

import java.time.Instant;

@Data
public class ContractorResponse {

  private Long id;
  private String name;
  private String activity;
  private String address;
  private Long phoneNumber;
  private String email;
  private String representativeName;
  private String DOY;
  private Long afm;
  private UserSummary createdBy;
  private UserSummary updatedBy;
  private Instant creationDateTime;
  private Instant updateDateTime;
}
