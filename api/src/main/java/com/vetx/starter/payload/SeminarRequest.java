package com.vetx.starter.payload;

import com.vetx.starter.model.SeminarType;
import com.vetx.starter.model.SpecialityName;
import lombok.Data;

import java.time.Instant;
import java.util.Collection;

@Data
public class SeminarRequest {
  private String name;
  private Instant date;
  private SeminarType seminarType;
  private Collection<SpecialityName> specialityNames;
}
