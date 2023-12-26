package com.ouharri.fileuploader.exception;

import com.ouharri.fileuploader.entity.FileDB;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The parent for all exceptions, associated with resources, such as {@link FileDB} etc.
 *
 *  @author <a href="mailto:ouharri.outman@gmail.com">ouharri</a>
 * @version 1.0
 */
@RestControllerAdvice
public class ResourceException extends RuntimeException {
    public ResourceException() {
    }

    public ResourceException(String message) {
        super(message);
    }
}
