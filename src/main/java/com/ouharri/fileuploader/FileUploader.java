package com.ouharri.fileuploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the File Uploader application.
 * This class contains the main method to start the Spring Boot application.
 */
@SpringBootApplication
public class FileUploader {

    /**
     * The main method to start the File Uploader application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(FileUploader.class, args);
    }
}