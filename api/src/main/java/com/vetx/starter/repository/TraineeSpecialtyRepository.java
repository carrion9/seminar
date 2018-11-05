package com.vetx.starter.repository;

import com.vetx.starter.model.SeminarSpecialtyProjection;
import com.vetx.starter.model.TraineeSpecialty;
import com.vetx.starter.model.TraineeSpecialtyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(excerptProjection = TraineeSpecialtyProjection.class)
@CrossOrigin
//@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface TraineeSpecialtyRepository extends JpaRepository<TraineeSpecialty, Long> {

}