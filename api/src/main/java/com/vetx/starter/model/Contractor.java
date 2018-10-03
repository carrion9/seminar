package com.vetx.starter.model;

import com.vetx.starter.model.audit.UserDateAudit;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class Contractor extends UserDateAudit {
  //TODO: dummy, change based on business rules

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
  private String activity;

  @NotBlank
  @Size(max = 140)
  private String address;

  @NotBlank
  @Size(min = 8, max = 10)
  private Long phoneNumber;

  @Email
  private String email;

  @NotBlank
  @Size(max = 140)
  private String representativeName;

  @NotBlank
  @Size(max = 140)
  private String DOY;

  @NotBlank
  @Size()
  private Long afm;

  @OneToMany(
      mappedBy = "seminar",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  @Builder.Default
  private List<SeminarTrainee> seminarTraineeList = new ArrayList<>();
}
