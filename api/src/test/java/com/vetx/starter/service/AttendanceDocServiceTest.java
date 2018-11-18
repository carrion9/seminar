package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AttendanceDocServiceTest {

  @Autowired
  AttendanceDocService attendanceDocService;

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
  public void createStyledTable() throws Exception {
    Seminar seminar = Seminar.builder().seminarType(SeminarType.ELPE_BASIC).name("foo").date(LocalDate.now()).build();

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

    Contractor contractor1 = Contractor.builder().afm("1").activity("foo").address("bar").doy("fooBar").email("bar@foo.bar").name("barFoo1").phoneNumber("2106520959").representativeName("BarBar").build();
    Contractor contractor2 = Contractor.builder().afm("2").activity("foo").address("bar").doy("fooBar").email("bar@foo.bar").name("barFoo2").phoneNumber("2106520959").representativeName("BarBar").build();

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

    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty1 = SeminarContractorTraineeSpecialty.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty1).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty2 = SeminarContractorTraineeSpecialty.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty2).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty3 = SeminarContractorTraineeSpecialty.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty3).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty4 = SeminarContractorTraineeSpecialty.builder().contractor(contractor1).seminar(seminar).trainee(trainee1).specialty(specialty4).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty5 = SeminarContractorTraineeSpecialty.builder().contractor(contractor1).seminar(seminar).trainee(trainee2).specialty(specialty1).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty6 = SeminarContractorTraineeSpecialty.builder().contractor(contractor1).seminar(seminar).trainee(trainee2).specialty(specialty2).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty7 = SeminarContractorTraineeSpecialty.builder().contractor(contractor1).seminar(seminar).trainee(trainee2).specialty(specialty3).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty8 = SeminarContractorTraineeSpecialty.builder().contractor(contractor2).seminar(seminar).trainee(trainee3).specialty(specialty1).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty9 = SeminarContractorTraineeSpecialty.builder().contractor(contractor2).seminar(seminar).trainee(trainee3).specialty(specialty1).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty10 = SeminarContractorTraineeSpecialty.builder().contractor(contractor2).seminar(seminar).trainee(trainee4).specialty(specialty1).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty11 = SeminarContractorTraineeSpecialty.builder().contractor(contractor2).seminar(seminar).trainee(trainee5).specialty(specialty1).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty12 = SeminarContractorTraineeSpecialty.builder().contractor(contractor2).seminar(seminar).trainee(trainee5).specialty(specialty2).build();
    SeminarContractorTraineeSpecialty seminarContractorTraineeSpecialty13 = SeminarContractorTraineeSpecialty.builder().contractor(contractor2).seminar(seminar).trainee(trainee5).specialty(specialty3).build();

    Set<SeminarContractorTraineeSpecialty> seminarContractorTraineeSpecialties = new HashSet<>();
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty1);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty2);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty3);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty4);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty5);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty6);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty7);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty8);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty9);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty10);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty11);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty12);
    seminarContractorTraineeSpecialties.add(seminarContractorTraineeSpecialty13);

    seminar.setSeminarTrainees(seminarContractorTraineeSpecialties);
    seminar = seminarRepository.save(seminar);

    attendanceDocService.createDocument(seminar, specialty1);
  }
}