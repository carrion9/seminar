package com.vetx.starter.repository;

import com.vetx.starter.model.SeminarTrainee;
import com.vetx.starter.model.SeminarTraineeSpeciality;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public interface SeminarTraineeSpecialityRepository extends PagingAndSortingRepository<SeminarTraineeSpeciality, Long> {
}
