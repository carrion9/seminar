package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SeminarTrainee extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  private Long cost;

  private Long actualCost;

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

  @JsonManagedReference
  @OneToMany(
      mappedBy = "seminarTrainee",
      targetEntity = SeminarTraineeSpeciality.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private Set<SeminarTraineeSpeciality> seminarTraineeSpecialitySet = new HashSet<>();
}
