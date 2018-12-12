package com.vetx.starter.service;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarTrainee;
import com.vetx.starter.repository.SeminarTraineeRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class WelcomeDocService {

  private SeminarTraineeRepository seminarTraineeRepository;

  @Autowired
  public WelcomeDocService(SeminarTraineeRepository seminarTraineeRepository) {
    this.seminarTraineeRepository = seminarTraineeRepository;
  }

  public ByteArrayOutputStream createDocument(Seminar seminar) throws Exception {
    List<SeminarTrainee> seminarTrainees = seminarTraineeRepository.findDistinctBySeminar(seminar);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy").withZone(ZoneId.systemDefault());

    try (XWPFDocument doc = new XWPFDocument(new ClassPathResource("welcomeTemplate.docx").getInputStream())) {
      for (SeminarTrainee seminarTrainee : seminarTrainees) {
        firstTable(doc);

        doc.createParagraph();

        insertTraineePicture(doc, seminarTrainee);

        XWPFParagraph lastParagraph = doc.createParagraph();
        lastParagraph.setPageBreak(true);


      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      doc.write(bos);
      return bos;
    }

  }

  private void insertTraineePicture(XWPFDocument doc, SeminarTrainee seminarTrainee) throws IOException, InvalidFormatException {
    XWPFParagraph p = doc.createParagraph();
    p.setAlignment(ParagraphAlignment.RIGHT);
    XWPFRun r2 = p.createRun();

    String imgFile = seminarTrainee.getTrainee().getImageLocation();
    if (imgFile != null) {
      int format = XWPFDocument.PICTURE_TYPE_JPEG;

      if (imgFile.endsWith(".emf")) {
        format = XWPFDocument.PICTURE_TYPE_EMF;
      } else if (imgFile.endsWith(".wmf")) {
        format = XWPFDocument.PICTURE_TYPE_WMF;
      } else if (imgFile.endsWith(".pict")) {
        format = XWPFDocument.PICTURE_TYPE_PICT;
      } else if (imgFile.endsWith(".png")) {
        format = XWPFDocument.PICTURE_TYPE_PNG;
      } else if (imgFile.endsWith(".dib")) {
        format = XWPFDocument.PICTURE_TYPE_DIB;
      } else if (imgFile.endsWith(".gif")) {
        format = XWPFDocument.PICTURE_TYPE_GIF;
      } else if (imgFile.endsWith(".tiff")) {
        format = XWPFDocument.PICTURE_TYPE_TIFF;
      } else if (imgFile.endsWith(".eps")) {
        format = XWPFDocument.PICTURE_TYPE_EPS;
      } else if (imgFile.endsWith(".bmp")) {
        format = XWPFDocument.PICTURE_TYPE_BMP;
      } else if (imgFile.endsWith(".wpg")) {
        format = XWPFDocument.PICTURE_TYPE_WPG;
      }

      try (FileInputStream is = new FileInputStream(imgFile.replace("traineeImageUpload", "/upload-dir"))) {
        r2.addPicture(is, format, imgFile, Units.toEMU(50), Units.toEMU(100));// 200x200 pixels

      }

    }
  }

  private void firstTable(XWPFDocument doc) {
    XWPFTable table = doc.createTable(5, 1);
    table.setWidth(6000);
//    table.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 100, 40, "000000");
//    table.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, 100, 10, "000000");
//    table.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, 100, 10, "000000");
//    table.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, 100, 10, "000000");

    table.getRows().forEach(xwpfTableRow -> xwpfTableRow.getTableCells().forEach(xwpfTableCell -> {
      CTTcPr tcpr = xwpfTableCell.getCTTc().addNewTcPr();
      // set vertical alignment to "center"
      CTVerticalJc va = tcpr.addNewVAlign();
      va.setVal(STVerticalJc.CENTER);

      // create cell color element
      CTShd ctshd = tcpr.addNewShd();
      ctshd.setColor("0070c0");
      ctshd.setVal(STShd.CLEAR);
    }));


    table.getRow(0).getCell(0).setText("Επωνυμία Εργοδότη: ");
    table.getRow(1).getCell(0).setText("Ημερομηνία: ");
    table.getRow(2).getCell(0).setText("Για τις Εγκαταστάσεις των: ");
    table.getRow(3).getCell(0).setText("Ημερομηνία Σεμιναρίου Βασικού Επιπέδου: ");
    table.getRow(4).getCell(0).setText("Βαθμολογία Βασικού Επιπέδου: ");


  }
}
