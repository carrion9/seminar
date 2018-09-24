package com.vetx.starter.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserSummary {
  private Long id;
  private String username;
  private String name;
}