package ru.javawebinar.basejava.model;

import javax.xml.crypto.Data;

/**
 * Created by simonov on 7/13/17.
 */
public class ExperienceEducationSection extends Section {
    private String title;
    private String url;
    private Data StartDate;
    private Data EndDate;
    private String description;

    public ExperienceEducationSection(String name, String url, Data StartDate, Data EndDate, String description) {
        this.title = name;
        this.url = url;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Data getStartDate() {
        return StartDate;
    }

    public Data getEndDate() {
        return EndDate;
    }

    public String getDescription() {
        return description;
    }
}
