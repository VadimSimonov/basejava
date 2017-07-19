package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * gkislin
 * 19.07.2016
 */
public class Organization extends Section {
    private final Link homePage;
    private List<Position>list=new ArrayList<>();

    public Organization(String name, String url) {
        this.homePage = new Link(name, url);

    }

    public void addExtension(Position position) {
        list.add(position);
    }

    public List<Position> getList() {
        return list;
    }
}
