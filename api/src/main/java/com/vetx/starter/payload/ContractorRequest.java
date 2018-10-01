package com.vetx.starter.payload;

import lombok.Data;

@Data
public class ContractorRequest {
  private String name;
  private String activity;
  private String address;
  private Long phoneNumber;
  private String email;
  private String representativeName;
  private String DOY;
  private Long afm;
}
