package com.vetx.starter.repository;

import com.vetx.starter.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Repository
@CrossOrigin
//@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface ContractorRepository extends JpaRepository<Contractor, Long> {
  Optional<Contractor> findByAfm(@Param("afm") String afm);
}
