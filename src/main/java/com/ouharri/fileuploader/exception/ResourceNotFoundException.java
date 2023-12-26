package com.ouharri.Jobinow.exceptions;

/**
 * This class is used when an attempt to find an entity has thrown an exception.
 *
 * @author <a href="mailto:ouharri.outman@gmail.com">ouharri</a>
 * @version 1.0
 */
public class ResourceNotFoundException extends ResourceException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
