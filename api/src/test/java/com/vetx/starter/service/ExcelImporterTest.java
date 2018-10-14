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

import java.io.IOException;
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

  private static final String FILE_NAME = "contractor-registration-elpe.xlsx";

  private byte[] data;

  private Seminar seminar;

  @Before
  public void setUp() throws Exception {
    excelImporter = new ExcelImporter(traineeRepository, seminarTraineeRepository, contractorRepository);
    data = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(FILE_NAME).toURI()));
    seminar = seminarRepository.findById(1L).get();
  }

  @Test
  public void importExcel() throws IOException {
    ApiResponse response = excelImporter.importExcel(seminar, data);

    Optional<Trainee> trainee = traineeRepository.findByAma("123");

    //Validate trainee
    assertTrue(trainee.isPresent());
    assertNotNull(trainee.get());
    assertNotNull(trainee.get().getKey());

    //Validate Contractor

    Optional<SeminarTrainee> seminarTrainee = seminarTraineeRepository.findByTraineeAndSeminar(trainee.get(), seminar);

    //Validate seminarTrainee
    assertTrue(seminarTrainee.isPresent());
    assertNotNull(seminarTrainee.get());
    assertNotNull(seminarTrainee.get().getKey());

    //diko sou
    //assertEquals();

    assertTrue(response.getSuccess());
    assertEquals("File uploaded succesfully", response.getMessage());
  }
}