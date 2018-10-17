package com.vetx.starter.repository;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarProjection;
import com.vetx.starter.model.SeminarSpecialtyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(excerptProjection = SeminarProjection.class)
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarRepository extends JpaRepository<Seminar, Long> {
}
