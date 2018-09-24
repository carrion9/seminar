package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false )
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
}
