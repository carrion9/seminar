package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SeminarTrainee extends UserDateAudit {
  //TODO: dummy, change based on business rules

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long cost;

  private Long actualCost;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "contractor_id", nullable = false)
  private Contractor contractor;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "seminar_id", nullable = false)
  private Seminar seminar;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "trainee_id", nullable = false)
  private Trainee trainee;

  @ElementCollection(targetClass=SpecialityName.class)
  @Enumerated(EnumType.STRING)
  private Set<SpecialityName> testSpecialities = new HashSet<>();
}
