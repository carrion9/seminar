package com.vetx.starter.controller;

import com.vetx.starter.exception.CannotDeleteUserException;
import com.vetx.starter.exception.ResourceNotFoundException;
import com.vetx.starter.exception.UserNotFoundException;
import com.vetx.starter.model.auth.RoleName;
import com.vetx.starter.model.auth.User;
import com.vetx.starter.repository.UserRepository;
import com.vetx.starter.payload.UserIdentityAvailability;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping(produces =MediaType.APPLICATION_JSON_VALUE)
  public Page<User> getUsers(
      @CurrentUser UserPrincipal currentUser,
      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
      ) {

    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

    Page<User> users = userRepository.findAll(pageable);
    return users;
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
  public User getUserProfile(@PathVariable(value = "username") String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    if ((user.getEmail().equals(currentUser.getEmail())) ||
        (user.getRoles().stream().filter(role -> role.getName().equals(RoleName.ROLE_ADMIN)).count() != 0))
    {
      throw new CannotDeleteUserException();
    }
    userRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public User updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    return userRepository.save(user);
  }
}
