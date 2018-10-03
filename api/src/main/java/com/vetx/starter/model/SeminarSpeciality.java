package com.vetx.starter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SeminarSpeciality {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Speciality speciality;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Seminar seminar;

}
