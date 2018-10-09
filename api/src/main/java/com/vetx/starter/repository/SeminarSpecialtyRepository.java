package com.vetx.starter.repository;

import com.vetx.starter.model.SeminarSpecialty;
import com.vetx.starter.model.SeminarSpecialtyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(excerptProjection = SeminarSpecialtyProjection.class)
@CrossOrigin
//@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarSpecialtyRepository extends JpaRepository<SeminarSpecialty, Long> {
}
