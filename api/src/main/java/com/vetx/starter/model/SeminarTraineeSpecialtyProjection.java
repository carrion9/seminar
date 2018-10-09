package com.vetx.starter.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "seminarTraineeSpecialtyInline", types = {SeminarTraineeSpecialty.class})
public interface SeminarTraineeSpecialtyProjection {
  Long getId();
  Long getKey();
  Double getGrade();
  boolean isPassed();
  Specialty getSpecialty();
}
