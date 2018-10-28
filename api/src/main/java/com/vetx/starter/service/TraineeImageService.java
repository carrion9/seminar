package com.vetx.starter.service;

import com.vetx.starter.exception.StorageFileNotFoundException;
import com.vetx.starter.model.*;
import com.vetx.starter.payload.ApiResponse;
import com.vetx.starter.repository.ContractorRepository;
import com.vetx.starter.repository.SeminarTraineeRepository;
import com.vetx.starter.repository.SpecialtyRepository;
import com.vetx.starter.repository.TraineeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

@Service
public class TraineeImageService {

  private TraineeRepository traineeRepository;

  @Value("${trainee.image.root.location}")
  private String rootLocationString;

  private Path rootLocation;

  public TraineeImageService(TraineeRepository traineeRepository) {
    this.traineeRepository = traineeRepository;
  }

  public ApiResponse uploadImage(Trainee trainee, MultipartFile file) throws IOException {
    String filename = trainee.getAma() + StringUtils.cleanPath(file.getOriginalFilename());
    try {
      init();
      if (file.isEmpty()) {
        throw new IOException("Failed to store empty file " + filename);
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, this.rootLocation.resolve(filename),
            StandardCopyOption.REPLACE_EXISTING);
        trainee.setImageLocation("traineeImageUpload/" + filename);
        traineeRepository.save(trainee);
      }
    } catch (IOException e) {
      throw new IOException("Failed to store file " + filename, e);
    }
    return new ApiResponse(true, "File uploaded succesfully");
  }

  public Resource loadAsResource(String filename) throws IOException {
    try {
      init();
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  private void init() throws IOException{

    this.rootLocation = Paths.get(this.rootLocationString);
    try {
      Files.createDirectories(rootLocation);
    }
    catch (IOException e) {
      throw new IOException("Could not initialize storage", e);
    }
  }

  private Path load(String filename) {
    return rootLocation.resolve(filename);
  }
}