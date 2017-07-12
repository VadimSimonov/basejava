package ru.javawebinar.basejava.model;

/**
 * Created by simonov on 7/11/17.
 */
public class TextContent extends Content {
    private  String text;

    public TextContent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
