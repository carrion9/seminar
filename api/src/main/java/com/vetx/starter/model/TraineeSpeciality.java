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
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class TraineeSpeciality {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @PositiveOrZero
  private  Double grade;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name="speciality_id")
  private Speciality speciality;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name="trainee_id")
  private Trainee trainee;
}
