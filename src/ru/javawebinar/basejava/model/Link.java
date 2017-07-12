package ru.javawebinar.basejava.model;

/**
 * Created by simonov on 7/11/17.
 */
public class Link  extends Content{
    private String name;
    private String url;

    public Link(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
