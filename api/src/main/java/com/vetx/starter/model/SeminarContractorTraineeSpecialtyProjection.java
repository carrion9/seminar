package com.vetx.starter.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "SeminarContractorTraineeSpecialtyProjection", types = {SeminarContractorTraineeSpecialty.class})
public interface SeminarContractorTraineeSpecialtyProjection {
  Long getKey();

  Double getGrade();

  boolean isPassed();

  SeminarContractor getSeminarContractor();

  Trainee getTrainee();

  Specialty getSpecialty();
}
