package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.repository.SeminarTraineeRepository;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceDocService {

  private SeminarTraineeRepository seminarTraineeRepository;

  @Autowired
  public AttendanceDocService(SeminarTraineeRepository seminarTraineeRepository) {
    this.seminarTraineeRepository = seminarTraineeRepository;
  }

  public ByteArrayOutputStream createDocument(Seminar seminar, Specialty specialty) throws Exception {
    // Create a new document from scratch

    Map<Integer, String> header = new HashMap<>();
    header.put(0, "Α/Α");
    header.put(1, "ΕΠΩΝΥΜΟ");
    header.put(2, "ΟΝΟΜΑ");
    header.put(3, "ΟΝΟΜΑ ΠΑΤΡΟΣ");
    header.put(4, "ΑΡΙΘΜΟΣ ΕΓΓΡΑΦΟΥ ΤΑΥΤΟΠΡΟΣΩΠΙΑΣ");
    header.put(5, "ΥΠΟΓΡΑΦΗ");


    List<Contractor> contractors = seminarTraineeRepository.findDistinctContractorBySeminarAndSpecialty(seminar, specialty);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy").withZone(ZoneId.systemDefault());
    String templateName = seminar.getRefinery().equals(RefineryEnum.ELPE) ? "attendanceTemplate-elpe.docx" : "attendanceTemplate-motorOil.docx";

    try (XWPFDocument doc = new XWPFDocument(new ClassPathResource(templateName).getInputStream())) {
      // hack to remove existing paragraph in template
      doc.removeBodyElement(doc.getParagraphPos(0));

      // -- OR --
      // open an existing empty document with styles already defined
      //XWPFDocument doc = new XWPFDocument(new FileInputStream("base_document.docx"));

      for (Contractor contractor : contractors) {

        List<SeminarTrainee> seminarTrainees = seminarTraineeRepository.findAllBySeminarAndContractorAndSpecialty(seminar, contractor, specialty);

        // Create a new table with 6 rows and 3 columns
        int nRows = seminarTrainees.size() + 1; // number of trainees per contractor in current seminar
        int nCols = 6;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = paragraph.createRun();
        r1.setBold(true);
        r1.setColor("17365d");
        r1.setFontSize(16);
        r1.setCapitalized(true);
        r1.setItalic(true);
        r1.setText(MessageFormat.format("ΔΕΛΤΙΟ ΠΑΡΟΥΣΙΑΣ ΚΑΤΑΡΤΙΖΟΜΕΝΩΝ {0} {1}",contractor.getName(), formatter.format(seminar.getDate())));

        XWPFParagraph paragraph2 = doc.createParagraph();
        paragraph2.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r2 = paragraph2.createRun();
        r2.setBold(true);
        r2.setColor("c00000");
        r2.setFontSize(14);
        r2.setItalic(true);
        r2.setText(MessageFormat.format("Ενημερωτικό  Σεμινάριο «{0}» για τα {1} ",specialty.getName(), seminar.getRefinery().toString()));

        XWPFTable table = doc.createTable(nRows, nCols);
        table.setWidth("96.30%");
        paragraph.setPageBreak(true);

        // Set the table style. If the style is not defined, the table style
        // will become "Normal".
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTString styleStr = tblPr.addNewTblStyle();
        styleStr.setVal("StyledTable");

        // Get a list of the rows in the table
        List<XWPFTableRow> rows = table.getRows();
        int rowCt = 0;
        int colCt = 0;


        for (XWPFTableRow row : rows) {
          // get table row properties (trPr)
          CTTrPr trPr = row.getCtRow().addNewTrPr();
          // set row height; units = twentieth of a point, 360 = 0.25"
          CTHeight ht = trPr.addNewTrHeight();
          ht.setVal(BigInteger.valueOf(360));

          // get the cells in this row
          List<XWPFTableCell> cells = row.getTableCells();
          // add content to each cell
          for (XWPFTableCell cell : cells) {
            // get a table cell properties element (tcPr)
            CTTcPr tcpr = cell.getCTTc().addNewTcPr();
            // set vertical alignment to "center"
            CTVerticalJc va = tcpr.addNewVAlign();
            va.setVal(STVerticalJc.CENTER);

            // create cell color element
            CTShd ctshd = tcpr.addNewShd();
            ctshd.setColor("0070c0");
            ctshd.setVal(STShd.CLEAR);
            if (rowCt == 0) {
              // header row
              ctshd.setFill("fde9d9");
            }

            // get 1st paragraph in cell's paragraph list
            XWPFParagraph para = cell.getParagraphs().get(0);
            // create a run to contain the content
            XWPFRun rh = para.createRun();
            // style cell as desired
            rh.setFontSize(12);
            rh.setFontFamily("Calibri");
            if (rowCt == 0) {
              // header row
              rh.setText(header.get(colCt));
              rh.setBold(true);
              rh.setColor("0070c0");
              para.setAlignment(ParagraphAlignment.CENTER);
            } else {
              // other rows
              switch (colCt) {
                case 0:
                  cell.setWidth("05.00%");
                  rh.setText(Integer.toString(rowCt));
                  break;
                case 1:
                  cell.setWidth("23.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getSurname());
                  break;
                case 2:
                  cell.setWidth("16.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getName());
                  break;
                case 3:
                  cell.setWidth("16.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getFathersName());
                  break;
                case 4:
                  cell.setWidth("16.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getDocumentCode());
                  break;
                default:
                  cell.setWidth("24.00%");
                  rh.setText("");
              }
              para.setAlignment(ParagraphAlignment.LEFT);
            }
            colCt++;
          } // for cell
          colCt = 0;
          rowCt++;
        } // for row
      }
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      doc.write(bos);
      return bos;
    }
  }

  public ByteArrayOutputStream createDocument(Seminar seminar) throws Exception {
    // Create a new document from scratch

    Map<Integer, String> header = new HashMap<>();
    header.put(0, "Α/Α");
    header.put(1, "ΕΠΩΝΥΜΟ");
    header.put(2, "ΟΝΟΜΑ");
    header.put(3, "ΟΝΟΜΑ ΠΑΤΡΟΣ");
    header.put(4, "ΑΡΙΘΜΟΣ ΕΓΓΡΑΦΟΥ ΤΑΥΤΟΠΡΟΣΩΠΙΑΣ");
    header.put(5, "ΥΠΟΓΡΑΦΗ");



    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy").withZone(ZoneId.systemDefault());
    String templateName = seminar.getRefinery().equals(RefineryEnum.ELPE) ? "attendanceTemplate-elpe.docx" : "attendanceTemplate-motorOil.docx";

    try (XWPFDocument doc = new XWPFDocument(new ClassPathResource(templateName).getInputStream())) {
      // hack to remove existing paragraph in template
      doc.removeBodyElement(doc.getParagraphPos(0));

      // -- OR --
      // open an existing empty document with styles already defined
      //XWPFDocument doc = new XWPFDocument(new FileInputStream("base_document.docx"));

      for (SeminarSpecialty seminarSpecialty : seminar.getSeminarSpecialties()) {

        List<SeminarTrainee> seminarTrainees = seminarTraineeRepository.findAllBySeminarAndSpecialty(seminar, seminarSpecialty.getSpecialty());

        // Create a new table with 6 rows and 3 columns
        int nRows = seminarTrainees.size() + 1; // number of trainees per contractor in current seminar
        int nCols = 6;

        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = paragraph.createRun();
        r1.setBold(true);
        r1.setColor("17365d");
        r1.setFontSize(16);
        r1.setCapitalized(true);
        r1.setItalic(true);
        r1.setText(MessageFormat.format("ΔΕΛΤΙΟ ΠΑΡΟΥΣΙΑΣ ΚΑΤΑΡΤΙΖΟΜΕΝΩΝ {0} {1}",seminar.getName(), formatter.format(seminar.getDate())));

        XWPFParagraph paragraph2 = doc.createParagraph();
        paragraph2.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r2 = paragraph2.createRun();
        r2.setBold(true);
        r2.setColor("c00000");
        r2.setFontSize(14);
        r2.setItalic(true);
        r2.setText(MessageFormat.format("Ενημερωτικό  Σεμινάριο «{0}» για τα {1} ",seminarSpecialty.getSpecialty().getName(), seminar.getRefinery().toString()));

        XWPFTable table = doc.createTable(nRows, nCols);
        table.setWidth("96.30%");
        paragraph.setPageBreak(true);

        // Set the table style. If the style is not defined, the table style
        // will become "Normal".
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTString styleStr = tblPr.addNewTblStyle();
        styleStr.setVal("StyledTable");

        // Get a list of the rows in the table
        List<XWPFTableRow> rows = table.getRows();
        int rowCt = 0;
        int colCt = 0;


        for (XWPFTableRow row : rows) {
          // get table row properties (trPr)
          CTTrPr trPr = row.getCtRow().addNewTrPr();
          // set row height; units = twentieth of a point, 360 = 0.25"
          CTHeight ht = trPr.addNewTrHeight();
          ht.setVal(BigInteger.valueOf(360));

          // get the cells in this row
          List<XWPFTableCell> cells = row.getTableCells();
          // add content to each cell
          for (XWPFTableCell cell : cells) {
            // get a table cell properties element (tcPr)
            CTTcPr tcpr = cell.getCTTc().addNewTcPr();
            // set vertical alignment to "center"
            CTVerticalJc va = tcpr.addNewVAlign();
            va.setVal(STVerticalJc.CENTER);

            // create cell color element
            CTShd ctshd = tcpr.addNewShd();
            ctshd.setColor("0070c0");
            ctshd.setVal(STShd.CLEAR);
            if (rowCt == 0) {
              // header row
              ctshd.setFill("fde9d9");
            }

            // get 1st paragraph in cell's paragraph list
            XWPFParagraph para = cell.getParagraphs().get(0);
            // create a run to contain the content
            XWPFRun rh = para.createRun();
            // style cell as desired
            rh.setFontSize(12);
            rh.setFontFamily("Calibri");
            if (rowCt == 0) {
              // header row
              rh.setText(header.get(colCt));
              rh.setBold(true);
              rh.setColor("0070c0");
              para.setAlignment(ParagraphAlignment.CENTER);
            } else {
              // other rows
              switch (colCt) {
                case 0:
                  cell.setWidth("05.00%");
                  rh.setText(Integer.toString(rowCt));
                  break;
                case 1:
                  cell.setWidth("23.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getSurname());
                  break;
                case 2:
                  cell.setWidth("16.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getName());
                  break;
                case 3:
                  cell.setWidth("16.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getFathersName());
                  break;
                case 4:
                  cell.setWidth("16.00%");
                  rh.setText(seminarTrainees.get(rowCt-1).getTrainee().getDocumentCode());
                  break;
                default:
                  cell.setWidth("24.00%");
                  rh.setText("");
              }
              para.setAlignment(ParagraphAlignment.LEFT);
            }
            colCt++;
          } // for cell
          colCt = 0;
          rowCt++;
        } // for row
      }
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      doc.write(bos);
      return bos;
    }
  }
}
