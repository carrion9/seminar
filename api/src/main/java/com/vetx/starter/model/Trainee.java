package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@EqualsAndHashCode(callSuper = false)
public class Trainee extends UserDateAudit {

  @Id
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
  @Column(unique = true)
  private String ama;

  @Enumerated(EnumType.STRING)
  @Column(length = 60)
  @NotNull
  @Builder.Default
  private CardType cardType = CardType.NO_CARD;

  @Enumerated(EnumType.STRING)
  @Column(length = 60)
  @NotNull
  @Builder.Default
  private DocType docType = DocType.NONE;

  @Enumerated(EnumType.STRING)
  @Column(length = 60)
  @NotNull
  @Builder.Default
  private CardStatus cardStatus = CardStatus.NO_CARD;

  private String imageLocation;

  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(
      mappedBy = "trainee",
      targetEntity = SeminarContractorTraineeSpecialty.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<SeminarContractorTraineeSpecialty> seminarContractorTraineeSpecialties = new HashSet<>();
}
