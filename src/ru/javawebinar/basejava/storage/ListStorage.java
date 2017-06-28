package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

/**
 * Created by simonov on 6/27/17.
 */
public class ListStorage extends AbstractStorage {
    protected ArrayList<Resume> list = new ArrayList<>();
    int size=0;
    @Override
    public void clear() {
       list.clear();
       size=0;
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            list.set(index,r);
        }
    }

    @Override
    public void save(Resume r) {
        list.add(r);
        size++;
    }

    @Override
    public Resume get(String uuid) {
        return list.stream().filter(i -> i.getUuid().equals(uuid)).findFirst().orElse(null);

    }

    @Override
    public void delete(String uuid) {
        list.remove(getIndex(uuid));
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

    @Override
    protected int getIndex(String uuid) {
        return list.indexOf(new Resume(uuid));
    }
}
