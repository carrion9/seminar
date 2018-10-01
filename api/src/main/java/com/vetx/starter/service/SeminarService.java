package com.vetx.starter.service;

import com.vetx.starter.exception.ResourceNotFoundException;
import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.auth.User;
import com.vetx.starter.payload.PagedResponse;
import com.vetx.starter.payload.SeminarRequest;
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
    Map<Long, User> editorMap = getSeminarEditorMap(seminars.getContent());

    List<SeminarResponse> seminarResponses = seminars.map(seminar -> {
      User creator = creatorMap.get(seminar.getCreatedBy());
      User editor = editorMap.get(seminar.getUpdatedBy());
      return mapToSeminarResponse(seminar, creator, editor);
    }).getContent();

    return new PagedResponse<>(seminarResponses, seminars.getNumber(),
        seminars.getSize(), seminars.getTotalElements(), seminars.getTotalPages(), seminars.isLast());
  }

  public Seminar createSeminar(SeminarRequest seminarRequest) {
    Seminar seminar = Seminar.builder()
        .name(seminarRequest.getName())
        .seminarType(seminarRequest.getSeminarType())
        .seminarSpecialities(seminarRequest.getSpecialityNames())
        .build();
    return seminarRepository.save(seminar);
  }

  public SeminarResponse getSeminarById(Long seminarId, UserPrincipal currentUser) {
    Seminar seminar = seminarRepository.findById(seminarId).orElseThrow(
        () -> new ResourceNotFoundException("Seminar", "id", seminarId));
    User creator = userRepository.findById(seminar.getCreatedBy())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", seminar.getCreatedBy()));;

    User editor = userRepository.findById(seminar.getUpdatedBy())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", seminar.getCreatedBy()));


    return mapToSeminarResponse(seminar, creator, editor);
  }

  private SeminarResponse mapToSeminarResponse(Seminar seminar, User creator, User editor) {
    SeminarResponse seminarResponse = new SeminarResponse();
    seminarResponse.setId(seminar.getId());
    seminarResponse.setDate(seminar.getDate());
    seminarResponse.setSeminarType(seminar.getSeminarType().name());
    seminarResponse.setName(seminar.getName());
    seminarResponse.setCreationDateTime(seminar.getCreatedAt());
    seminarResponse.setUpdateTime(seminar.getUpdatedAt());
    UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
    UserSummary editorSummary = new UserSummary(editor.getId(), editor.getUsername(), editor.getName());
    seminarResponse.setCreatedBy(creatorSummary);
    seminarResponse.setUpdatedBy(editorSummary);
    return seminarResponse;
  }

  private Map<Long, User> getSeminarEditorMap(List<Seminar> seminarList) {
    List<Long> creatorIds = seminarList.stream()
        .map(Seminar::getUpdatedBy)
        .distinct()
        .collect(Collectors.toList());

    List<User> creators = userRepository.findByIdIn(creatorIds);
    Map<Long, User> editorMap = creators.stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));

    return editorMap;
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
