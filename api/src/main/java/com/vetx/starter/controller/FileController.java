package com.vetx.starter.controller;

import com.vetx.starter.model.Seminar;
import com.vetx.starter.model.SeminarSpecialty;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.SeminarRepository;
import com.vetx.starter.security.CurrentUser;
import com.vetx.starter.security.UserPrincipal;
import com.vetx.starter.service.AttendanceDocService;
import com.vetx.starter.service.ExcelImporter;
import com.vetx.starter.service.WelcomeDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class FileController {

  private ExcelImporter excelImporter;
  private SeminarRepository seminarRepository;
  private AttendanceDocService attendanceDocService;
  private WelcomeDocService welcomeDocService;

  @Autowired
  public FileController(ExcelImporter excelImporter, SeminarRepository seminarRepository, AttendanceDocService attendanceDocService, WelcomeDocService welcomeDocService) {
    this.excelImporter = excelImporter;
    this.seminarRepository = seminarRepository;
    this.attendanceDocService = attendanceDocService;
    this.welcomeDocService = welcomeDocService;
  }



  @GetMapping(value="/upload")
  public @ResponseBody  String provideUploadInfo() {
    return "You can upload a file by posting to this same URL.";
  }

  @PostMapping(value="/upload")
//  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity  handleFileUpload(@RequestParam("seminarId") Long seminarId, @RequestParam("file") MultipartFile file) throws IOException {
    Optional<Seminar> seminar = seminarRepository.findById(seminarId);
    if (!seminar.isPresent()) {
      return new ResponseEntity(new ApiResponse(false, "Create the Seminar first"),
          HttpStatus.BAD_REQUEST);
    }

    byte[] bytes = file.getBytes();
    ApiResponse apiResponse = excelImporter.importExcel(seminar.get(), bytes, file.getOriginalFilename());
    return new ResponseEntity(apiResponse,HttpStatus.OK);
  }

  @GetMapping(value = "/seminars/{seminarId}/attendance-document/{seminarSpecialtyId}", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
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

    Resource resource = new ByteArrayResource(attendanceDocService.createDocument(seminar.get(), seminarSpecialtyOptional.get().getSpecialty()).toByteArray());

    return ResponseEntity
        .ok()
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ "attendance-document-" + seminar.get().getDate().toString() +".docx")
        .body(resource);
  }

  @GetMapping(value = "/seminars/{seminarId}/welcome-document", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<Resource> getWelcomeDocument(@PathVariable("seminarId") Long seminarId) throws Exception {
    Optional<Seminar> seminar = seminarRepository.findById(seminarId);
    if (!seminar.isPresent()) {
      return new ResponseEntity(new ApiResponse(false, "Create the Seminar first"),
          HttpStatus.BAD_REQUEST);
    }

    Resource resource = new ByteArrayResource(welcomeDocService.createDocument(seminar.get()).toByteArray());

    return ResponseEntity
        .ok()
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ "welcome-document-" + seminar.get().getDate().toString() +".docx")
        .body(resource);
  }

}