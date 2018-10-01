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
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Seminar extends UserDateAudit {
  //TODO: dummy, change based on business rules

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private SeminarType seminarType;

  @NotBlank
  private Instant date;

  @NotBlank
  @Column(length = 60)
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

}
