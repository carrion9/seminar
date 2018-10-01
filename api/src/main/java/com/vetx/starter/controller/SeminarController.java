package com.vetx.starter.controller;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.payload.PagedResponse;
import com.vetx.starter.payload.SeminarRequest;
import com.vetx.starter.payload.SeminarResponse;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.security.CurrentUser;
import com.vetx.starter.security.UserPrincipal;
import com.vetx.starter.service.SeminarService;
import com.vetx.starter.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


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

  @PostMapping()
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> createSeminar(@Valid @RequestBody SeminarRequest seminarRequest) {
    Seminar seminar = seminarService.createSeminar(seminarRequest);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{seminarId}")
        .buildAndExpand(seminar.getId()).toUri();

    return ResponseEntity.created(location)
        .body(new ApiResponse(true, "Seminar Created Successfully"));
  }

  @GetMapping("/{seminarId}")
  public SeminarResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                  @PathVariable Long seminarId) {
    return seminarService.getSeminarById(seminarId, currentUser);
  }


}
