package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.ContractorRepository;
import com.vetx.starter.repository.SeminarTraineeRepository;
import com.vetx.starter.repository.TraineeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ooxml.*;
import org.apache.poi.xssf.*;

@Service
@Transactional
public class ExcelImporter {

  private TraineeRepository traineeRepository;
  private SeminarTraineeRepository seminarTraineeRepository;
  private ContractorRepository contractorRepository;

  @Autowired
  public ExcelImporter(TraineeRepository traineeRepository, SeminarTraineeRepository seminarTraineeRepository, ContractorRepository contractorRepository) {
    this.traineeRepository = traineeRepository;
    this.seminarTraineeRepository = seminarTraineeRepository;
    this.contractorRepository = contractorRepository;
  }


    public ApiResponse importExcel(Seminar seminar, byte[] uploadedExcelFile) throws IOException {

        Contractor contractor = null;
        Trainee trainee = null;
        InputStream excelInputStream = new ByteArrayInputStream(uploadedExcelFile);

        // - - - - Import From Excel - - - - - //
        try {
            Workbook workbook = new XSSFWorkbook(excelInputStream);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            //Row currentRow = iterator.next();
            //Iterator<Cell> cellIterator = currentRow.iterator();

            //Cell currentCell = (Cell) cellIterator.next();
            for (Row row: datatypeSheet) {
                for(Cell cell: row) {
                    switch(cell.getRowIndex())
                    {
                        case 1:
                            //do nothing, there lands a title which I don't think we need and we care about too.
                            break;
                        case 2:
                            if(cell.getColumnIndex() == 3)     //Contractor's name
                            {
                                String contractorName = cell.getStringCellValue();
                                contractor = contractorRepository.save(Contractor.builder().name(contractorName).build());
                            }
                            break;
                        case 3:
                            if(cell.getColumnIndex() == 1)          //Activity
                            {
                                String contractorActivity = cell.getStringCellValue();
                                contractorActivity.replaceAll("ΑΝΤΙΚ. ΔΡΑΣΤΗΡΙΟΤΗΤΑΣ:   ","");
                                contractor = contractorRepository.save(Contractor.builder().activity(contractorActivity).build());
                            }
                            else if(cell.getColumnIndex() == 7)     //ΑΦΜ
                            {
                                long contractorAfm = (long)cell.getNumericCellValue();
                                contractor = contractorRepository.save(Contractor.builder().afm(contractorAfm).build());
                            }
                            break;
                        case 4:
                            if(cell.getColumnIndex() == 3)           //Address
                            {
                                String contractorAddress = cell.getStringCellValue();
                                contractor = contractorRepository.save(Contractor.builder().address(contractorAddress).build());

                            }
                            else if(cell.getColumnIndex() == 7)       //DOY
                            {
                                String contractorDoy = cell.getStringCellValue();
                                contractor = contractorRepository.save(Contractor.builder().address(contractorDoy).build());
                            }
                            break;
                        case 5:
                            if(cell.getColumnIndex() == 3)           //Address
                            {
                                String contractorEmail = cell.getStringCellValue();
                                contractor = contractorRepository.save(Contractor.builder().email(contractorEmail).build());
                            }
                            else if(cell.getColumnIndex() == 7)       //DOY
                            {
                                long contractorPhoneNumber = (long)cell.getNumericCellValue();
                                contractor = contractorRepository.save(Contractor.builder().phoneNumber(contractorPhoneNumber).build());
                            }
                            break;
                        case 6:
                            if(cell.getColumnIndex() == 3)           //Address
                            {
                                String contractorRepresentativeName = cell.getStringCellValue();
                                contractor = contractorRepository.save(Contractor.builder().representativeName(contractorRepresentativeName).build());
                            }
                            else if(cell.getColumnIndex() == 7)       //DOY
                            {
                                long contractorPhoneNumber = (long)cell.getNumericCellValue();
                                contractor = contractorRepository.save(Contractor.builder().phoneNumber(contractorPhoneNumber).build());
                            }
                            break;
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            //do nothing for now
                            break;
                        default:
                            if(cell.getColumnIndex() == 1)
                            {
                                long traineeId = (long)cell.getNumericCellValue();
                                trainee = traineeRepository.save(Trainee.builder().id(traineeId).build());
                            }
                            if(cell.getColumnIndex() == 2)
                            {
                                String traineeSurname = cell.getStringCellValue();
                                trainee = traineeRepository.save(Trainee.builder().surname(traineeSurname).build());
                            }
                            if(cell.getColumnIndex() == 3)
                            {
                                String traineeName = cell.getStringCellValue();
                                trainee = traineeRepository.save(Trainee.builder().name(traineeName).build());
                            }
                            if(cell.getColumnIndex() == 4)
                            {
                                String traineeFathersName = cell.getStringCellValue();
                                trainee = traineeRepository.save(Trainee.builder().fathersName(traineeFathersName).build());
                            }
                            if(cell.getColumnIndex() == 5)
                            {
                                String traineeNationality = cell.getStringCellValue();
                                trainee = traineeRepository.save(Trainee.builder().nationality(traineeNationality).build());
                            }
                            if(cell.getColumnIndex() == 6)
                            {
                                String traineeDocumentCode = cell.getStringCellValue();
                                trainee = traineeRepository.save(Trainee.builder().documentCode(traineeDocumentCode).build());
                            }
                            if((cell.getColumnIndex() == 7) || (cell.getColumnIndex() == 8) || (cell.getColumnIndex() == 9))
                            {
                                XSSFCellStyle cellStyle = (XSSFCellStyle) datatypeSheet.getRow(cell.getRowIndex()).getCell(cell.getColumnIndex()).getCellStyle();
                                Color color = cellStyle.getFillBackgroundColorColor();
                                if(((XSSFColor) color).getARGBHex() != "#00FFFFFF")
                                {
                                    switch(cell.getColumnIndex()) {
                                        case 7:
                                            trainee = traineeRepository.save(Trainee.builder().docType(DocType.IDENTITY).build());
                                            break;
                                        case 8:
                                            trainee = traineeRepository.save(Trainee.builder().docType(DocType.PASSPORT).build());
                                            break;
                                        case 9:
                                            trainee = traineeRepository.save(Trainee.builder().docType(DocType.DRIVING_LICENSE).build());
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                long traineeId = (long)cell.getNumericCellValue();
                                trainee = traineeRepository.save(Trainee.builder().id(traineeId).build());
                            }
                            if(cell.getColumnIndex() == 10)
                            {
                                String traineeAma = cell.getStringCellValue();
                                trainee = traineeRepository.save(Trainee.builder().ama(traineeAma).build());
                            }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read from excel contractor afm. He may already exist in db.

        Long contractorAfm = 1234567489L;
        if (contractorRepository.existsByAfm(contractorAfm)) {
            contractor = contractorRepository.findByAfm(contractorAfm).get();
        } else {
            contractor = contractorRepository.save(Contractor.builder().afm(contractorAfm).build());
        }

        //Και κάπως έτσι γεννιέται ένας εκπαιδευόμενος με ΝΤΑΤΑ.
        Trainee newTrainee = Trainee.builder().ama("123").cardStatus(CardStatus.DELIVERED).fathersName("mpampas").name("foo").nationality("bar").surname("pipis").documentCode("asd").build();
        traineeRepository.save(newTrainee);

        SeminarTrainee seminarTrainee = SeminarTrainee.builder().trainee(newTrainee).seminar(seminar).cost(60L).actualCost(0L).contractor(contractor).build();
        seminarTraineeRepository.save(seminarTrainee);

        return new ApiResponse(true, "File uploaded succesfully");
    }

    public void findOrSaveContractor(ContractorRepository contractorRepository)
    {

    }
}
