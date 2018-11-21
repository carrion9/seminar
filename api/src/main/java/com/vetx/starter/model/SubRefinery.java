package com.vetx.starter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SubRefinery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @NotBlank
  @Column(unique = true)
  private String name;

  @Builder.Default
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "subRefinery",
      targetEntity = SeminarContractorSubRefinery.class,
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private Set<SeminarContractorSubRefinery> seminarContractorSubRefineries = new HashSet<>();
}
