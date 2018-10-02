package com.vetx.starter.repository;

import com.vetx.starter.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorRepository extends PagingAndSortingRepository<Contractor, Long> {
}
