package com.vetx.starter.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "SeminarContractorWithSuggestedCostAndNumOfTrainees", types = {SeminarContractor.class})
public interface SeminarContractorWithSuggestedCostAndNumOfTrainees {
  Long getKey();

  Contractor getContractor();

  Double getCost();

  Seminar getSeminar();

  @Value("#{@seminarContractorSuggestedCostService.calculateSuggestedTotalCost(target)}")
  Double getSuggestedCost();

  @Value("#{@seminarTraineeRepository.countDistinctTraineeBySeminarAndContractor(target.seminar, target.contractor)}")
  Double getNumOfTrainees();
}
