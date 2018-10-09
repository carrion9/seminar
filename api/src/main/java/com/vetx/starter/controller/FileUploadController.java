package com.vetx.starter.controller;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.service.ExcelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api")
public class FileUploadController {

  private ExcelImporter excelImporter;
  private SeminarRepository seminarRepository;

  @Autowired
  public FileUploadController(ExcelImporter excelImporter, SeminarRepository seminarRepository) {
    this.excelImporter = excelImporter;
    this.seminarRepository = seminarRepository;
  }

  @RequestMapping(value="/upload", method=RequestMethod.GET)
  public @ResponseBody  String provideUploadInfo() {
    return "You can upload a file by posting to this same URL.";
  }

  @RequestMapping(value="/upload", method=RequestMethod.POST)
  public ResponseEntity  handleFileUpload(@RequestParam("seminarId") Long seminarId,  @RequestParam("file") MultipartFile file) throws IOException {
    if (!seminarRepository.existsById(seminarId)) {
      return new ResponseEntity(new ApiResponse(false, "Create the Seminar first"),
          HttpStatus.BAD_REQUEST);
    }
    Seminar seminar = seminarRepository.getOne(seminarId);

    byte[] bytes = file.getBytes();
    ApiResponse apiResponse = excelImporter.importExcel(seminar, bytes);
    return new ResponseEntity(apiResponse,HttpStatus.OK);
  }

}