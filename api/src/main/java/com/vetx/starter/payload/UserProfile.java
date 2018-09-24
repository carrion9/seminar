package com.vetx.starter.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class UserProfile {
  private Long id;
  private String username;
  private String name;
  private Instant joinedAt;
}
