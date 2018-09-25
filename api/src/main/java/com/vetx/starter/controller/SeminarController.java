package com.vetx.starter.controller;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.security.UserPrincipal;
import com.vetx.starter.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/polls")
public class SeminarController {

  private SeminarRepository seminarRepository;

  @Autowired
  public SeminarController(SeminarRepository seminarRepository) {
    this.seminarRepository = seminarRepository;
  }

  @GetMapping()
  public Page<Seminar> getSeminars(
      UserPrincipal currentUser,
      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

    return seminarRepository.findAll(pageable);
  }

}
