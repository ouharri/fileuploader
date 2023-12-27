package com.ouharri.fileuploader.service;

import com.ouharri.fileuploader.entity.FileDB;
import com.ouharri.fileuploader.exception.ResourceNotCreatedException;
import com.ouharri.fileuploader.exception.ResourceNotFoundException;
import com.ouharri.fileuploader.repository.FileDBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link FileStorageServiceImpl} class.
 * These tests cover various scenarios related to file storage and retrieval.
 * Uses Mockito for mocking dependencies and simulating behavior.
 *
 * @author Your Name
 * @see FileStorageServiceImpl
 */
class FileStorageServiceTest {

    @Mock
    private FileDBRepository fileDBRepository;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void storeFile_Success() throws IOException {
        // Arrange
        MultipartFile mockFile = new MockMultipartFile("test.txt", "Test file content".getBytes());

        // Mock behavior
        when(fileDBRepository.save(any(FileDB.class))).thenReturn(new FileDB());

        // Act
        FileDB storedFile = fileStorageService.store(mockFile);

        // Assert
        assertNotNull(storedFile);
        verify(fileDBRepository, times(1)).save(any(FileDB.class));
    }

    @Test
    void storeFile_Failure() {
        // Arrange
        MultipartFile mockFile = new MockMultipartFile("test.txt", "Test file content".getBytes());

        // Mock behavior to throw an exception
        when(fileDBRepository.save(any(FileDB.class))).thenThrow(new ResourceNotCreatedException(""));

        // Act and Assert
        assertThrows(ResourceNotCreatedException.class, () -> fileStorageService.store(mockFile));
        verify(fileDBRepository, times(1)).save(any(FileDB.class));
    }

    @Test
    void getFile_Success() {
        // Arrange
        UUID fileId = UUID.randomUUID();
        FileDB mockFile = new FileDB();
        mockFile.setId(fileId);

        // Mock behavior
        when(fileDBRepository.findById(fileId)).thenReturn(Optional.of(mockFile));

        // Act
        FileDB retrievedFile = fileStorageService.getFile(fileId);

        // Assert
        assertNotNull(retrievedFile);
        assertEquals(fileId, retrievedFile.getId());
        verify(fileDBRepository, times(1)).findById(fileId);
    }

    @Test
    void getFile_NotFound() {
        // Arrange
        UUID fileId = UUID.randomUUID();

        // Mock behavior to return empty optional
        when(fileDBRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> fileStorageService.getFile(fileId));
        verify(fileDBRepository, times(1)).findById(fileId);
    }

    @Test
    void getAllFiles() {
        // Arrange
        List<FileDB> mockFiles = Stream.of(new FileDB(), new FileDB()).collect(Collectors.toList());
        when(fileDBRepository.findAll()).thenReturn(mockFiles);

        // Act
        List<FileDB> allFiles = fileStorageService.getAllFiles().collect(Collectors.toList());

        // Assert
        assertNotNull(allFiles);
        assertEquals(mockFiles.size(), allFiles.size());
        verify(fileDBRepository, times(1)).findAll();
    }

    @Test
    void updateFile_Success() throws IOException {
        // Arrange
        UUID fileId = UUID.randomUUID();
        MultipartFile mockFile = new MockMultipartFile("test.txt", "Updated file content".getBytes());
        FileDB existingFile = new FileDB();
        existingFile.setId(fileId);

        // Mock behavior
        when(fileDBRepository.findById(fileId)).thenReturn(Optional.of(existingFile));
        when(fileDBRepository.save(any(FileDB.class))).thenReturn(existingFile);

        // Act
        FileDB updatedFile = fileStorageService.updateFile(fileId, mockFile);

        // Assert
        assertNotNull(updatedFile);
        assertEquals(fileId, updatedFile.getId());
        verify(fileDBRepository, times(1)).findById(fileId);
        verify(fileDBRepository, times(1)).save(any(FileDB.class));
    }

    @Test
    void updateFile_FileNotFound() {
        // Arrange
        UUID fileId = UUID.randomUUID();
        MultipartFile mockFile = new MockMultipartFile("test.txt", "Updated file content".getBytes());

        // Mock behavior to return empty optional
        when(fileDBRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> fileStorageService.updateFile(fileId, mockFile));
        verify(fileDBRepository, times(1)).findById(fileId);
        verify(fileDBRepository, never()).save(any(FileDB.class));
    }

    @Test
    void deleteFile_Success() {
        // Arrange
        UUID fileId = UUID.randomUUID();
        FileDB existingFile = new FileDB();
        existingFile.setId(fileId);

        // Mock behavior
        when(fileDBRepository.findById(fileId)).thenReturn(Optional.of(existingFile));

        // Act
        fileStorageService.deleteFile(fileId);

        // Assert (verify that delete is called)
        verify(fileDBRepository, times(1)).findById(fileId);
        verify(fileDBRepository, times(1)).delete(existingFile);
    }

    @Test
    void deleteFile_FileNotFound() {
        // Arrange
        UUID fileId = UUID.randomUUID();

        // Mock behavior to return empty optional
        when(fileDBRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> fileStorageService.deleteFile(fileId));
        verify(fileDBRepository, times(1)).findById(fileId);
        verify(fileDBRepository, never()).delete(any(FileDB.class));
    }

}