package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonov on 6/27/17.
 */
public class ListStorage extends AbstractStorage {
    protected ArrayList<Resume> list = new ArrayList<>();
    int size=0;
    @Override
    public void clear() {
       list.clear();
    }

    @Override
    public void update(Resume r) {
        super.update(r);
    }

    @Override
    public void save(Resume r) {
        list.add(r);
        size++;
    }

    @Override
    public Resume get(String uuid) {
        return super.get(uuid);
    }

    @Override
    public void delete(String uuid) {
        super.delete(uuid);
        size--;
    }

    @Override
    public Resume[] getAll() {
        Resume[]array=new Resume[list.size()];
        array = list.toArray(array);
        return array;
    }

    @Override
    public int size() {
        return size;
    }
}
