package com.vetx.starter.controller;

import com.vetx.starter.exception.ResourceNotFoundException;
import com.vetx.starter.model.auth.User;
import com.vetx.starter.repository.UserRepository;
import com.vetx.starter.payload.UserIdentityAvailability;
import com.vetx.starter.payload.UserProfile;
import com.vetx.starter.payload.UserSummary;
import com.vetx.starter.security.CurrentUser;
import com.vetx.starter.security.UserPrincipal;
import com.vetx.starter.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

  private UserRepository userRepository;

  private EntityLinks links;

  @Autowired
  public UserController(UserRepository userRepository, EntityLinks entityLinks) {
    this.userRepository = userRepository;
    this.links = entityLinks;
  }

  @GetMapping(produces =MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedResources<UserProfile>> getUsers(
      UserPrincipal currentUser,
      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
      PagedResourcesAssembler assembler) {

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

    Page<UserProfile> userProfiles = userRepository.findAll(pageable).map(UserProfile::fromEntity);
    PagedResources < UserProfile > pr = assembler.toResource(userProfiles, linkTo(UserController.class).slash("/api/user").withSelfRel());
    HttpHeaders responseHeaders = new HttpHeaders();
//    responseHeaders.add("Link", createLinkHeader(pr));
    return new ResponseEntity <> (assembler.toResource(userProfiles, linkTo(UserController.class).slash("/api/user").withSelfRel()), responseHeaders, HttpStatus.OK);
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
    UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    return userSummary;
  }

  @GetMapping("/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
    Boolean isAvailable = !userRepository.existsByUsername(username);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/checkEmailAvailability")
  public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
    Boolean isAvailable = !userRepository.existsByEmail(email);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/{username}")
  public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    return UserProfile.fromEntity(user);
  }

  private String createLinkHeader(PagedResources < UserProfile > pr) {
    return buildLinkHeader(
        pr.getLinks("first").get(0).getHref(), "first") +
        ", " +
        buildLinkHeader(pr.getLinks("next").get(0).getHref(), "next"
        );
  }

  public static String buildLinkHeader(final String uri, final String rel) {
    return "<" + uri + ">; rel=\"" + rel + "\"";
  }

}
