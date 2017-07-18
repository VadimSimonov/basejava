package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * gkislin
 * 19.07.2016
 */
public class Organization {
    private final Link homePage;
    private List<ExtensionSection>list=new ArrayList<>();

    public Organization(String name, String url) {
        this.homePage = new Link(name, url);

    }

    public void addExtension(ExtensionSection extensionSection) {
        list.add(extensionSection);
    }

    public List<ExtensionSection> getList() {
        return list;
    }
}
