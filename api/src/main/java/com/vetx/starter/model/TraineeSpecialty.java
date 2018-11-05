package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
    {"specialty_id", "trainee_id"})})
public class TraineeSpecialty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @PositiveOrZero
  private  Double grade;

  @ManyToOne
  @JoinColumn(name="specialty_id")
  private Specialty specialty;

  @ManyToOne
  @JoinColumn(name="trainee_id")
  private Trainee trainee;
}
