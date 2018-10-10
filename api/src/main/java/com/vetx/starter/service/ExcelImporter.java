package com.vetx.starter.service;

import com.vetx.starter.model.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.ContractorRepository;
import com.vetx.starter.repository.SeminarTraineeRepository;
import com.vetx.starter.repository.TraineeRepository;
import org.apache.poi.ss.usermodel.*;
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
                        case 2:
                            if(cell.getColumnIndex() == 3)     //Contractor's name
                            {
                                String contractorName = cell.getStringCellValue();
                                if (contractorRepository.existsByName(contractorName)) {
                                    contractor = contractorRepository.findByName(contractorName).get();
                                } else {
                                    contractor = contractorRepository.save(Contractor.builder().name(contractorName).build());
                                }
                            }
                            break;
                        case 3:
                            if(cell.getColumnIndex() == 1)          //Activity
                            {
                                String contractorActivity = cell.getStringCellValue();
                                contractorActivity.replaceAll("ΑΝΤΙΚ. ΔΡΑΣΤΗΡΙΟΤΗΤΑΣ:   ","");
                                if(contractorRepository.existsByActivity(contractorActivity)) {
                                    contractor = contractorRepository.findByActivity(contractorActivity).get();
                                }else {
                                    contractor = contractorRepository.save(Contractor.builder().activity(contractorActivity).build());
                                }
                            }
                            else if(cell.getColumnIndex() == 7)     //ΑΦΜ
                            {
                                long contractorAfm = (long)cell.getNumericCellValue();
                                if(contractorRepository.existsByAfm(contractorAfm)) {
                                    contractor = contractorRepository.findByAfm(contractorAfm).get();
                                } else {
                                    contractor = contractorRepository.save(Contractor.builder().afm(contractorAfm).build());
                                }
                            }
                            break;
                        case 4:
                            if(cell.getColumnIndex() == 3)           //Address
                            {
                                String contractorAddress = cell.getStringCellValue();
                                if(contractorRepository.existsByAddress(contractorAddress)) {
                                    contractor = contractorRepository.findByAddress(contractorAddress).get();
                                } else {
                                    contractor = contractorRepository.save(Contractor.builder().address(contractorAddress).build());
                                }
                            }
                            else if(cell.getColumnIndex() == 7)       //DOY
                            {
                                String contractorDoy = cell.getStringCellValue();
                                if(contractorRepository.existsByDoy(contractorDoy)) {
                                    contractor = contractorRepository.findByDoy(contractorDoy).get();
                                } else {
                                    contractor = contractorRepository.save(Contractor.builder().address(contractorDoy).build());
                                }
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
