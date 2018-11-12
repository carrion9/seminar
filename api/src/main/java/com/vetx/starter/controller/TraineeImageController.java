package com.vetx.starter.controller;

import com.vetx.starter.model.Trainee;
import com.vetx.starter.repository.TraineeRepository;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.service.TraineeImageService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class TraineeImageController {

    private TraineeRepository traineeRepository;
    private TraineeImageService traineeImageService;

    public TraineeImageController(TraineeRepository traineeRepository, TraineeImageService traineeImageService) {
        this.traineeRepository = traineeRepository;
        this.traineeImageService = traineeImageService;
    }

    @GetMapping(value = "/traineeImageUpload/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Resource file = traineeImageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    @PostMapping(value="/traineeImageUpload")
    //@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> handleImageUpload(@RequestParam("traineeId") Long traineeId, @RequestParam("image") MultipartFile file) throws IOException {
        Optional<Trainee> trainee = traineeRepository.findById((traineeId));
        if(!trainee.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "Create Trainee first"),
                HttpStatus.BAD_REQUEST);
        }
      if (!file.getOriginalFilename().endsWith("jpg") && !file.getOriginalFilename().endsWith("png") && !file.getOriginalFilename().endsWith("jpeg") && !file.getOriginalFilename().endsWith("gif")) {
        return new ResponseEntity<>(new ApiResponse(false, "Formats supported: png, jpg and gif"),
            HttpStatus.BAD_REQUEST);
      }

      ApiResponse apiResponse = traineeImageService.uploadImage(trainee.get(), file);
      return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}

