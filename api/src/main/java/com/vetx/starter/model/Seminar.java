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

  @Enumerated(EnumType.STRING)
  private SeminarType seminarType;

  @NotBlank
  private Instant date;

  @NotBlank
  @Column(length = 140)
  private String name;

  @OneToMany(
      mappedBy = "seminar",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  @Fetch(FetchMode.SELECT)
  @BatchSize(size = 30)
  private List<SeminarTrainee> seminarTraineeList = new ArrayList<>();

  @ElementCollection(targetClass=SpecialityName.class)
  @Enumerated(EnumType.STRING)
  private Collection<SpecialityName> seminarSpecialities = new HashSet<>();
}
