package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SeminarSpecialty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name="specialty_id")
  private Specialty specialty;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name="seminar_id")
  private Seminar seminar;

}
