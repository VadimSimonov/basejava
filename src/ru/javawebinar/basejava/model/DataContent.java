package ru.javawebinar.basejava.model;

import javax.xml.crypto.Data;

/**
 * Created by simonov on 7/11/17.
 */
public class DataContent extends Content {
    private Data data;

    public DataContent(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
