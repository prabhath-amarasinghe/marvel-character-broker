package com.broker.character.marvel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MarvelCharactersData
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    private Integer offset = null;
    private Integer limit = null;
    private Integer total = null;
    private Integer count = null;

    private List<MarvelCharacter> results = new ArrayList<>();

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<MarvelCharacter> getResults() {
        return results;
    }

    public void setResults(List<MarvelCharacter> data) {
        this.results = data;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Data marvelCharactersData = (Data) o;
        return Objects.equals(this.offset, marvelCharactersData.offset) &&
                Objects.equals(this.limit, marvelCharactersData.limit) &&
                Objects.equals(this.total, marvelCharactersData.total) &&
                Objects.equals(this.count, marvelCharactersData.count) &&
                Objects.equals(this.results, marvelCharactersData.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, limit, total, count, results);
    }


}
