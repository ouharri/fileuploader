package com.ouharri.fileuploader.service.impl;

import com.ouharri.fileuploader.entity.FileDB;
import com.ouharri.fileuploader.exception.ResourceNotCreatedException;
import com.ouharri.fileuploader.exception.ResourceNotFoundException;
import com.ouharri.fileuploader.repository.FileDBRepository;
import com.ouharri.fileuploader.service.spec.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for handling file-related operations.
 */
@Service
@AllArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileDBRepository fileDBRepository;

    /**
     * Store a file in the database.
     *
     * @param file The file to be stored.
     * @return The stored FileDB entity.
     * @throws IOException If an I/O exception occurs while reading the file data.
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "file", key = "#result.id", allEntries = true, condition = "#result.id != null")
            }
    )
    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            FileDB fileDB = FileDB.builder()
                    .name(fileName)
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .build();
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
    @Cacheable(value = "file", key = "#id")
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
    @Cacheable("file")
    public List<FileDB> getAllFiles() {
        return fileDBRepository.findAll();
    }

    /**
     * Update the content of a file in the database.
     *
     * @param id   The unique identifier of the file.
     * @param file The updated file data.
     * @return The updated FileDB entity.
     * @throws IOException               If an I/O exception occurs while reading the file data.
     * @throws ResourceNotFoundException If the file with the specified ID is not found.
     */
    @CachePut(value = "file", key = "#id")
    public FileDB updateFile(UUID id, MultipartFile file) throws IOException {
        FileDB existingFile = fileDBRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("File not found with id " + id)
                );
        if (existingFile == null)
            throw new ResourceNotFoundException("Could not find file with id " + id);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        existingFile.setName(fileName);
        existingFile.setType(file.getContentType());
        existingFile.setData(file.getBytes());
        try {
            return fileDBRepository.save(existingFile);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not update file with id " + id);
        }
    }

    /**
     * Delete a file from the database by its ID.
     *
     * @param id The unique identifier of the file.
     * @throws ResourceNotFoundException If the file with the specified ID is not found.
     */
    @CacheEvict(value = "file", key = "#id", allEntries = true)
    public void deleteFile(UUID id) {
        FileDB fileDB = fileDBRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("File not found with id " + id)
                );
        if (fileDB == null)
            throw new ResourceNotFoundException("Could not find file with id " + id);
        try {
            fileDBRepository.delete(fileDB);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Could not delete file with id " + id);
        }
    }
}