package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@EqualsAndHashCode(callSuper = true)
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

  @OneToMany(
      mappedBy = "seminar",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private List<SeminarTrainee> seminarTraineeList = new ArrayList<>();

  @OneToMany(
      mappedBy = "seminar",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<SeminarSpeciality> seminarSpecialitySet = new HashSet<>();
}
