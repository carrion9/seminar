package com.vetx.starter.repository;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarSpecialty;
import com.vetx.starter.model.SeminarSpecialtyProjection;
import com.vetx.starter.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(excerptProjection = SeminarSpecialtyProjection.class)
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarSpecialtyRepository extends JpaRepository<SeminarSpecialty, Long> {
  boolean existsSeminarSpecialtyBySeminarAndSpecialty(Seminar seminar, Specialty specialty);
}
