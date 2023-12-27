package com.ouharri.fileuploader.service;

import com.ouharri.fileuploader.entity.FileDB;
import com.ouharri.fileuploader.exception.ResourceNotCreatedException;
import com.ouharri.fileuploader.exception.ResourceNotFoundException;
import com.ouharri.fileuploader.repository.FileDBRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Service class for handling file-related operations.
 */
@Service
@EnableCaching
@AllArgsConstructor
public class FileStorageService {

    private final FileDBRepository fileDBRepository;

    /**
     * Store a file in the database.
     *
     * @param file The file to be stored.
     * @return The stored FileDB entity.
     * @throws IOException If an I/O exception occurs while reading the file data.
     */
    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDB fileDB = FileDB.builder()
                .name(fileName)
                .type(file.getContentType())
                .data(file.getBytes())
                .build();
        try {
            return fileDBRepository.save(fileDB);
        } catch (ResourceNotCreatedException e) {
            throw new ResourceNotCreatedException("Could not store file " + fileName + ". Please try again!");
        }
    }

    /**
     * Get a file from the database by its ID.
     *
     * @param id The unique identifier of the file.
     * @return The FileDB entity.
     * @throws ResourceNotFoundException If the file with the specified ID is not found.
     */
    @Cacheable("Files")
    public FileDB getFile(UUID id) {
        return fileDBRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("File not found with id " + id)
                );
    }

    /**
     * Get a stream of all files in the database.
     *
     * @return A stream of FileDB entities.
     */
    @Cacheable("Files")
    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }
}