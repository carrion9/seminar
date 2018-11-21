package com.vetx.starter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
    {"seminar_contractor_id", "sub_refinery_id"})})
public class SeminarContractorSubRefinery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @ManyToOne
  @JoinColumn(name="seminar_contractor_id")
  private SeminarContractor seminarContractor;

  @ManyToOne
  @JoinColumn(name="sub_refinery_id")
  private SubRefinery subRefinery;
}
