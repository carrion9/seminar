package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class SeminarTrainee extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  private Long cost;

  private Long actualCost;

  @PositiveOrZero
  private  Double grade = 0.0;

  private boolean passed = false;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name="contractor_id")
  private Contractor contractor;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name="seminar_id")
  private Seminar seminar;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name="trainee_id")
  private Trainee trainee;

  @JsonBackReference
  @ManyToOne
//  @EqualsAndHashCode.Exclude
  @JoinColumn(name="specialty_id")
  private Specialty specialty;
}
