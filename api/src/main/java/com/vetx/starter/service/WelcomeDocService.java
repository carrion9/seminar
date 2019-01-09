package com.vetx.starter.service;

import com.vetx.starter.model.DocType;
import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarTrainee;
import com.vetx.starter.repository.SeminarTraineeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Service
public class WelcomeDocService {

  private SeminarTraineeRepository seminarTraineeRepository;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy").withZone(ZoneId.systemDefault());

  private Map<DocType, String> docTypeMapping = Stream.of(
      new AbstractMap.SimpleImmutableEntry<>(DocType.IDENTITY, "ΤΑΥΤΟΤΗΤΑ"),
      new AbstractMap.SimpleImmutableEntry<>(DocType.DRIVING_LICENSE, "ΔΙΠΛΩΜΑ ΟΔΗΓΗΣΗΣ"),
      new AbstractMap.SimpleImmutableEntry<>(DocType.PASSPORT, "ΔΙΑΒΑΤΗΡΙΟ"),
      new AbstractMap.SimpleImmutableEntry<>(DocType.NONE, "N/A"))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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

    try (XWPFDocument doc = new XWPFDocument(new ClassPathResource("welcomeTemplate2.docx").getInputStream())) {
      // hack to remove existing paragraph in template
      doc.removeBodyElement(doc.getParagraphPos(0));
      initHeaderFooter(doc);

      for (SeminarTrainee seminarTrainee : seminarTrainees) {
        insertTraineePicture(doc, seminarTrainee);
        seminarDetailsTable(doc, seminarTrainee);

        addBreakLines(doc, 1);

        traineeDetailsTable(doc, seminarTrainee);
        addBreakLines(doc, 1);

        specialtyDetailsTable(doc, seminarTrainee, seminarSpecialties);
        addDisclaimer(doc);

        XWPFParagraph lastParagraph = doc.createParagraph();
        lastParagraph.setAlignment(ParagraphAlignment.CENTER);
        lastParagraph.setPageBreak(true);

        traineeDetailsTable(doc, seminarTrainee);
        addBreakLines(doc, 1);

        traineeSpecialtiesTable(doc, seminarTrainee);

        lastParagraph = doc.createParagraph();
        lastParagraph.setAlignment(ParagraphAlignment.CENTER);
        lastParagraph.setPageBreak(true);

      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      doc.write(bos);
      return bos;
    }

  }

