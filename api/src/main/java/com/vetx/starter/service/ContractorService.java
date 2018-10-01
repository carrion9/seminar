package com.vetx.starter.service;

import com.vetx.starter.exception.ResourceNotFoundException;
import com.vetx.starter.model.Contractor;
import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.auth.User;
import com.vetx.starter.payload.*;
import com.vetx.starter.repository.ContractorRepository;
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
public class ContractorService {

  private ContractorRepository contractorRepository;
  private UserRepository userRepository;

  @Autowired
  public ContractorService(ContractorRepository contractorRepository) {
    this.contractorRepository = contractorRepository;
  }

  public PagedResponse<ContractorResponse> getAllContractors(UserPrincipal currentUser, int page, int size) {
    //    validatePageNumberAndSize(page, size);

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    Page<Contractor> contractors = contractorRepository.findAll(pageable);

    if(contractors.getNumberOfElements() == 0) {
      return new PagedResponse<>(Collections.emptyList(), contractors.getNumber(),
          contractors.getSize(), contractors.getTotalElements(), contractors.getTotalPages(), contractors.isLast());
    }

    Map<Long, User> creatorMap = getContractorCreatorMap(contractors.getContent());
    Map<Long, User> editorMap = getContractorEditorMap(contractors.getContent());

    List<ContractorResponse> contractorResponses = contractors.map(contractor -> {
      User creator = creatorMap.get(contractor.getCreatedBy());
      User editor = editorMap.get(contractor.getUpdatedBy());
      return mapToContractorResponse(contractor, creator, editor);
    }).getContent();

    return new PagedResponse<>(contractorResponses, contractors.getNumber(),
        contractors.getSize(), contractors.getTotalElements(), contractors.getTotalPages(), contractors.isLast());
  }

  public ContractorResponse getContractorById(Long contractorId, UserPrincipal currentUser) {
      Contractor contractor = contractorRepository.findById(contractorId).orElseThrow(
          () -> new ResourceNotFoundException("Contractor", "id", contractorId));
      User creator = userRepository.findById(contractor.getCreatedBy())
          .orElseThrow(() -> new ResourceNotFoundException("User", "id", contractor.getCreatedBy()));;

      User editor = userRepository.findById(contractor.getUpdatedBy())
          .orElseThrow(() -> new ResourceNotFoundException("User", "id", contractor.getCreatedBy()));


      return mapToContractorResponse(contractor, creator, editor);
    }

  public Contractor createContractor(ContractorRequest contractorRequest) {
      Contractor contractor = Contractor.builder()
          .name(contractorRequest.getName())
//          .seminarType(contractorRequest.getSeminarType())
//          .seminarSpecialities(contractorRequest.getSpecialityNames())
          .build();
      return contractorRepository.save(contractor);
  }

  private ContractorResponse mapToContractorResponse(Contractor contractor, User creator, User editor) {
    ContractorResponse contractorResponse = new ContractorResponse();
    contractorResponse.setId(contractor.getId());
//    contractorResponse.setDate(contractor.getDate());
//    contractorResponse.setSeminarType(contractor.getSeminarType().name());
    contractorResponse.setName(contractor.getName());
    contractorResponse.setCreationDateTime(contractor.getCreatedAt());
    contractorResponse.setUpdateDateTime(contractor.getUpdatedAt());
    UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
    UserSummary editorSummary = new UserSummary(editor.getId(), editor.getUsername(), editor.getName());
    contractorResponse.setCreatedBy(creatorSummary);
    contractorResponse.setUpdatedBy(editorSummary);
    return contractorResponse;
  }

  private Map<Long, User> getContractorEditorMap(List<Contractor> contractorList) {
    List<Long> creatorIds = contractorList.stream()
        .map(Contractor::getUpdatedBy)
        .distinct()
        .collect(Collectors.toList());

    List<User> creators = userRepository.findByIdIn(creatorIds);
    Map<Long, User> editorMap = creators.stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));

    return editorMap;
  }

  private Map<Long, User> getContractorCreatorMap(List<Contractor> contractorList) {
    List<Long> creatorIds = contractorList.stream()
        .map(Contractor::getCreatedBy)
        .distinct()
        .collect(Collectors.toList());

    List<User> creators = userRepository.findByIdIn(creatorIds);
    Map<Long, User> creatorMap = creators.stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));

    return creatorMap;
  }
}
