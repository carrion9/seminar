package com.vetx.starter.repository;

import com.vetx.starter.model.SubRefinery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubRefineryRepository  extends JpaRepository<SubRefinery, Long> {
  Optional<SubRefinery> findByName(String name);
}
