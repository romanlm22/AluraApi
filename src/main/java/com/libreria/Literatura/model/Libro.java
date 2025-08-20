package com.libreria.Literatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Libro {

    private String title;
    private List<Autor> authors;
    private List<String> languages;

    @JsonProperty("download_count")
    private Integer downloadCount;

    public Libro() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Autor> getAuthors() { return authors; }
    public void setAuthors(List<Autor> authors) { this.authors = authors; }

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
}
