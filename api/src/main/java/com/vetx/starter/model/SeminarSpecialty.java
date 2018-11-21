package com.vetx.starter.model;

import lombok.*;
import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
    {"specialty_id", "seminar_id"})})
public class SeminarSpecialty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @ManyToOne
  @JoinColumn(name = "specialty_id")
  private Specialty specialty;

  @ManyToOne
  @JoinColumn(name = "seminar_id")
  private Seminar seminar;

}
