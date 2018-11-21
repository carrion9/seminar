package com.vetx.starter.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "SeminarContractorProjection", types = {SeminarContractor.class})
public interface SeminarContractorProjection {
  Long getKey();
  Contractor getContractor();
  Double getCost();
//  Seminar getSeminar();
  Set<SeminarContractorSubRefineryWithRefinery> getSeminarContractorSubRefineries();

  @Value("#{@seminarContractorSuggestedCostService.calculateSuggestedTotalCost(target)}")
  Double getSuggestedCost();

  @Value("#{@seminarTraineeRepository.countDistinctTraineeBySeminarAndContractor(target.seminar, target.contractor)}")
  Double getNumOfTrainees();
}