  private void initHeaderFooter(XWPFDocument doc) throws NoSuchFieldException, IllegalAccessException {
    // create header/footer functions insert an empty paragraph
    XWPFHeader head = doc.createHeader(HeaderFooterType.DEFAULT);
    head.createParagraph().createRun().setFontSize(14);
    head.getParagraphs().get(0).getRuns().get(0).setColor("191970");
    head.getParagraphs().get(0).getRuns().get(0).setText("ΕΝΤΥΠΟ ΥΠΟΔΟΧΗΣ ΥΠΟΨΗΦΙΟΥ                                                       Αρ. Θέσης: ");
//    head.getParagraphs().get(0).getCTP().addNewFldSimple().setInstr("PAGE / 2 \\* MERGEFORMAT");

    XWPFFooter foot = doc.createFooter(HeaderFooterType.DEFAULT);
    foot.createParagraph().createRun().setText("Ονοματεπώνυμο Αξιολογητή:                                                                     Υπογραφή:");

    head = doc.createHeader(HeaderFooterType.EVEN);
    head.createParagraph().createRun().setFontSize(14);
    head.getParagraphs().get(0).getRuns().get(0).setColor("191970");
    head.getParagraphs().get(0).getRuns().get(0).setText("ΦΥΛΛΟ ΒΑΘΜΟΛΟΓΙΑΣ                                                                     Αρ. Θέσης: ");


    Field _settings = XWPFDocument.class.getDeclaredField("settings");
    _settings.setAccessible(true);
    XWPFSettings xwpfsettings = (XWPFSettings)_settings.get(doc);
    Field _ctSettings = XWPFSettings.class.getDeclaredField("ctSettings");
    _ctSettings.setAccessible(true);
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings ctsettings =
        (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings)_ctSettings.get(xwpfsettings);

    ctsettings.addNewEvenAndOddHeaders();

    //
// head.getParagraphs().get(0).getCTP().addNewFldSimple().setInstr("PAGE / 2 \\* MERGEFORMAT");

//    head = doc.createHeader(HeaderFooterType.EVEN);
//    head.createParagraph().createRun().setFontSize(14);
//    head.getParagraphs().get(0).getRuns().get(0).setColor("191970");
//    head.getParagraphs().get(0).getRuns().get(0).setText("ΕΝΤΥΠΟ ΥΠΟΔΟΧΗΣ ΥΠΟΨΗΦΙΟΥ                                                       Αρ. Θέσης: ");
//    head.getParagraphs().get(0).getCTP().addNewFldSimple().setInstr("PAGE / 2 \\* MERGEFORMAT");
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

    XWPFTable table = doc.createTable(traineeSpecialties.size() * 2 + 2, 6);
    table.setWidth("98.6%");

    // Set the table style. If the style is not defined, the table style
    // will become "Normal".
    CTTblPr tblPr = table.getCTTbl().getTblPr();
    CTString styleStr = tblPr.addNewTblStyle();
    styleStr.setVal("Table Grid 1");

    // Get a list of the rows in the table
    List<XWPFTableRow> rows = table.getRows();

    for (int rowPos = 0; rowPos < rows.size(); rowPos++) {
      XWPFTableRow row = rows.get(rowPos);
      row.setHeight(1);
      // get the cells in this row
      List<XWPFTableCell> cells = row.getTableCells();
      // add content to each cell
      for (int cellPos = 0; cellPos < cells.size(); cellPos++) {
        XWPFTableCell cell = cells.get(cellPos);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
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
        if (rowPos == 0) {
          if (cellPos == 0) {
            // header row
            rh.setText("Ειδικότητες για τις οποίες έχει εξεταστεί");
            rh.setBold(true);
            rh.setColor("0070c0");
            CTHMerge hMerge = CTHMerge.Factory.newInstance();
            hMerge.setVal(STMerge.RESTART);
            cell.getCTTc().getTcPr().setHMerge(hMerge);
          } else {
            CTHMerge hMerge = CTHMerge.Factory.newInstance();
            hMerge.setVal(STMerge.CONTINUE);
            cell.getCTTc().getTcPr().setHMerge(hMerge);
          }
        } else if (rowPos == 1) {
          rh.setText(headings.get(cellPos));
          rh.setBold(true);
          rh.setColor("0070c0");

        } else if (rowPos % 2 == 0) {
          if (cellPos == 0) {
            rh.setText(traineeSpecialties.get((rowPos - 2) / 2));
          }
          if (cellPos == 0 || cellPos == cells.size() -1) {
            CTVMerge vMerge = CTVMerge.Factory.newInstance();
            vMerge.setVal(STMerge.RESTART);
            cell.getCTTc().getTcPr().setVMerge(vMerge);
          }
        } else {
          if (cellPos == 0  || cellPos == cells.size() -1) {
            CTVMerge vMerge = CTVMerge.Factory.newInstance();
            vMerge.setVal(STMerge.CONTINUE);
            cell.getCTTc().getTcPr().setVMerge(vMerge);
          }
        }
        para.setAlignment(ParagraphAlignment.LEFT);

        // style cell as desired
      }
    }
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
    for (int i = 0; i < numberOfBreakLines; i++) {
      mainRun.addBreak();
    }
  }

  private void insertTraineePicture(XWPFDocument doc, SeminarTrainee seminarTrainee) throws Exception {
    XWPFParagraph paragraph = doc.createParagraph();
    paragraph.setAlignment(ParagraphAlignment.RIGHT);
    XWPFRun run = paragraph.createRun();

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
      try (FileInputStream in = new FileInputStream(imgFile.replace("traineeImageUpload", "/upload-dir"))) {
        run.addPicture(in, format, imgFile, Units.toEMU(100), Units.toEMU(100));
        in.close();
        CTDrawing drawing = run.getCTR().getDrawingArray(0);

        CTAnchor anchor = getAnchorWithGraphic(drawing, imgFile, false /*behind text*/);

        drawing.setAnchorArray(new CTAnchor[] {anchor});
        drawing.removeInline(0);
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
        .collect(Collectors.joining(" / "));
    LinkedList<String> input = new LinkedList<>();
    input.add("Επωνυμία Εργοδότη: " + seminarTrainee.getContractor().getName());
    input.add("Ημερομηνία: " + seminarTrainee.getSeminar().getDate().format(formatter));
    input.add("Για τις Εγκαταστάσεις των: " + subRefineriesNames);
    input.add("Ημερομηνία Σεμιναρίου Βασικού Επιπέδου: " + seminarTrainee.getSeminar().getDate().format(formatter));
    input.add("Βαθμολογία Βασικού Επιπέδου: ");  // I believe it has to be blank

    XWPFTable table = doc.createTable(5, 1);
    table.setWidth("70%");

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
    input.add("Όνομα Πατρός: " + seminarTrainee.getTrainee().getFathersName());
    input.add("ΑΜΑ: " + seminarTrainee.getTrainee().getAma());
    input.add("Αριθμός Εγγράφου Ταυτοπροσωπίας: " + seminarTrainee.getTrainee().getDocumentCode());  // I believe it has to be blank
    input.add("Τύπος Εγγράφου: " + docTypeMapping.get(seminarTrainee.getTrainee().getDocType()));

    XWPFTable table = doc.createTable(4, 2);
    table.setWidth("98.6%");

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
            CTHMerge hMerge = CTHMerge.Factory.newInstance();
            hMerge.setVal(STMerge.RESTART);
            cell.getCTTc().getTcPr().setHMerge(hMerge);
          } else {
            CTHMerge hMerge = CTHMerge.Factory.newInstance();
            hMerge.setVal(STMerge.CONTINUE);
            cell.getCTTc().getTcPr().setHMerge(hMerge);
          }
        } else {
          rh.setText(input.pop());
        }
        para.setAlignment(ParagraphAlignment.LEFT);

