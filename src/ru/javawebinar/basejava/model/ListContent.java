package ru.javawebinar.basejava.model;

import java.util.List;

/**
 * Created by simonov on 7/11/17.
 */
public class ListContent {
    private List<String>list;

    public ListContent(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }


}
