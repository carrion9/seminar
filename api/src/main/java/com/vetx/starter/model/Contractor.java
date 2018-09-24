package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Contractor extends UserDateAudit {
  //TODO: dummy, change based on business rules

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 140)
  private String name;

  @NotBlank
  @Size(max = 140)
  private String afm;

  @NotBlank
  @Size(max = 140)
  private String representative;
}
