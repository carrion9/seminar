package com.vetx.starter.service;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarContractorTraineeSpecialty;
import com.vetx.starter.repository.SeminarTraineeRepository;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WelcomeDocService {

  private SeminarTraineeRepository seminarTraineeRepository;

  @Autowired
  public WelcomeDocService(SeminarTraineeRepository seminarTraineeRepository) {
    this.seminarTraineeRepository = seminarTraineeRepository;
  }

  public void createDocument(Seminar seminar) throws Exception {
    List<SeminarContractorTraineeSpecialty> seminarContractorTraineeSpecialties = seminarTraineeRepository.findDistinctBySeminar(seminar);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy").withZone(ZoneId.systemDefault());

    try (XWPFDocument doc = new XWPFDocument(new FileInputStream("welcomeTemplate.docx"))) {

      try (OutputStream out = new FileOutputStream("welcomeDocument.docx")) {
        doc.write(out);
      }
    }

  }
}
