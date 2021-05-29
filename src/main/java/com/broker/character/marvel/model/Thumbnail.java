package com.broker.character.marvel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * Thumbnail
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Thumbnail {


    private String path;

    private String extension;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Thumbnail thumbnail = (Thumbnail) o;
        return Objects.equals(this.path, thumbnail.path) &&
                Objects.equals(this.extension, thumbnail.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, extension);
    }


    @Override
    public String toString() {
        return "Thumbnail{" +
                "path='" + path + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
