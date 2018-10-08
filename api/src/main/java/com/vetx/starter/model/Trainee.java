package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@EqualsAndHashCode(callSuper = false)
public class Trainee extends UserDateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @NotBlank
  @Size(max = 140)
  private String name;

  @NotBlank
  @Size(max = 140)
  private String surname;

  @NotBlank
  @Size(max = 140)
  private String fathersName;

  @NotBlank
  @Size(max = 140)
  private String nationality;

  @NotBlank
  @Size(max = 140)
  private String documentCode;

  @NotBlank
  @Size(max = 140)
  @NaturalId
  private String ama;

  @Enumerated(EnumType.STRING)
  @Column(length = 60)
  private CardType cardType;

  @Enumerated(EnumType.STRING)
  @Column(length = 60)
  private CardStatus cardStatus;

  @JsonManagedReference
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "trainee",
      targetEntity = SeminarTrainee.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private Set<SeminarTrainee> seminarTrainees = new HashSet<>();

  @JsonManagedReference
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "trainee",
      targetEntity = TraineeSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  private Set<TraineeSpecialty> traineeSpecialties = new HashSet<>();
}
