package com.vetx.starter.service;

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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

@Service
public class ImageUploader {
    
    private TraineeRepository traineeRepository;

    public ImageUploader(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public ApiResponse uploadImage(Trainee trainee, byte[] uploadedImageFile) throws IOException {

        // InputStream imageInputStream = new ByteArrayInputStream(uploadedImageFile);

        // BufferedImage bufferImage = ImageIO.read(imageInputStream);

        trainee.setImage(uploadedImageFile);
        traineeRepository.save(trainee);

        return new ApiResponse(true, "File uploaded succesfully");
    }


}