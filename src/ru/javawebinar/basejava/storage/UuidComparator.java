package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;

/**
 * Created by simonov on 7/4/17.
 */
public class UuidComparator implements Comparator<Resume> {
    @Override
    public int compare(Resume o1, Resume o2) {
        return o1.getUuid().compareTo(o2.getUuid());
    }
}
