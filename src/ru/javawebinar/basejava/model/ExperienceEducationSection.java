package ru.javawebinar.basejava.model;

import javax.xml.crypto.Data;

/**
 * Created by simonov on 7/13/17.
 */
public class ExperienceEducationSection extends Content {
    private String name;
    private String url;
    private Data data;
    private String description;

    public ExperienceEducationSection(String name, String url, Data data, String description) {
        this.name = name;
        this.url = url;
        this.data = data;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Data getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }
}
