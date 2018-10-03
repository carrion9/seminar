package com.vetx.starter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Speciality {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @NaturalId
  private String name;

  @OneToMany(
      mappedBy = "speciality",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private List<SeminarSpeciality> seminarSpecialityList = new ArrayList<>();

  @OneToMany(
      mappedBy = "trainee",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private List<TraineeSpeciality> traineeSpecialityList = new ArrayList<>();

  @OneToMany(
      mappedBy = "seminarTrainee",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private List<SeminarTraineeSpeciality> seminarTraineeSpecialityList = new ArrayList<>();
}
