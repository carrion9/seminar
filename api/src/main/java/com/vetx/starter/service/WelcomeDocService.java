package com.vetx.starter.service;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarTrainee;
import com.vetx.starter.repository.SeminarTraineeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class WelcomeDocService {

  private SeminarTraineeRepository seminarTraineeRepository;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy").withZone(ZoneId.systemDefault());

  private static String cTAbstractNumBulletXML =
      "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
          + "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
          + "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
          + "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
          + "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
          + "</w:abstractNum>";

  @Autowired
  public WelcomeDocService(SeminarTraineeRepository seminarTraineeRepository) {
    this.seminarTraineeRepository = seminarTraineeRepository;
  }

  public ByteArrayOutputStream createDocument(Seminar seminar) throws Exception {
    List<SeminarTrainee> seminarTrainees = seminarTraineeRepository.findBySeminar(seminar);
    seminarTrainees = new ArrayList<>(seminarTrainees.stream().collect(toMap(SeminarTrainee::getTrainee, p -> p, (p, q) -> p)).values());
    LinkedList<String> seminarSpecialties = seminar
        .getSeminarSpecialties()
        .stream()
        .map(seminarSpecialty -> seminarSpecialty.getSpecialty().getName())
        .collect(Collectors.toCollection(LinkedList::new));

    try (XWPFDocument doc = new XWPFDocument(new ClassPathResource("welcomeTemplate.docx").getInputStream())) {
      for (SeminarTrainee seminarTrainee : seminarTrainees) {
        seminarDetailsTable(doc, seminarTrainee);

        addBreakLines(doc, 1);

        traineeDetailsTable(doc, seminarTrainee);
        addBreakLines(doc, 1);

        specialtyDetailsTable(doc,seminarTrainee, seminarSpecialties);
        addDisclaimer(doc);

//        insertTraineePicture(doc, seminarTrainee);

        XWPFParagraph lastParagraph = doc.createParagraph();
        lastParagraph.setAlignment(ParagraphAlignment.CENTER);
        lastParagraph.setPageBreak(true);

        traineeDetailsTable(doc, seminarTrainee);
        addBreakLines(doc, 1);

        traineeSpecialtiesTable(doc,seminarTrainee);

        lastParagraph = doc.createParagraph();
        lastParagraph.setAlignment(ParagraphAlignment.CENTER);
        lastParagraph.setPageBreak(true);

      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      doc.write(bos);
      return bos;
    }

  }

  private void traineeSpecialtiesTable(XWPFDocument doc, SeminarTrainee seminarTrainee) {
    List<String> traineeSpecialties = seminarTraineeRepository
        .findByTraineeAndSeminar(seminarTrainee.getTrainee(), seminarTrainee.getSeminar())
        .stream()
        .map(seminarTraineeSp -> seminarTraineeSp.getSpecialty().getName())
        .collect(Collectors.toList());

    List<String> headings = new ArrayList<>();
    headings.add("Ειδικότητες");
    headings.add("Βαθμολογία");
    headings.add("Σωστές");
    headings.add("Λάθος");
    headings.add("%");
    headings.add("Τελική Βαθμ.");

    XWPFTable table = doc.createTable(traineeSpecialties.size() + 2, 6);


    // Set the table style. If the style is not defined, the table style
    // will become "Normal".
    CTTblPr tblPr = table.getCTTbl().getTblPr();
    CTString styleStr = tblPr.addNewTblStyle();
    styleStr.setVal("Table Grid 1");

    // Get a list of the rows in the table
    List<XWPFTableRow> rows = table.getRows();

    for (int i = 0; i < rows.size(); i++) {
      XWPFTableRow row = rows.get(i);
      row.setHeight(1);
      // get the cells in this row
      List<XWPFTableCell> cells = row.getTableCells();
      // add content to each cell
      for (int i1 = 0; i1 < cells.size(); i1++) {
        XWPFTableCell cell = cells.get(i1);
        // get a table cell properties element (tcPr)
        CTTcPr tcpr = cell.getCTTc().addNewTcPr();
        // set vertical alignment to "center"
        CTVerticalJc va = tcpr.addNewVAlign();
        va.setVal(STVerticalJc.CENTER);

        // create cell color element
        CTShd ctshd = tcpr.addNewShd();
        ctshd.setColor("0070c0");
        ctshd.setVal(STShd.CLEAR);
        // get 1st paragraph in cell's paragraph list
        XWPFParagraph para = cell.getParagraphs().get(0);
        // create a run to contain the content
        XWPFRun rh = para.createRun();
        if (i == 0) {
          if (i1 == 0) {
            // header row
            rh.setText("Ειδικότητες για τις οποίες έχει εξεταστεί");
            rh.setBold(true);
            rh.setColor("0070c0");
          }
        } else if (i == 1) {
          rh.setText(headings.get(i1));
          rh.setBold(true);
          rh.setColor("0070c0");

        }
        else {
          if (i1 == 0) {
            rh.setText(traineeSpecialties.get(i-2));
          }
        }
        para.setAlignment(ParagraphAlignment.LEFT);

        // style cell as desired
      }
    }
    //merge heading
    CTHMerge hMerge = CTHMerge.Factory.newInstance();
    hMerge.setVal(STMerge.RESTART);
    table.getRow(0).getCell(0).getCTTc().getTcPr().setHMerge(hMerge);
    table.getRow(0).getCell(1).getCTTc().getTcPr().setHMerge(hMerge);
  }

  private void addDisclaimer(XWPFDocument doc) throws XmlException {
    List<String> disclaimers = new ArrayList<>();
    disclaimers.add("Τα στοιχεία της παρούσας αίτησης είναι αληθή.");
    disclaimers.add("Γνωρίζει ότι η επιτυχής ολοκλήρωση του τεστ αξιολόγησης είναι ένα από τα μέτρα ασφαλείας των εγκαταστάσεων των " +
            "Διυλιστηρίων, και δεν αποτελεί πιστοποίηση ικανοτήτων του εξεταζόμενου.");
    disclaimers.add("Εξουσιοδοτεί το Φορέα να παράσχει χωρίς ειδοποίηση οποιαδήποτε πληροφορία  σχετικά με τα αποτελέσματα της " +
            "διαδικασίας αξιολόγησης που την/τον αφορά στις άμεσα  ενδιαφερόμενες εταιρείες (Διυλιστήριο στο οποίο θα εργαστεί και " +
            "στην Εργολαβική εταιρεία που ανήκει).");
    disclaimers.add("Δεν θα αποκαλύψει σε φυσικό ή νομικό πρόσωπο οποιαδήποτε πληροφορία που να αφορά την διαδικασία αξιολόγησης " +
            "στην οποία  θα λάβει μέρος.");

    XWPFParagraph p = doc.createParagraph();
    p.setAlignment(ParagraphAlignment.LEFT);
    XWPFRun run = p.createRun();

    run.setText("Ο/Η αιτών δηλώνει ότι :");
    run.setItalic(true);

    CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
    CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
    XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
    XWPFNumbering numbering = doc.createNumbering();
    BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);

    BigInteger numID = numbering.addNum(abstractNumID);
    for (int i = 0; i < 4; i++) {
      p = doc.createParagraph();
      p.setNumID(numID);
      run = p.createRun();
      run.setText(disclaimers.get(i));
      run.setItalic(true);
      p.setSpacingAfter(0);
    }

  }

  private void addBreakLines(XWPFDocument doc, int numberOfBreakLines) {
    XWPFParagraph mainParagraph = doc.createParagraph();
    mainParagraph.setAlignment(ParagraphAlignment.LEFT);
    XWPFRun mainRun = mainParagraph.createRun();
    for (int i = 0 ; i< numberOfBreakLines ; i++) {
      mainRun.addBreak();
    }
  }

  private void insertTraineePicture(XWPFDocument doc, SeminarTrainee seminarTrainee) throws IOException, InvalidFormatException {
    XWPFParagraph p = doc.createParagraph();
    p.setAlignment(ParagraphAlignment.RIGHT);
    XWPFRun r = p.createRun();

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
        r.addPicture(is, format, imgFile, Units.toEMU(50), Units.toEMU(100));// 200x200 pixels

      }

    }
  }

  private void seminarDetailsTable(XWPFDocument doc, SeminarTrainee seminarTrainee) {
    String subRefineriesNames = seminarTrainee
        .getSeminar()
        .getSeminarContractors()
        .stream()
        .filter(seminarContractor -> seminarContractor.getContractor().equals(seminarTrainee.getContractor()))
        .findFirst()
        .get()
        .getSeminarContractorSubRefineries()
        .stream()
        .map(seminarContractorSubRefinery -> seminarContractorSubRefinery.getSubRefinery().getName())
        .collect(Collectors.joining("/"));
    LinkedList<String> input = new LinkedList<>();
    input.add("Επωνυμία Εργοδότη: " + seminarTrainee.getContractor().getName());
    input.add("Ημερομηνία: " + seminarTrainee.getSeminar().getDate().format(formatter));
    input.add("Για τις Εγκαταστάσεις των: " + subRefineriesNames);
    input.add("Ημερομηνία Σεμιναρίου Βασικού Επιπέδου: " + seminarTrainee.getSeminar().getDate().format(formatter));
    input.add("Βαθμολογία Βασικού Επιπέδου: " );  // I believe it has to be blank

    XWPFTable table = doc.createTable(5, 1);

    // Set the table style. If the style is not defined, the table style
    // will become "Normal".
    CTTblPr tblPr = table.getCTTbl().getTblPr();
    CTString styleStr = tblPr.addNewTblStyle();
    styleStr.setVal("Table Grid 1");

    // Get a list of the rows in the table
    List<XWPFTableRow> rows = table.getRows();

    for (XWPFTableRow row : rows) {
      row.setHeight(1);
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
        // get 1st paragraph in cell's paragraph list
        XWPFParagraph para = cell.getParagraphs().get(0);
        // create a run to contain the content
        XWPFRun rh = para.createRun();
        // style cell as desired
        rh.setText(input.pop());
        para.setAlignment(ParagraphAlignment.LEFT);
      }
    }
  }

  private void traineeDetailsTable(XWPFDocument doc, SeminarTrainee seminarTrainee) {
    LinkedList<String> input = new LinkedList<>();
    input.add("Επώνυμο: " + seminarTrainee.getTrainee().getSurname());
    input.add("Όνομα: " + seminarTrainee.getTrainee().getName());
    input.add("Όνομα Πατρός: " +  seminarTrainee.getTrainee().getFathersName());
    input.add("ΑΜΑ: " + seminarTrainee.getTrainee().getAma());
    input.add("Αριθμός Εγγράφου Ταυτοπροσωπίας: " + seminarTrainee.getTrainee().getDocumentCode());  // I believe it has to be blank
    input.add("Τύπος Εγγράφου: " + StringUtils.capitalize(seminarTrainee.getTrainee().getDocType().toString()));

    XWPFTable table = doc.createTable(4, 2);

    // Set the table style. If the style is not defined, the table style
    // will become "Normal".
    CTTblPr tblPr = table.getCTTbl().getTblPr();
    CTString styleStr = tblPr.addNewTblStyle();
    styleStr.setVal("Table Grid 1");

    // Get a list of the rows in the table
    List<XWPFTableRow> rows = table.getRows();

    for (int i = 0; i < rows.size(); i++) {
      XWPFTableRow row = rows.get(i);
      row.setHeight(1);
      // get the cells in this row
      List<XWPFTableCell> cells = row.getTableCells();
      // add content to each cell
      for (int i1 = 0; i1 < cells.size(); i1++) {
        XWPFTableCell cell = cells.get(i1);
        // get a table cell properties element (tcPr)
        CTTcPr tcpr = cell.getCTTc().addNewTcPr();
        // set vertical alignment to "center"
        CTVerticalJc va = tcpr.addNewVAlign();
        va.setVal(STVerticalJc.CENTER);

        // create cell color element
        CTShd ctshd = tcpr.addNewShd();
        ctshd.setColor("0070c0");
        ctshd.setVal(STShd.CLEAR);
        // get 1st paragraph in cell's paragraph list
        XWPFParagraph para = cell.getParagraphs().get(0);
        // create a run to contain the content
        XWPFRun rh = para.createRun();
        if (i == 0) {
          if (i1 == 0) {
            // header row
            rh.setText("Στοιχεία Εργαζόμενου");
            rh.setBold(true);
            rh.setColor("0070c0");
          }
        } else {
          rh.setText(input.pop());
        }
        para.setAlignment(ParagraphAlignment.LEFT);

        // style cell as desired
      }
    }
    //merge heading
    CTHMerge hMerge = CTHMerge.Factory.newInstance();
    hMerge.setVal(STMerge.RESTART);
    table.getRow(0).getCell(0).getCTTc().getTcPr().setHMerge(hMerge);
    table.getRow(0).getCell(1).getCTTc().getTcPr().setHMerge(hMerge);

  }

  private void specialtyDetailsTable(XWPFDocument doc, SeminarTrainee seminarTrainee, LinkedList<String> seminarSpecialties) {
    List<String> traineeSpecialties = seminarTraineeRepository
        .findByTraineeAndSeminar(seminarTrainee.getTrainee(), seminarTrainee.getSeminar())
        .stream()
        .map(seminarTraineeSp -> seminarTraineeSp.getSpecialty().getName())
        .collect(Collectors.toList());

    LinkedList<String> copySeminarSpecialties = new LinkedList<>();
    copySeminarSpecialties.addAll(seminarSpecialties);

    XWPFTable table = doc.createTable(seminarTrainee.getSeminar().getSeminarSpecialties().size() /2 + 1, 4);


    // Set the table style. If the style is not defined, the table style
    // will become "Normal".
    CTTblPr tblPr = table.getCTTbl().getTblPr();
    CTString styleStr = tblPr.addNewTblStyle();
    styleStr.setVal("Table Grid 1");

    // Get a list of the rows in the table
    List<XWPFTableRow> rows = table.getRows();

    for (int i = 0; i < rows.size(); i++) {
      XWPFTableRow row = rows.get(i);
      row.setHeight(1);
      // get the cells in this row
      List<XWPFTableCell> cells = row.getTableCells();
      // add content to each cell
      for (int i1 = 0; i1 < cells.size(); i1++) {
        XWPFTableCell cell = cells.get(i1);
        // get a table cell properties element (tcPr)
        CTTcPr tcpr = cell.getCTTc().addNewTcPr();
        // set vertical alignment to "center"
        CTVerticalJc va = tcpr.addNewVAlign();
        va.setVal(STVerticalJc.CENTER);

        // create cell color element
        CTShd ctshd = tcpr.addNewShd();
        ctshd.setColor("0070c0");
        ctshd.setVal(STShd.CLEAR);
        // get 1st paragraph in cell's paragraph list
        XWPFParagraph para = cell.getParagraphs().get(0);
        // create a run to contain the content
        XWPFRun rh = para.createRun();
        if (i == 0) {
          if (i1 == 0) {
            // header row
            rh.setText("Ειδικότητες για τις οποίες θέλει να εξεταστεί");
            rh.setBold(true);
            rh.setColor("0070c0");
          }
        } else {
          String tempSpecialty = copySeminarSpecialties.get(0);
          if (i1 % 2 == 0) {
            rh.setText(tempSpecialty);
          }
          else {
            if (traineeSpecialties.contains(tempSpecialty)) {
              rh.setText("X");
            }
            copySeminarSpecialties.pop();
          }
        }
        para.setAlignment(ParagraphAlignment.LEFT);

        // style cell as desired
      }
    }
    //merge heading
    CTHMerge hMerge = CTHMerge.Factory.newInstance();
    hMerge.setVal(STMerge.RESTART);
    table.getRow(0).getCell(0).getCTTc().getTcPr().setHMerge(hMerge);
    table.getRow(0).getCell(1).getCTTc().getTcPr().setHMerge(hMerge);

  }
}
