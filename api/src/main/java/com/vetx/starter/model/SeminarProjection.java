package com.vetx.starter.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Projection(name = "seminarProjection", types = Seminar.class)
public interface SeminarProjection {

  Long getKey();

  SeminarType getSeminarType();

  LocalDate getDate();

  String getName();

  Set<SeminarTraineeProjection> getSeminarTrainees();

  Set<SeminarSpecialtyProjection> getSeminarSpecialties();

  Set<SeminarContractorProjection> getSeminarContractors();

  String getCreatedBy();

  String getUpdatedBy();

  Instant getCreatedAt();

  Instant getUpdatedAt();

  @Value("#{@seminarContractorRepository.getTotalCostBySeminar(target)}")
  Double getCost();

}
