package com.vetx.starter.model;

import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "SeminarContractorSubRefineryWithRefinery", types = SeminarContractorSubRefinery.class)
public interface SeminarContractorSubRefineryWithRefinery {
  Long getKey();
  SubRefineryProjection getSubRefinery();
}
