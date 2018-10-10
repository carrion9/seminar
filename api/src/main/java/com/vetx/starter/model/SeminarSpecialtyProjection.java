package com.vetx.starter.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "SeminarSpecialtyProjection", types = {SeminarSpecialty.class})
public interface SeminarSpecialtyProjection {
  Long getId();
  Long getKey();
  Specialty getSpecialty();
}
