package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WelcomeDocServiceTest {

  @Autowired
  WelcomeDocService welcomeDocService;

  @Autowired
  SeminarRepository seminarRepository;

  @Autowired
  SpecialtyRepository specialtyRepository;

  @Autowired
  ContractorRepository contractorRepository;

  @Autowired
  SeminarSpecialtyRepository seminarSpecialtyRepository;

  @Autowired
  TraineeRepository traineeRepository;

  @Test
  public void createDocument() throws Exception {
    Seminar seminar = Seminar.builder().seminarType(SeminarType.ELPE_BASIC).name("foo").date(Instant.now()).build();

    Specialty specialty1 = Specialty.builder().name("specialty1").build();
    Specialty specialty2 = Specialty.builder().name("specialty2").build();
    Specialty specialty3 = Specialty.builder().name("specialty3").build();
    Specialty specialty4 = Specialty.builder().name("specialty4").build();
    specialty1 = specialtyRepository.save(specialty1);
    specialty2 = specialtyRepository.save(specialty2);
    specialty3 = specialtyRepository.save(specialty3);
    specialty4 = specialtyRepository.save(specialty4);

    Set<SeminarSpecialty> seminarSpecialties = new HashSet<>();
    SeminarSpecialty seminarSpecialty1 = SeminarSpecialty.builder().specialty(specialty1).seminar(seminar).build();
    SeminarSpecialty seminarSpecialty2 = SeminarSpecialty.builder().specialty(specialty2).seminar(seminar).build();
    SeminarSpecialty seminarSpecialty3 = SeminarSpecialty.builder().specialty(specialty3).seminar(seminar).build();
    SeminarSpecialty seminarSpecialty4 = SeminarSpecialty.builder().specialty(specialty4).seminar(seminar).build();
    seminarSpecialties.add(seminarSpecialty1);
    seminarSpecialties.add(seminarSpecialty2);
    seminarSpecialties.add(seminarSpecialty3);
    seminarSpecialties.add(seminarSpecialty4);

    seminar.setSeminarSpecialties(seminarSpecialties);

    Contractor contractor1 = Contractor.builder().afm("1").activity("foo").address("bar").DOY("fooBar").email("bar@foo.bar").name("barFoo1").phoneNumber("2106520959").representativeName("BarBar").build();
    Contractor contractor2 = Contractor.builder().afm("2").activity("foo").address("bar").DOY("fooBar").email("bar@foo.bar").name("barFoo2").phoneNumber("2106520959").representativeName("BarBar").build();

    contractor1 = contractorRepository.save(contractor1);
    contractor2 = contractorRepository.save(contractor2);

    Trainee trainee1 = Trainee.builder().ama("12345L").cardStatus(CardStatus.PENDING).documentCode("foo").fathersName("Bar").name("fooBar").nationality("BarLand").surname("BarFooMan").cardType(CardType.GREEN).build();
    Trainee trainee2 = Trainee.builder().ama("12346L").cardStatus(CardStatus.PENDING).documentCode("foo").fathersName("Bar").name("fooBar").nationality("BarLand").surname("BarFooMan").cardType(CardType.GREEN).build();
    Trainee trainee3 = Trainee.builder().ama("12347L").cardStatus(CardStatus.PENDING).documentCode("foo").fathersName("Bar").name("fooBar").nationality("BarLand").surname("BarFooMan").cardType(CardType.GREEN).build();
    Trainee trainee4 = Trainee.builder().ama("12348L").cardStatus(CardStatus.PENDING).documentCode("foo").fathersName("Bar").name("fooBar").nationality("BarLand").surname("BarFooMan").cardType(CardType.GREEN).build();
    Trainee trainee5 = Trainee.builder().ama("12349L").cardStatus(CardStatus.PENDING).documentCode("foo").fathersName("Bar").name("fooBar").nationality("BarLand").surname("BarFooMan").cardType(CardType.GREEN).build();

    trainee1 = traineeRepository.save(trainee1);
    trainee2 = traineeRepository.save(trainee2);
    trainee3 = traineeRepository.save(trainee3);
    trainee4 = traineeRepository.save(trainee4);
    trainee5 = traineeRepository.save(trainee5);

    SeminarTrainee seminarTrainee1 = SeminarTrainee.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty1).build();
    SeminarTrainee seminarTrainee2 = SeminarTrainee.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty2).build();
    SeminarTrainee seminarTrainee3 = SeminarTrainee.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty3).build();
    SeminarTrainee seminarTrainee4 = SeminarTrainee.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty4).build();
    SeminarTrainee seminarTrainee5 = SeminarTrainee.builder().contractor(contractor1).seminar(seminar).trainee(trainee2).specialty(specialty1).build();
    SeminarTrainee seminarTrainee6 = SeminarTrainee.builder().contractor(contractor1).seminar(seminar).trainee(trainee2).specialty(specialty2).build();
    SeminarTrainee seminarTrainee7 = SeminarTrainee.builder().contractor(contractor1).seminar(seminar).trainee(trainee2).specialty(specialty3).build();
    SeminarTrainee seminarTrainee8 = SeminarTrainee.builder().contractor(contractor2).seminar(seminar).trainee(trainee3).specialty(specialty1).build();
    SeminarTrainee seminarTrainee9 = SeminarTrainee.builder().contractor(contractor2).seminar(seminar).trainee(trainee3).specialty(specialty1).build();
    SeminarTrainee seminarTrainee10 = SeminarTrainee.builder().contractor(contractor2).seminar(seminar).trainee(trainee4).specialty(specialty1).build();
    SeminarTrainee seminarTrainee11 = SeminarTrainee.builder().contractor(contractor2).seminar(seminar).trainee(trainee5).specialty(specialty1).build();
    SeminarTrainee seminarTrainee12 = SeminarTrainee.builder().contractor(contractor2).seminar(seminar).trainee(trainee5).specialty(specialty2).build();
    SeminarTrainee seminarTrainee13 = SeminarTrainee.builder().contractor(contractor2).seminar(seminar).trainee(trainee5).specialty(specialty3).build();

    Set<SeminarTrainee> seminarTrainees = new HashSet<>();
    seminarTrainees.add(seminarTrainee1);
    seminarTrainees.add(seminarTrainee2);
    seminarTrainees.add(seminarTrainee3);
    seminarTrainees.add(seminarTrainee4);
    seminarTrainees.add(seminarTrainee5);
    seminarTrainees.add(seminarTrainee6);
    seminarTrainees.add(seminarTrainee7);
    seminarTrainees.add(seminarTrainee8);
    seminarTrainees.add(seminarTrainee9);
    seminarTrainees.add(seminarTrainee10);
    seminarTrainees.add(seminarTrainee11);
    seminarTrainees.add(seminarTrainee12);
    seminarTrainees.add(seminarTrainee13);

    seminar.setSeminarTrainees(seminarTrainees);
    seminar = seminarRepository.save(seminar);

    welcomeDocService.createDocument(seminar);
  }
}