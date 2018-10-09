package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SeminarTraineeSpecialty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @PositiveOrZero
  private  Double grade = 0.0;

  private boolean passed = false;

  @JsonBackReference
  @ManyToOne
  @EqualsAndHashCode.Exclude
  @JoinColumn(name="specialty_id")
  private Specialty specialty;

  @JsonBackReference
  @ManyToOne
  @EqualsAndHashCode.Exclude
  @JoinColumn(name="seminarTrainee_id")
  private SeminarTrainee seminarTrainee;
}
