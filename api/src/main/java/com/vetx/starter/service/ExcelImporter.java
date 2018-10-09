package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.ContractorRepository;
import com.vetx.starter.repository.SeminarTraineeRepository;
import com.vetx.starter.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Transactional
public class ExcelImporter {

  private TraineeRepository traineeRepository;
  private SeminarTraineeRepository seminarTraineeRepository;
  private ContractorRepository contractorRepository;

  @Autowired
  public ExcelImporter(TraineeRepository traineeRepository, SeminarTraineeRepository seminarTraineeRepository, ContractorRepository contractorRepository) {
    this.traineeRepository = traineeRepository;
    this.seminarTraineeRepository = seminarTraineeRepository;
    this.contractorRepository = contractorRepository;
  }


  public ApiResponse importExcel(Seminar seminar, byte[] byteExcel) {

    InputStream excel = new ByteArrayInputStream(byteExcel);

    //Read from excel contractor afm. He may already exist in db.
    Contractor contractor = null;
    Long contractorAfm = 1234567489L;
    if (contractorRepository.existsByAfm(contractorAfm)){
      contractor = contractorRepository.findByAfm(contractorAfm).get();
    }
    else {
      contractor = contractorRepository.save(Contractor.builder().afm(contractorAfm).build());
    }

    //Και κάπως έτσι γεννιέται ένας εκπαιδευόμενος με ΝΤΑΤΑ.
    Trainee newTrainee = Trainee.builder().ama("123").cardStatus(CardStatus.DELIVERED).fathersName("mpampas").name("foo").nationality("bar").surname("pipis").documentCode("asd").build();
    traineeRepository.save(newTrainee);

    SeminarTrainee seminarTrainee = SeminarTrainee.builder().trainee(newTrainee).seminar(seminar).cost(60L).actualCost(0L).contractor(contractor).build();
    seminarTraineeRepository.save(seminarTrainee);

    return new ApiResponse(true, "File uploaded succesfully");
  }
}
