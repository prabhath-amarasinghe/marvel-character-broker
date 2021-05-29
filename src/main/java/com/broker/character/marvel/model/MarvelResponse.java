package com.broker.character.marvel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * MarvelResponse
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarvelResponse {

    private String etag;

    private Data data;

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarvelResponse marvelResponse = (MarvelResponse) o;
        return Objects.equals(this.etag, marvelResponse.etag) &&
                Objects.equals(this.data, marvelResponse.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(etag, data);
    }

}
