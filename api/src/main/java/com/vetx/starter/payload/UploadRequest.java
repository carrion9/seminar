package com.vetx.starter.payload;

import lombok.*;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UploadRequest {
  @NotBlank
  private Long seminarId;
  @NotBlank
  private StandardServletMultipartResolver file;
}
