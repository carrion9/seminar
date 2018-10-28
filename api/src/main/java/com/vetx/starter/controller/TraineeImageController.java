package com.vetx.starter.controller;

import com.vetx.starter.model.Trainee;
import com.vetx.starter.repository.TraineeRepository;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.security.CurrentUser;
import com.vetx.starter.security.UserPrincipal;
import com.vetx.starter.service.ImageUploader;

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
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class TraineeImageController {

    private TraineeRepository traineeRepository;
    private ImageUploader imageUploader;

    public TraineeImageController(TraineeRepository traineeRepository, ImageUploader imageUploader) {
        this.traineeRepository = traineeRepository;
        this.imageUploader = imageUploader;
    }

    @Autowired
    @GetMapping("/traineeImageUpload")
    public @ResponseBody  String provideUploadInfo() {
        return "You can upload an image by posting to this same URL.";
    }
    
    @PostMapping(value="/traineeImageUpload")
    //@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity handleImageUpload(@RequestParam("traineeId") Long traineeId, @RequestParam("image") MultipartFile file) throws IOException {
        Optional<Trainee> trainee = traineeRepository.findById((traineeId));
        if(!trainee.isPresent()) {
            return new ResponseEntity(new ApiResponse(false, "Create Trainee first"),
                HttpStatus.BAD_REQUEST);
        }

        byte[] bytes = file.getBytes();
        ApiResponse apiResponse = imageUploader.uploadImage(trainee.get(), bytes);
        return new ResponseEntity(apiResponse,HttpStatus.OK);      
    }

    // @PostMapping(value="/upload")
    // //  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    //   public ResponseEntity  handleFileUpload(@RequestParam("seminarId") Long seminarId, @RequestParam("file") MultipartFile file) throws IOException {
    //     Optional<Seminar> seminar = seminarRepository.findById(seminarId);
    //     if (!seminar.isPresent()) {
    //       return new ResponseEntity(new ApiResponse(false, "Create the Seminar first"),
    //           HttpStatus.BAD_REQUEST);
    //     }
    
    //     byte[] bytes = file.getBytes();
    //     ApiResponse apiResponse = excelImporter.importExcel(seminar.get(), bytes);
    //     return new ResponseEntity(apiResponse,HttpStatus.OK);
    //   }


}

