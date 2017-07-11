package ru.javawebinar.basejava.model;

import java.io.File;

/**
 * Created by simonov on 7/11/17.
 */
public class PhotoContent {
    private File file;

    public PhotoContent(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
