package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Transactional
public class ExcelImporter {

  private TraineeRepository traineeRepository;
  private SeminarTraineeRepository seminarTraineeRepository;
  private ContractorRepository contractorRepository;
  private SpecialtyRepository specialtyRepository;
  private SeminarSpecialtyRepository seminarSpecialtyRepository;

  @Autowired
  public ExcelImporter(TraineeRepository traineeRepository, SeminarTraineeRepository seminarTraineeRepository, ContractorRepository contractorRepository, SpecialtyRepository specialtyRepository, SeminarSpecialtyRepository seminarSpecialtyRepository) {
    this.traineeRepository = traineeRepository;
    this.seminarTraineeRepository = seminarTraineeRepository;
    this.contractorRepository = contractorRepository;
    this.specialtyRepository = specialtyRepository;
    this.seminarSpecialtyRepository = seminarSpecialtyRepository;
  }

  public ApiResponse importExcel(Seminar seminar, byte[] uploadedExcelFile) throws IOException {

    Contractor contractor = null;
    Trainee trainee = null;
    InputStream excelInputStream = new ByteArrayInputStream(uploadedExcelFile);

    //Trainee data initialization
    String traineeDocumentCode = null;
    String traineeName = null;
    String traineeSurname = null;
    String traineeFathersName = null;
    String traineeNationality = null;
    String traineeAma = null;
    DocType docType = DocType.NONE;

    //Contractor data initialization
    String contractorName = null;
    String contractorActivity = null;
    String contractorAfm = "";
    String contractorAddress = null;
    String contractorDOY = null;
    String contractorEmail = null;
    String contractorPhoneNumber = "";
    String contractorRepresentativeName = null;

    Map<Integer, Specialty> specialtyList = new HashMap<>();

    try {
      Workbook workbook = new XSSFWorkbook(excelInputStream);
      Sheet datatypeSheet = workbook.getSheetAt(0);
      Iterator<Row> iterator = datatypeSheet.iterator();

      for (Row row : datatypeSheet) {
        traineeAma = null;
        List<SeminarTrainee> seminarTraineeList = new ArrayList<>();
        for (Cell cell : row) {
          switch (cell.getRowIndex()) {
            case 0:
              //do nothing, there lands a title which I don't think we need and we care about too.
              break;
            case 1:
              if (cell.getColumnIndex() == 2)     //Contractor's name
              {
                contractorName = cell.getStringCellValue().trim();
              }
              break;
            case 2:
              if (cell.getColumnIndex() == 0)          //Activity
              {
                contractorActivity = cell.getStringCellValue().trim();
                contractorActivity = contractorActivity.split(":")[1].trim();
              } else if (cell.getColumnIndex() == 6)     //ΑΦΜ
              {
                contractorAfm = cell.getStringCellValue().trim();
              }
              break;
            case 3:
              if (cell.getColumnIndex() == 2)           //Address
              {
                contractorAddress = cell.getStringCellValue().trim();

              } else if (cell.getColumnIndex() == 6)       //DOY
              {
                contractorDOY = cell.getStringCellValue().trim();
              }
              if (cell.getColumnIndex() >= 11) {
                if (cell.getStringCellValue().trim().length() > 0) {
                  Optional<Specialty> specialtyOptional = specialtyRepository.findByName(cell.getStringCellValue().trim());
                  if (!specialtyOptional.isPresent()) {
                    specialtyRepository.save(Specialty.builder().name(cell.getStringCellValue().trim()).build());
                  }
                  Specialty specialty = specialtyRepository.findByName(cell.getStringCellValue().trim()).get();
                  specialtyList.put(cell.getColumnIndex(), specialty);
                  if(!seminarSpecialtyRepository.existsSeminarSpecialtyBySeminarAndSpecialty(seminar, specialty)) {
                    SeminarSpecialty seminarSpecialty = SeminarSpecialty.builder().seminar(seminar).specialty(specialty).build();
                    seminarSpecialtyRepository.save(seminarSpecialty);
                  }
                }
              }
              break;
            case 4:
              if (cell.getColumnIndex() == 2)           //Email
              {
                contractorEmail = cell.getStringCellValue().trim();
              } else if (cell.getColumnIndex() == 6)       //Phone Number
              {
                contractorPhoneNumber = cell.getStringCellValue().trim();
              }
              break;
            case 5:
              if (cell.getColumnIndex() == 2)           //Representative
              {
                contractorRepresentativeName = cell.getStringCellValue().trim();
              }
              break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
              //do nothing for now
              break;
            default:                                //Trainee section
              switch (cell.getColumnIndex()) {
                case 0:
                  break;
                case 1:
                  traineeSurname = cell.getStringCellValue().trim();
                  break;
                case 2:
                  traineeName = cell.getStringCellValue().trim();
                  break;
                case 3:
                  traineeFathersName = cell.getStringCellValue().trim();
                  break;
                case 4:
                  traineeNationality = cell.getStringCellValue().trim();
                  break;
                case 5:
                  traineeDocumentCode = cell.getStringCellValue().trim();
                  break;
                case 6:
                case 7:
                case 8:
                  XSSFCellStyle cellStyle = (XSSFCellStyle) datatypeSheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex()).getCellStyle();
                  if (IndexedColors.AUTOMATIC.index != cellStyle.getFillForegroundColor()) {
                    switch (cell.getColumnIndex()) {
                      case 6:
                        docType = DocType.IDENTITY;
                        break;
                      case 7:
                        docType = DocType.PASSPORT;
                        break;
                      case 8:
                        docType = DocType.DRIVING_LICENSE;
                        break;
                      default:
                        break;
                    }
                  }
                  break;
                case 9:
                  try {
                    traineeAma = Double.toString(cell.getNumericCellValue()).split("\\.")[0];
                  } catch (IllegalStateException e) {
                    traineeAma = cell.getStringCellValue().trim();
                  }
                  break;
                case 10:
                  break;
                default:
                  XSSFCellStyle specialtyCellStyle = (XSSFCellStyle) datatypeSheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex()).getCellStyle();
                  if (IndexedColors.AUTOMATIC.getIndex() != specialtyCellStyle.getFillForegroundColor()) {
                    //TODO Import to seminarTraineeSpecialties
                    seminarTraineeList.add(SeminarTrainee.builder().specialty(specialtyList.get(cell.getColumnIndex())).build());
                  }

              }
              break;
          }
        }
        //Build Trainne from excel
        if (traineeAma != null) {
          trainee = Trainee.builder().docType(docType).ama(traineeAma).documentCode(traineeDocumentCode)
              .name(traineeName).surname(traineeSurname).fathersName(traineeFathersName).nationality(traineeNationality).build();
          //Build Contractor from excel
          contractor = Contractor.builder().activity(contractorActivity).address(contractorAddress).afm(contractorAfm).doy(contractorDOY).name(contractorName)
              .email(contractorEmail).representativeName(contractorRepresentativeName).phoneNumber(contractorPhoneNumber).build();
          //Save Contractor and Trainee to database (Update/override if already exists)
          saveContractorAndTrainee(contractor, trainee, seminarTraineeList, seminar);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ApiResponse(true, "File uploaded succesfully");
  }

  private void saveContractorAndTrainee(Contractor contractor, Trainee trainee, List<SeminarTrainee> seminarTraineeList, Seminar seminar) {
    // Save Contractor to repo
    Optional<Contractor> contractorOptional = contractorRepository.findByAfm(contractor.getAfm());
    if (!contractorOptional.isPresent()) {
      contractor = contractorRepository.save(contractor);
    } else {
      contractorOptional.get().setActivity(contractor.getActivity());
      contractorOptional.get().setAddress(contractor.getAddress());
      contractorOptional.get().setDoy(contractor.getDoy());
      contractorOptional.get().setEmail(contractor.getEmail());
      contractorOptional.get().setName(contractor.getName());
      contractorOptional.get().setPhoneNumber(contractor.getPhoneNumber());
      contractorOptional.get().setRepresentativeName(contractor.getRepresentativeName());
      contractor = contractorRepository.save(contractorOptional.get());
    }

    //Save Trainee to repo
    Optional<Trainee> traineeOptional = traineeRepository.findByAma(trainee.getAma());
    if (!traineeOptional.isPresent()) {
      trainee = traineeRepository.save(trainee);
    } else {
      traineeOptional.get().setDocType(trainee.getDocType());
      traineeOptional.get().setDocumentCode(trainee.getDocumentCode());
      traineeOptional.get().setFathersName(trainee.getFathersName());
      traineeOptional.get().setName(trainee.getName());
      traineeOptional.get().setNationality(trainee.getNationality());
      traineeOptional.get().setSurname(trainee.getSurname());
      trainee = traineeRepository.save(traineeOptional.get());
    }

    Contractor finalContractor = contractor;
    Trainee finalTrainee = trainee;

    seminarTraineeList.forEach(seminarTrainee -> {
      Optional<SeminarTrainee> seminarTraineeOptional = seminarTraineeRepository.findBySeminarAndContractorAndTraineeAndSpecialty(seminar, finalContractor, finalTrainee, seminarTrainee.getSpecialty());
      if (!seminarTraineeOptional.isPresent()) {
        seminarTrainee.setContractor(finalContractor);
        seminarTrainee.setTrainee(finalTrainee);
        seminarTrainee.setSeminar(seminar);
        seminarTraineeRepository.save(seminarTrainee);
      }
    });
  }
}
