package ru.javawebinar.basejava.model;

import java.time.LocalDate;

/**
 * Created by simonov on 7/20/17.
 */
public class Organization {
    private LocalDate StartDate;
    private LocalDate EndDate;
    private String title;
    private String description;

    public Organization(LocalDate startDate, LocalDate endDate, String title, String description) {
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.title = title;
        this.description = description;
    }
}
