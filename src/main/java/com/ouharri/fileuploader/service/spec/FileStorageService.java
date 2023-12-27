package com.ouharri.fileuploader.service.spec;

import com.ouharri.fileuploader.entity.FileDB;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Service interface for handling file-related operations.
 */
public interface FileStorageService {

    /**
     * Store a file in the database.
     *
     * @param file The file to be stored.
     * @return The stored FileDB entity.
     * @throws IOException If an I/O exception occurs while reading the file data.
     */
    FileDB store(MultipartFile file) throws IOException;

    /**
     * Get a file from the database by its ID.
     *
     * @param id The unique identifier of the file.
     * @return The FileDB entity.
     */
    FileDB getFile(UUID id);

    /**
     * Get a stream of all files in the database.
     *
     * @return A stream of FileDB entities.
     */
    Stream<FileDB> getAllFiles();

    /**
     * Update the content of a file in the database.
     *
     * @param id   The unique identifier of the file.
     * @param file The updated file data.
     * @return The updated FileDB entity.
     * @throws IOException If an I/O exception occurs while reading the file data.
     */
    FileDB updateFile(UUID id, MultipartFile file) throws IOException;

    /**
     * Delete a file from the database by its ID.
     *
     * @param id The unique identifier of the file.
     */
    void deleteFile(UUID id);
}