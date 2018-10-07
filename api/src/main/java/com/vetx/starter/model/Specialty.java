package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
public class Specialty extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @NaturalId
  private String name;

  @JsonManagedReference
  @OneToMany(
      mappedBy = "specialty",
      targetEntity = SeminarSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private List<SeminarSpecialty> seminarSpecialtyList = new ArrayList<>();

  @JsonManagedReference
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "trainee",
      targetEntity = TraineeSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private List<TraineeSpecialty> traineeSpecialtyList = new ArrayList<>();

  @JsonManagedReference
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "seminarTrainee",
      targetEntity = SeminarTraineeSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private List<SeminarTraineeSpecialty> seminarTraineeSpecialtyList = new ArrayList<>();
}
