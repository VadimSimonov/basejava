package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonov on 7/13/17.
 */
public class OrganizationSection extends Section {
    private Link PageName;
    private List<Organization>lists=new ArrayList<>();


    public OrganizationSection(String name, String url) {
        this.PageName=new Link(name,url);
    }

    public void setListsJob(Organization lists) {
        this.lists.add(lists);
    }
}
