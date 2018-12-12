package com.vetx.starter.repository;

import com.vetx.starter.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource(excerptProjection = SeminarContractorSubRefineryWithRefinery.class)
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarContractorSubRefineryRepository  extends JpaRepository<SeminarContractorSubRefinery, Long> {
  boolean existsBySeminarContractorAndSubRefinery(SeminarContractor seminarContractor, SubRefinery subRefinery);
}
