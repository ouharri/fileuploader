package com.ouharri.fileuploader.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object representing a simple message for client communication.
 */
@Getter
@Setter
@AllArgsConstructor
public class ResponseMessage {

    /**
     * The message content.
     */
    private String message;
}