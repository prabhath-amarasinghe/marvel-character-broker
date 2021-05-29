package com.broker.character.marvel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * MarvelCharacter
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelCharacter {

    private Long id;

    private String name;

    private String description;

    private Thumbnail thumbnail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarvelCharacter marvelCharacter = (MarvelCharacter) o;
        return Objects.equals(this.id, marvelCharacter.id) &&
                Objects.equals(this.name, marvelCharacter.name) &&
                Objects.equals(this.description, marvelCharacter.description) &&
                Objects.equals(this.thumbnail, marvelCharacter.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, thumbnail);
    }

    @Override
    public String toString() {
        return "MarvelCharacter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail=" + thumbnail +
                '}';
    }
}
