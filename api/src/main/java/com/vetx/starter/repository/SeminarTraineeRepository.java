package com.vetx.starter.repository;

import com.vetx.starter.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(excerptProjection = SeminarContractorTraineeSpecialtyProjection.class)
@CrossOrigin
//@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarTraineeRepository extends JpaRepository<SeminarContractorTraineeSpecialty, Long> {

  List<SeminarContractorTraineeSpecialty> findByTraineeAndSeminar(Trainee trainee, Seminar seminar);

  List<SeminarContractorTraineeSpecialty> findAllBySeminarAndContractorAndSpecialty(Seminar seminar, Contractor contractor, Specialty specialty);

  @Query("SELECT DISTINCT a.contractor FROM SeminarContractorTraineeSpecialty as a WHERE a.seminar=?1 and a.specialty=?2")
  List<Contractor> findDistinctContractorBySeminarAndSpecialty(Seminar seminar, Specialty specialty);

  List<SeminarContractorTraineeSpecialty> findDistinctBySeminar(Seminar seminar);

  @Query("SELECT COUNT(DISTINCT a.trainee) FROM SeminarContractorTraineeSpecialty as a WHERE a.seminar=?1 and a.contractor=?2")
  Long countDistinctTraineeBySeminarAndContractor(Seminar seminar, Contractor contractor);

  Optional<SeminarContractorTraineeSpecialty> findBySeminarAndContractorAndTraineeAndSpecialty(Seminar seminar, Contractor contractor, Trainee trainee, Specialty specialty);

}
