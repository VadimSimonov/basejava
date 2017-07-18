package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by simonov on 7/18/17.
 */
public class ExtensionSection {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String description;
    private final String title;

    public ExtensionSection(LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(title, "title must not be null");
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.title = title;
    }
}
