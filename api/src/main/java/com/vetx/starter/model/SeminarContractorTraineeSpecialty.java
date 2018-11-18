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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
    {"seminarContractor_id", "trainee_id", "specialty_id"})})
public class SeminarContractorTraineeSpecialty extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @PositiveOrZero
  @Builder.Default
  private  Double grade = 0.0;

  @Builder.Default
  private boolean passed = false;

  @ManyToOne
  @JoinColumn(name="seminarContractor_id")
  private SeminarContractor seminarContractor;

  @ManyToOne
  @JoinColumn(name="trainee_id")
  private Trainee trainee;

  @ManyToOne
  @JoinColumn(name="specialty_id")
  private Specialty specialty;
}
