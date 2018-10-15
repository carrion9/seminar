package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Specialty extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @Column(unique = true)
  private String name;

  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "specialty",
      targetEntity = SeminarSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private Set<SeminarSpecialty> seminarSpecialties = new HashSet<>();

  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "trainee",
      targetEntity = TraineeSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private Set<TraineeSpecialty> traineeSpecialties = new HashSet<>();

//  @JsonManagedReference
//  @EqualsAndHashCode.Exclude
//  @OneToMany(
//      mappedBy = "seminarTrainee",
//      targetEntity = SeminarTrainee.class,
//      cascade = CascadeType.ALL,
//      fetch = FetchType.EAGER,
//      orphanRemoval = true
//  )
//  private Set<SeminarTrainee> seminarTrainees = new HashSet<>();
}
