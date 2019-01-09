package com.vetx.starter.repository;

import com.vetx.starter.model.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(excerptProjection = SeminarTraineeProjection.class)
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarTraineeRepository extends JpaRepository<SeminarTrainee, Long> {

  List<SeminarTrainee> findByTraineeAndSeminar(Trainee trainee, Seminar seminar);

  List<SeminarTrainee> findAllBySeminarAndContractorAndSpecialty(Seminar seminar, Contractor contractor, Specialty specialty);

  List<SeminarTrainee> findAllBySeminarAndSpecialty(Seminar seminar, Specialty specialty);

  @Query("SELECT DISTINCT a.contractor FROM SeminarTrainee as a WHERE a.seminar=?1 and a.specialty=?2")
  List<Contractor> findDistinctContractorBySeminarAndSpecialty(Seminar seminar, Specialty specialty);

  List<SeminarTrainee> findBySeminar(Seminar seminar);

  @Query("SELECT COUNT(DISTINCT a.trainee) FROM SeminarTrainee as a WHERE a.seminar=?1 and a.contractor=?2")
  Long countDistinctTraineeBySeminarAndContractor(Seminar seminar, Contractor contractor);

  Optional<SeminarTrainee> findBySeminarAndContractorAndTraineeAndSpecialty(Seminar seminar, Contractor contractor, Trainee trainee, Specialty specialty);

}
