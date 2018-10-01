package com.vetx.starter.service;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.auth.User;
import com.vetx.starter.payload.PagedResponse;
import com.vetx.starter.payload.SeminarResponse;
import com.vetx.starter.payload.UserSummary;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.repository.UserRepository;
import com.vetx.starter.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SeminarService {
  private SeminarRepository seminarRepository;
  private UserRepository userRepository;

  @Autowired
  public SeminarService(SeminarRepository seminarRepository, UserRepository userRepository) {
    this.seminarRepository = seminarRepository;
    this.userRepository = userRepository;
  }

  public PagedResponse<SeminarResponse> getAllSeminars(UserPrincipal currentUser, int page, int size) {
//    validatePageNumberAndSize(page, size);

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Seminar> seminars = seminarRepository.findAll(pageable);

    if(seminars.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), seminars.getNumber(),
          seminars.getSize(), seminars.getTotalElements(), seminars.getTotalPages(), seminars.isLast());
    }

    Map<Long, User> creatorMap = getSeminarCreatorMap(seminars.getContent());

    List<SeminarResponse> seminarResponses = seminars.map(seminar -> {
      SeminarResponse seminarResponse = new SeminarResponse();
      seminarResponse.setId(seminar.getId());
      seminarResponse.setDate(seminar.getDate());
      seminarResponse.setSeminarType(seminar.getSeminarType().name());
      seminarResponse.setName(seminar.getName());
      seminarResponse.setCreationDateTime(seminar.getCreatedAt());
      User creator = creatorMap.get(seminar.getCreatedBy());
      UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
      seminarResponse.setCreatedBy(creatorSummary);
      return seminarResponse;
    }).getContent();

    return new PagedResponse<>(seminarResponses, seminars.getNumber(),
        seminars.getSize(), seminars.getTotalElements(), seminars.getTotalPages(), seminars.isLast());
  }

  private Map<Long, User> getSeminarCreatorMap(List<Seminar> seminarList) {
    List<Long> creatorIds = seminarList.stream()
        .map(Seminar::getCreatedBy)
        .distinct()
        .collect(Collectors.toList());

    List<User> creators = userRepository.findByIdIn(creatorIds);
    Map<Long, User> creatorMap = creators.stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));

    return creatorMap;
  }
}
