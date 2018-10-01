package com.vetx.starter.controller;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.payload.PagedResponse;
import com.vetx.starter.payload.SeminarResponse;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.security.UserPrincipal;
import com.vetx.starter.service.SeminarService;
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
@RequestMapping("/api/seminar")
public class SeminarController {

  private SeminarService seminarService;

  @Autowired
  public SeminarController(SeminarService seminarService) {
    this.seminarService = seminarService;
  }

  @GetMapping()
  public PagedResponse<SeminarResponse> getSeminars(
      UserPrincipal currentUser,
      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    return seminarService.getAllSeminars(currentUser, page, size);
  }

}
