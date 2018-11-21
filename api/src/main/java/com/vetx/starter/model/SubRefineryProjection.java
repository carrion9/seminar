package com.vetx.starter.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "SubRefineryProjection", types = SubRefinery.class)
public interface SubRefineryProjection {
  Long getKey();
  String getName();
}
