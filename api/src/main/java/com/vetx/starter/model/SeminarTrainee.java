package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SeminarTrainee extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  private Long cost;

  private Long actualCost;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Contractor contractor;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Seminar seminar;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Trainee trainee;

  @OneToMany(
      mappedBy = "seminarTrainee",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<SeminarTraineeSpeciality> seminarTraineeSpecialitySet = new HashSet<>();
}
