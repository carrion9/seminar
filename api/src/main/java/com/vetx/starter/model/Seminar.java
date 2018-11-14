package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Seminar extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @NotNull
  @Enumerated(EnumType.STRING)
  private SeminarType seminarType;

  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",timezone = "UTC")
  private LocalDate date;

  @NotBlank
  @Column(unique = true)
  private String name;

  @Builder.Default
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "seminar",
      targetEntity = SeminarTrainee.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<SeminarTrainee> seminarTrainees = new HashSet<>();

  @Builder.Default
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "seminar",
      targetEntity = SeminarSpecialty.class,
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<SeminarSpecialty> seminarSpecialties = new HashSet<>();

  @Builder.Default
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "seminar",
      targetEntity = SeminarContractor.class,
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<SeminarContractor> seminarContractors = new HashSet<>();
}
