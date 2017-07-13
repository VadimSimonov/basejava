package ru.javawebinar.basejava.model;

/**
 * Created by simonov on 7/11/17.
 */
public class TextSection extends Section {
    private  String text;

    public TextSection(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
