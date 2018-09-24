package com.vetx.starter.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class TraineeCard  {
  //TODO: dummy, change based on business rules
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @NaturalId
  @Column(length = 60)
  private CardType cardType;

  @Enumerated(EnumType.STRING)
  @NaturalId
  @Column(length = 60)
  private CardStatus cardStatus;

//  private List<String> specializationList = new ArrayList<>();
}
