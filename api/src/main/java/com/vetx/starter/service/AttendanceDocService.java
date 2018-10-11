package com.vetx.starter.service;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarTrainee;
import com.vetx.starter.model.Specialty;
import com.vetx.starter.repository.SeminarTraineeRepository;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceDocService {

  private SeminarTraineeRepository seminarTraineeRepository;

  @Autowired
  public AttendanceDocService(SeminarTraineeRepository seminarTraineeRepository) {
    this.seminarTraineeRepository = seminarTraineeRepository;
  }

  /**
   * Create a table with some row and column styling. I "manually" add the
   * style name to the table, but don't check to see if the style actually
   * exists in the document. Since I'm creating it from scratch, it obviously
   * won't exist. When opened in MS Word, the table style becomes "Normal".
   * I manually set alternating row colors. This could be done using Themes,
   * but that's left as an exercise for the reader. The cells in the last
   * column of the table have 10pt. "Courier" font.
   * I make no claims that this is the "right" way to do it, but it worked
   * for me. Given the scarcity of XWPF examples, I thought this may prove
   * instructive and give you ideas for your own solutions.
   */
  public void createStyledTable(Seminar seminar, Specialty specialty) throws Exception {
    // Create a new document from scratch

    Map<Integer, String> header = new HashMap<>();
    header.put(0, "Α/Α");
    header.put(1, "ΕΠΩΝΥΜΟ");
    header.put(2, "ΟΝΟΜΑ");
    header.put(3, "ΟΝΟΜΑ ΠΑΤΡΟΣ");
    header.put(4, "ΑΡΙΘΜΟΣ ΕΓΓΡΑΦΟΥ ΤΑΥΤΟΠΡΟΣΩΠΙΑΣ");
    header.put(5, "ΥΠΟΓΡΑΦΗ");


    List<SeminarTrainee> seminarTraineeContractors = seminarTraineeRepository.findDistinctBySeminarAndSpecialty(seminar, specialty);

    try (XWPFDocument doc = new XWPFDocument()) {
      // -- OR --
      // open an existing empty document with styles already defined
      //XWPFDocument doc = new XWPFDocument(new FileInputStream("base_document.docx"));

      for (SeminarTrainee traineeContractor : seminarTraineeContractors) {

        List<SeminarTrainee> seminarTrainees = seminarTraineeRepository.findAllBySeminarAndContractorAndSpecialty(seminar, traineeContractor.getContractor(), specialty);
//        seminarTrainees.stream().collect(Collectors.toMap(SeminarTrainee::getId, ))

        // Create a new table with 6 rows and 3 columns
        int nRows = seminarTrainees.size(); // number of trainees per contractor in current seminar
        int nCols = 6;
        XWPFTable table = doc.createTable(nRows, nCols);

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
            ctshd.setColor("auto");
            ctshd.setVal(STShd.CLEAR);
            if (rowCt == 0) {
              // header row
              ctshd.setFill("A7BFDE");
            } else if (rowCt % 2 == 0) {
              // even row
              ctshd.setFill("D3DFEE");
            } else {
              // odd row
              ctshd.setFill("EDF2F8");
            }

            // get 1st paragraph in cell's paragraph list
            XWPFParagraph para = cell.getParagraphs().get(0);
            // create a run to contain the content
            XWPFRun rh = para.createRun();
            // style cell as desired
            if (colCt == nCols - 1) {
              // last column is 10pt Courier
              rh.setFontSize(10);
              rh.setFontFamily("Courier");
            }
            if (rowCt == 0) {
              // header row
              rh.setText(header.get(colCt));
              rh.setBold(true);
              para.setAlignment(ParagraphAlignment.CENTER);
            } else {
              // other rows
              rh.setText("row " + rowCt + ", col " + colCt);
              para.setAlignment(ParagraphAlignment.LEFT);
            }
            colCt++;
          } // for cell
          colCt = 0;
          rowCt++;
        } // for row
      }

      // write the file
      try (OutputStream out = new FileOutputStream("styledTable.docx")) {
        doc.write(out);
      }
    }
  }
}