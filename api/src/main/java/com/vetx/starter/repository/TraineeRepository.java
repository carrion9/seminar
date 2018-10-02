package com.vetx.starter.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends PagingAndSortingRepository<TraineeRepository, Long> {
}
