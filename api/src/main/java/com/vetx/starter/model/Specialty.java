package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
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
  @NotNull
  private String name;

  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(
      mappedBy = "specialty",
      targetEntity = SeminarSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<SeminarSpecialty> seminarSpecialties = new HashSet<>();

  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(
      mappedBy = "specialty",
      targetEntity = TraineeSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<TraineeSpecialty> traineeSpecialties = new HashSet<>();

  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(
      mappedBy = "specialty",
      targetEntity = SeminarTrainee.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<SeminarTrainee> seminarTrainees = new HashSet<>();
}