package com.vetx.starter.service;

import com.vetx.starter.model.SeminarContractor;
import com.vetx.starter.repository.SeminarContractorRepository;
import com.vetx.starter.repository.SeminarTraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SeminarContractorSuggestedCostService {

  private SeminarTraineeRepository seminarTraineeRepository;

  @Value("${trainee.suggestedCost}")
  private Double suggestedCostPerTrainee;

  @Autowired
  public SeminarContractorSuggestedCostService(SeminarTraineeRepository seminarTraineeRepository) {
    this.seminarTraineeRepository = seminarTraineeRepository;
  }

  public Double calculateSuggestedTotalCost(SeminarContractor seminarContractor) {
    Long traineeCount = seminarTraineeRepository.countDistinctTraineeBySeminarAndContractor(seminarContractor.getSeminar(), seminarContractor.getContractor());

    return traineeCount * suggestedCostPerTrainee;
  }
}
