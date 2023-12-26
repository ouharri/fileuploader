package com.ouharri.fileuploader.repository;

import com.ouharri.fileuploader.entity.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on FileDB entities.
 */
@Repository
public interface FileDBRepository extends JpaRepository<FileDB, UUID> {

}