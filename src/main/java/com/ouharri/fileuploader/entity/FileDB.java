package com.ouharri.fileuploader.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * Entity class representing a file stored in the system.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileDB extends AbstractEntity {

    /**
     * The name of the file.
     */
    @NotBlank(message = "Name cannot be blank")
    private String name;

    /**
     * The type (content type) of the file.
     */
    @NotBlank(message = "Type cannot be blank")
    private String type;

    /**
     * The binary data of the file.
     */
    @Lob
    private byte[] data;

    /**
     * Computes the hash code of this instance.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}