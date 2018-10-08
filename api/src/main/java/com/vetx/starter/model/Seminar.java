package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
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
  private Long id;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @Enumerated(EnumType.STRING)
  private SeminarType seminarType;

  @NotBlank
  private Instant date;

  @NotBlank
  @NaturalId
  @Column(length = 140)
  private String name;

  @Builder.Default
  @JsonManagedReference
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "seminar",
      targetEntity = SeminarTrainee.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private Set<SeminarTrainee> seminarTrainees = new HashSet<>();

  @Builder.Default
  @JsonManagedReference
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "seminar",
      targetEntity = SeminarSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private Set<SeminarSpecialty> seminarSpecialties = new HashSet<>();
}
