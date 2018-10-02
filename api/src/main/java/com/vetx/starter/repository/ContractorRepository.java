package com.vetx.starter.repository;

import com.vetx.starter.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin
public interface ContractorRepository extends PagingAndSortingRepository<Contractor, Long> {
}
