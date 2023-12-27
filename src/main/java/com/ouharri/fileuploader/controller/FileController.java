package com.ouharri.fileuploader.controller;

import com.ouharri.fileuploader.entity.FileDB;
import com.ouharri.fileuploader.message.ResponseFile;
import com.ouharri.fileuploader.service.spec.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controller class for handling file-related operations.
 *
 * @author <a href="mailto:ouharrioutman@gmail.com">Ouharri Outman</a>
 */
@Controller
@AllArgsConstructor
@CrossOrigin("**")
public class FileController {

    private final FileStorageService storageService;

    /**
     * Upload a file to the server.
     *
     * @param file The file to be uploaded.
     * @return ResponseEntity containing a success message and the file metadata.
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseFile> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            FileDB fileDB = storageService.store(file);
            ResponseFile responseFile = ResponseFile.builder()
                    .name(fileDB.getName())
                    .type(fileDB.getType())
                    .size(file.getSize())
                    .url(
                            ServletUriComponentsBuilder
                                    .fromCurrentContextPath()
                                    .path("/files/")
                                    .path(fileDB.getId().toString())
                                    .toUriString()
                    )
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    /**
     * Get a list of all files available on the server.
     *
     * @return ResponseEntity containing a list of ResponseFile objects.
     */
    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles()
                .stream()
                .map(dbFile -> ResponseFile.builder()
                        .name(dbFile.getName())
                        .type(dbFile.getType())
                        .size(dbFile.getData().length)
                        .url(
                                ServletUriComponentsBuilder
                                        .fromCurrentContextPath()
                                        .path("/files/")
                                        .path(dbFile.getId().toString())
                                        .toUriString()
                        )
                        .build())
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    /**
     * Get a specific file by its ID.
     *
     * @param id The unique identifier of the file.
     * @return ResponseEntity containing the file data and metadata.
     */
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) {
        FileDB fileDB = storageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "Inline; filename=\"" + fileDB.getName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, fileDB.getType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileDB.getData().length))
                .header("Content-Description", "File Transfer")
                .header("Content-Transfer-Encoding", "binary")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                .header(HttpHeaders.ETAG, fileDB.getId().toString())
                .header(HttpHeaders.LAST_MODIFIED, fileDB.getUpdatedAt().toString())
                .header(HttpHeaders.DATE, fileDB.getCreatedAt().toString())
                .header(HttpHeaders.PRAGMA, "public")
                .header(HttpHeaders.EXPIRES, "31536000")
                .body(fileDB.getData());
    }

    /**
     * Update the content of a specific file by its ID.
     *
     * @param id   The unique identifier of the file.
     * @param file The updated file data.
     * @return ResponseEntity containing the updated file data and metadata.
     */
    @PutMapping("/files/{id}")
    public ResponseEntity<ResponseFile> updateFile(@PathVariable UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        FileDB updatedFile = storageService.updateFile(id, file);
        ResponseFile responseFile = ResponseFile.builder()
                .name(updatedFile.getName())
                .type(updatedFile.getType())
                .size(file.getSize())
                .url(
                        ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/files/")
                                .path(updatedFile.getId().toString())
                                .toUriString()
                )
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseFile);
    }

    /**
     * Delete a specific file by its ID.
     *
     * @param id The unique identifier of the file.
     * @return ResponseEntity indicating the success of the operation.
     */
    @DeleteMapping("/files/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable UUID id) {
        storageService.deleteFile(id);
        return ResponseEntity.status(HttpStatus.OK).body("File deleted successfully");
    }
}