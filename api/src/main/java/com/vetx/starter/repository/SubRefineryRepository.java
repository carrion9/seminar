package com.vetx.starter.repository;

import com.vetx.starter.model.SubRefinery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Repository
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SubRefineryRepository  extends JpaRepository<SubRefinery, Long> {
  Optional<SubRefinery> findByName(String name);
}
