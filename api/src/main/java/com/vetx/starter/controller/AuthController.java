package com.vetx.starter.controller;

import com.vetx.starter.exception.AppException;
import com.vetx.starter.model.auth.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.payload.auth.JwtAuthenticationResponse;
import com.vetx.starter.payload.auth.LoginRequest;
import com.vetx.starter.payload.auth.SignUpRequest;
import com.vetx.starter.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private AuthenticationManager authenticationManager;

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  private PasswordEncoder passwordEncoder;

  private JwtTokenProvider tokenProvider;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenProvider = tokenProvider;
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return ResponseEntity.ok(JwtAuthenticationResponse.builder().accessToken(tokenProvider.generateToken(authentication)).build());
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
          HttpStatus.BAD_REQUEST);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
          HttpStatus.BAD_REQUEST);
    }
    // Creating user's account
    // TODO: Replace with mapper
    User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
        signUpRequest.getEmail(), signUpRequest.getPassword());

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
        .orElseThrow(() -> new AppException("User Role not set."));

    user.setRoles(Collections.singleton(userRole));

    User result = userRepository.save(user);

    URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/api/users/{username}")
        .buildAndExpand(result.getUsername())
        .toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
  }
}