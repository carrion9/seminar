package com.vetx.starter.repository;

import com.vetx.starter.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = SeminarContractorSubRefineryWithRefinery.class)
public interface SeminarContractorSubRefineryRepository  extends JpaRepository<SeminarContractorSubRefinery, Long> {
  boolean existsBySeminarContractorAndSubRefinery(SeminarContractor seminarContractor, SubRefinery subRefinery);
}
