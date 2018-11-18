package com.vetx.starter.repository;

import com.vetx.starter.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(excerptProjection = SeminarContractorWithSuggestedCostAndNumOfTrainees.class)
@CrossOrigin
//@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarContractorRepository extends JpaRepository<SeminarContractor, Long> {

  Optional<SeminarContractor> findBySeminarAndContractor(Seminar seminar, Contractor contractor);
  List<SeminarContractor> findAllBySeminar(Seminar seminar);

  default Double getTotalCostBySeminar(Seminar seminar) {
    List<SeminarContractor> seminarContractors = findAllBySeminar(seminar);
    return seminarContractors.stream().mapToDouble(SeminarContractor::getCost).sum();
  }
}
