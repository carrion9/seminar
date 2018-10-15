package com.vetx.starter.controller;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarSpecialty;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.service.AttendanceDocService;
import com.vetx.starter.service.ExcelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class FileController {

  private ExcelImporter excelImporter;
  private SeminarRepository seminarRepository;
  private AttendanceDocService attendanceDocService;

  public FileController(ExcelImporter excelImporter, SeminarRepository seminarRepository, AttendanceDocService attendanceDocService) {
    this.excelImporter = excelImporter;
    this.seminarRepository = seminarRepository;
    this.attendanceDocService = attendanceDocService;
  }

  @Autowired


  @RequestMapping(value="/upload", method=RequestMethod.GET)
  public @ResponseBody  String provideUploadInfo() {
    return "You can upload a file by posting to this same URL.";
  }

  @RequestMapping(value="/upload", method=RequestMethod.POST)
  public ResponseEntity  handleFileUpload(@RequestParam("seminarId") Long seminarId,  @RequestParam("file") MultipartFile file) throws IOException {
    Optional<Seminar> seminar = seminarRepository.findById(seminarId);
    if (!seminar.isPresent()) {
      return new ResponseEntity(new ApiResponse(false, "Create the Seminar first"),
          HttpStatus.BAD_REQUEST);
    }

    byte[] bytes = file.getBytes();
    ApiResponse apiResponse = excelImporter.importExcel(seminar.get(), bytes);
    return new ResponseEntity(apiResponse,HttpStatus.OK);
  }

  @GetMapping(value = "/seminars/{seminarId}/attendance-document/{seminarSpecialtyId}")
  public ResponseEntity<Resource> getAttendanceDocument(@PathVariable("seminarId") Long seminarId, @PathVariable("seminarSpecialtyId") Long seminarSpecialtyId) throws Exception {
    Optional<Seminar> seminar = seminarRepository.findById(seminarId);
    if (!seminar.isPresent()) {
      return new ResponseEntity(new ApiResponse(false, "Create the Seminar first"),
          HttpStatus.BAD_REQUEST);
    }

    Optional<SeminarSpecialty> seminarSpecialtyOptional = seminar
        .get()
        .getSeminarSpecialties()
        .stream()
        .filter(seminarSpecialty -> seminarSpecialty.getKey().equals(seminarSpecialtyId))
        .findAny();
    if (!seminarSpecialtyOptional.isPresent()) {
      return new ResponseEntity(new ApiResponse(false, "Add specialty to Seminar first"),
          HttpStatus.BAD_REQUEST);
    }

    ByteArrayResource resource = new ByteArrayResource(attendanceDocService.createDocument(seminar.get(), seminarSpecialtyOptional.get().getSpecialty()));

    return ResponseEntity
        .ok()
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);

  }

}