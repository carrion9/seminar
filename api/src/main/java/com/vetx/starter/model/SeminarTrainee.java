package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
    {"contractor_id", "seminar_id", "trainee_id", "specialty_id"})})
public class SeminarTrainee extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @PositiveOrZero
  @Builder.Default
  private  Double grade = 0.0;

  @Builder.Default
  private boolean passed = false;

  @ManyToOne
  @JoinColumn(name="contractor_id")
  private Contractor contractor;

  @ManyToOne
  @JoinColumn(name="seminar_id")
  private Seminar seminar;

  @ManyToOne
  @JoinColumn(name="trainee_id")
  private Trainee trainee;

  @ManyToOne
  @JoinColumn(name="specialty_id")
  private Specialty specialty;
}
