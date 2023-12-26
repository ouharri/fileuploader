package com.ouharri.fileuploader.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object representing a file for client communication.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseFile {

    /**
     * The name of the file.
     */
    private String name;

    /**
     * The URL for downloading or accessing the file.
     */
    private String url;

    /**
     * The content type (MIME type) of the file.
     */
    private String type;

    /**
     * The size of the file in bytes.
     */
    private long size;
}