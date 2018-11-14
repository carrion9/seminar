package com.vetx.starter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
    {"contractor_id", "seminar_id"})})
public class SeminarContractor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @PositiveOrZero
  @Builder.Default
  @NotNull
  private Double cost = 0.0;

  @ManyToOne
  @JoinColumn(name = "contractor_id")
  private Contractor contractor;

  @ManyToOne
  @JoinColumn(name = "seminar_id")
  private Seminar seminar;
}
