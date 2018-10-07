package com.vetx.starter.repository;

import com.vetx.starter.model.SeminarSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin
//@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarSpecialtyRepository extends JpaRepository<SeminarSpecialty, Long> {
}
