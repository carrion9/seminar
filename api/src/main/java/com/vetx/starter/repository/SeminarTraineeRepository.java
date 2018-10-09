package com.vetx.starter.repository;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarTrainee;
import com.vetx.starter.model.SeminarTraineeProjection;
import com.vetx.starter.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@RepositoryRestResource(excerptProjection = SeminarTraineeProjection.class)
@CrossOrigin
//@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarTraineeRepository extends JpaRepository<SeminarTrainee, Long> {

  Optional<SeminarTrainee> findByTraineeAndSeminar(Trainee trainee, Seminar seminar);
}