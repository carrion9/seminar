package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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

  @Autowired
  private SpecialtyRepository specialtyRepository;

  @Autowired
  private  SeminarSpecialtyRepository seminarSpecialtyRepository;

  private ExcelImporter excelImporter;

  private static final String FILE_NAME = "contractor-registration-elpe.xlsx";

  private byte[] data;

  private Seminar seminar;

  @Before
  public void setUp() throws Exception {
    excelImporter = new ExcelImporter(traineeRepository, seminarTraineeRepository, contractorRepository, specialtyRepository, seminarSpecialtyRepository);
    data = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(FILE_NAME).toURI()));
    seminar = seminarRepository.findById(1L).get();
  }

  @Test
  public void importExcel() throws IOException {
    ApiResponse response = excelImporter.importExcel(seminar, data, FILE_NAME);


    //Trainee No1
    Optional<Trainee> trainee = traineeRepository.findByAma("7636775");

    //Validate trainee
    assertTrue(trainee.isPresent());
    assertNotNull(trainee.get());
    assertNotNull(trainee.get().getKey());

    //Validate Contractor

    List<SeminarTrainee> seminarTrainees = seminarTraineeRepository.findByTraineeAndSeminar(trainee.get(), seminar);

    //Validate seminarTrainee
    assertEquals(6, seminarTrainees.size());


    // Trainee No2
    trainee = traineeRepository.findByAma("1909357");

    //Validate trainee
    assertTrue(trainee.isPresent());
    assertNotNull(trainee.get());
    assertNotNull(trainee.get().getKey());

    //Validate Contractor

    seminarTrainees = seminarTraineeRepository.findByTraineeAndSeminar(trainee.get(), seminar);

    //Validate seminarTrainee
    assertEquals(6, seminarTrainees.size());

    // Trainee No3
    trainee = traineeRepository.findByAma("1987983");

    //Validate trainee
    assertTrue(trainee.isPresent());
    assertNotNull(trainee.get());
    assertNotNull(trainee.get().getKey());

    //Validate Contractor

    seminarTrainees = seminarTraineeRepository.findByTraineeAndSeminar(trainee.get(), seminar);

    //Validate seminarTrainee
    assertEquals(7, seminarTrainees.size());

    // Trainee No4
    trainee = traineeRepository.findByAma("9833521");

    //Validate trainee
    assertTrue(trainee.isPresent());
    assertNotNull(trainee.get());
    assertNotNull(trainee.get().getKey());

    //Validate Contractor

    seminarTrainees = seminarTraineeRepository.findByTraineeAndSeminar(trainee.get(), seminar);

    //Validate seminarTrainee
    assertEquals(6, seminarTrainees.size());


    assertEquals("ΑΛΕΞΙΟΥ", trainee.get().getSurname());
    assertEquals("ΓΕΩΡΓΙΟΣ", trainee.get().getName());
    assertEquals("ΙΩΑΝΝΗΣ", trainee.get().getFathersName());
    assertEquals("ΕΛΛΗΝΙΚΗ", trainee.get().getNationality());
    assertEquals("ΑΗ633542", trainee.get().getDocumentCode());
    assertEquals(DocType.IDENTITY, trainee.get().getDocType());

    assertTrue(contractorRepository.findByAfm("099006588").isPresent());
    Contractor contractor = contractorRepository.findByAfm("099006588").get();
    assertEquals("ATOM DYNAMIC SA", contractor.getName());
    assertEquals("ΤΕΧΝΙΚΗ ΕΤΑΙΡΕΙΑ", contractor.getActivity());
    assertEquals("ΚΕΝΤΡΙΚΑ: ΧΡΥΣΟΣΤΟΜΟΥ ΣΜΥΡΝΗΣ 184 ΠΕΤΡΟΥΠΟΛΗ, ΥΠ/ΜΑ: ΘΕΣΗ ΠΑΤΗΜΑ, ΜΑΝΔΡΑ, 19600", contractor.getAddress());
    assertEquals("ΦΑΕ ΑΘΗΝΩΝ", contractor.getDoy());
    assertEquals("info@atomdynamic.gr, mp@atomdynamic.gr", contractor.getEmail());
    assertEquals("2105596649, 2105552968", contractor.getPhoneNumber());
    assertEquals("ΠΑΠΑΚΩΝΣΤΑΝΤΙΝΟΥ ΜΑΡΙΑ", contractor.getRepresentativeName());


    assertTrue(response.getSuccess());
    assertEquals("File uploaded succesfully", response.getMessage());
  }
}