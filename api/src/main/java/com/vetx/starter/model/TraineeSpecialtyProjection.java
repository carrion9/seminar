package com.vetx.starter.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "TraineeSpecialtyProjection", types = {TraineeSpecialty.class})
public interface TraineeSpecialtyProjection {
Long getKey();
Double getGrade();
Specialty getSpecialty();
}
