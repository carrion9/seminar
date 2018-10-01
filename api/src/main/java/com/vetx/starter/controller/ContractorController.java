package com.vetx.starter.controller;

import com.vetx.starter.model.Contractor;
import com.vetx.starter.model.Seminar;
import com.vetx.starter.payload.*;
import com.vetx.starter.security.CurrentUser;
import com.vetx.starter.security.UserPrincipal;
import com.vetx.starter.service.ContractorService;
import com.vetx.starter.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/contractor")
public class ContractorController {

  private ContractorService contractorService;

  @Autowired
  public ContractorController(ContractorService contractorService) {
    this.contractorService = contractorService;
  }

  @GetMapping()
  public PagedResponse<ContractorResponse> getSeminars(
      UserPrincipal currentUser,
      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    return contractorService.getAllContractors(currentUser, page, size);
  }

  @PostMapping()
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> createContractor(@Valid @RequestBody ContractorRequest contractorRequest) {
    Contractor contractor = contractorService.createContractor(contractorRequest);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest().path("/{contractorId}")
        .buildAndExpand(contractor.getId()).toUri();

    return ResponseEntity.created(location)
        .body(new ApiResponse(true, "Contractor Created Successfully"));
  }

  @GetMapping("/{contractorId}")
  public ContractorResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long contractorId) {
    return contractorService.getContractorById(contractorId, currentUser);
  }
}
