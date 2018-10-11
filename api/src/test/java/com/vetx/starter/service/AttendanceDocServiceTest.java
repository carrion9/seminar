package com.vetx.starter.service;

import com.vetx.starter.model.Specialty;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.repository.SpecialtyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AttendanceDocServiceTest {

  @Autowired
  AttendanceDocService attendanceDocService;

  @Autowired
  SeminarRepository seminarRepository;

  @Autowired
  SpecialtyRepository specialtyRepository;

  @Test
  public void createStyledTable() throws Exception {
    attendanceDocService.createStyledTable(seminarRepository.getOne(1L), specialtyRepository.getOne(1L));
  }
}