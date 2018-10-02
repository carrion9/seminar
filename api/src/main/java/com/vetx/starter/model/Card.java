package com.vetx.starter.model;

import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Card {
  //TODO: dummy, change based on business rules
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long key;

  @Enumerated(EnumType.STRING)
  @Column(length = 60)
  private CardType cardType;

  @Enumerated(EnumType.STRING)
  @Column(length = 60)
  private CardStatus cardStatus;

  @ElementCollection(targetClass=SpecialityName.class)
  @Enumerated(EnumType.STRING)
  private Set<SpecialityName> passedSpecialities = new HashSet<>();
}