        // style cell as desired
      }
    }
  }

  private void specialtyDetailsTable(XWPFDocument doc, SeminarTrainee seminarTrainee, LinkedList<String> seminarSpecialties) {
    List<String> traineeSpecialties = seminarTraineeRepository
        .findByTraineeAndSeminar(seminarTrainee.getTrainee(), seminarTrainee.getSeminar())
        .stream()
        .map(seminarTraineeSp -> seminarTraineeSp.getSpecialty().getName())
        .collect(Collectors.toList());

    LinkedList<String> copySeminarSpecialties = new LinkedList<>();
    copySeminarSpecialties.addAll(seminarSpecialties);

    XWPFTable table = doc.createTable(seminarTrainee.getSeminar().getSeminarSpecialties().size() / 2 + 1 + seminarTrainee.getSeminar().getSeminarSpecialties().size() % 2, 4);
    table.setWidth("98.6%");


    // Set the table style. If the style is not defined, the table style
    // will become "Normal".
    CTTblPr tblPr = table.getCTTbl().getTblPr();
    CTString styleStr = tblPr.addNewTblStyle();
    styleStr.setVal("Table Grid 1");

    // Get a list of the rows in the table
    List<XWPFTableRow> rows = table.getRows();

    for (int rowCount = 0; rowCount < rows.size(); rowCount++) {
      XWPFTableRow row = rows.get(rowCount);
      row.setHeight(1);
      // get the cells in this row
      List<XWPFTableCell> cells = row.getTableCells();
      // add content to each cell
      for (int cellCount = 0; cellCount < cells.size(); cellCount++) {
        XWPFTableCell cell = cells.get(cellCount);
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
        if (rowCount == 0) {
          if (cellCount == 0) {
            // header row
            rh.setText("Ειδικότητες για τις οποίες θέλει να εξεταστεί");
            rh.setBold(true);
            rh.setColor("0070c0");
            CTHMerge hMerge = CTHMerge.Factory.newInstance();
            hMerge.setVal(STMerge.RESTART);
            cell.getCTTc().getTcPr().setHMerge(hMerge);
          } else {
            CTHMerge hMerge = CTHMerge.Factory.newInstance();
            hMerge.setVal(STMerge.CONTINUE);
            cell.getCTTc().getTcPr().setHMerge(hMerge);
          }
        } else if(!copySeminarSpecialties.isEmpty()) {
          String tempSpecialty = copySeminarSpecialties.getFirst();
          if (cellCount % 2 == 0) {
            rh.setText(tempSpecialty);
          } else {
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
  }

  private static CTAnchor getAnchorWithGraphic(CTDrawing drawing /*inline drawing*/,
                                               String drawingDescr, boolean behind) throws Exception {

    CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
    long width = drawing.getInlineArray(0).getExtent().getCx();
    long height = drawing.getInlineArray(0).getExtent().getCy();

    String anchorXML =
        "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + ((behind) ? 1 : 0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
            + "<wp:simplePos x=\"0\" y=\"0\"/>"
            + "<wp:positionH relativeFrom=\"column\"><wp:posOffset>4668520</wp:posOffset></wp:positionH>"
            + "<wp:positionV relativeFrom=\"paragraph\"><wp:posOffset>284480</wp:posOffset></wp:positionV>"
            + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
            + "<wp:effectExtent l=\"0\" t=\"0\" r=\"13335\" b=\"17145\"/><wp:wrapNone/>"
            + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + drawingDescr + "\"/><wp:cNvGraphicFramePr/>"
            + "</wp:anchor>";

    drawing = CTDrawing.Factory.parse(anchorXML);
    CTAnchor anchor = drawing.getAnchorArray(0);
    anchor.setGraphic(graphicalobject);
    return anchor;
  }
}
