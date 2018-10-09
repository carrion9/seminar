package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.ContractorRepository;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.repository.SeminarTraineeRepository;
import com.vetx.starter.repository.TraineeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelImporterTest {

  @Autowired
  private TraineeRepository traineeRepository;

  @Autowired
  private SeminarRepository seminarRepository;

  @Autowired
  private SeminarTraineeRepository seminarTraineeRepository;

  @Autowired
  private ContractorRepository contractorRepository;

  private ExcelImporter excelImporter;

  private static final String FILE_NAME = "motorOil-template.xlsx";

  private byte[] data;

  private Seminar seminar;

  @Before
  public void setUp() throws Exception {
    excelImporter = new ExcelImporter(traineeRepository, seminarTraineeRepository, contractorRepository);
    data = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(FILE_NAME).toURI()));
    seminar = seminarRepository.getOne(1L);
  }

  @Test
  public void importExcel() {
    Trainee newTrainee = Trainee.builder().ama("123").cardStatus(CardStatus.DELIVERED).fathersName("mpampas").name("foo").nationality("bar").surname("pipis").documentCode("asd").build();
    ApiResponse response = excelImporter.importExcel(seminar, data);

    Optional<Trainee> trainee = traineeRepository.findByAma("123");

    //Validate trainee
    assertTrue(trainee.isPresent());
    assertNotNull(trainee.get());
    assertNotNull(trainee.get().getId());
    assertEquals(newTrainee.getCardStatus(), trainee.get().getCardStatus());

    //Validate Contractor

    Optional<SeminarTrainee> seminarTrainee = seminarTraineeRepository.findByTraineeAndSeminar(trainee.get(), seminar);

    //Validate seminarTrainee
    assertTrue(seminarTrainee.isPresent());
    assertNotNull(seminarTrainee.get());
    assertNotNull(seminarTrainee.get().getId());

    //diko sou
    //assertEquals();

    assertTrue(response.getSuccess());
    assertEquals("File uploaded succesfully", response.getMessage());
  }
}