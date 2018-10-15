package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SeminarSpecialty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @ManyToOne
  @JoinColumn(name="specialty_id")
  private Specialty specialty;

  @ManyToOne
  @JoinColumn(name="seminar_id")
  private Seminar seminar;

}
