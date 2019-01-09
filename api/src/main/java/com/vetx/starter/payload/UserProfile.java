package com.vetx.starter.payload;

import com.vetx.starter.model.auth.Role;
import com.vetx.starter.model.auth.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class UserProfile extends ResourceSupport {
  private Long key;
  private String username;
  private String name;
  private String email;
  @Builder.Default
  private Set<Role> roles = new HashSet<>();
  private Instant joinedAt;

  public static UserProfile fromEntity(User user) {
    return UserProfile.builder()
        .key(user.getId())
        .username(user.getUsername())
        .name(user.getName())
        .email(user.getEmail())
        .roles(user.getRoles())
        .joinedAt(user.getCreatedAt()).build();
  }
}
