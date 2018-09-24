package com.vetx.starter.payload.auth;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthenticationResponse {
  @NonNull
  private String accessToken;
  @Builder.Default
  private String tokenType = "Bearer";
}
