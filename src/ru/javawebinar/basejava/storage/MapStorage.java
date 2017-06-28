package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simonov on 6/27/17.
 */
public class MapStorage extends AbstractStorage {
    protected HashMap<String,Resume> map = new HashMap<>();

    @Override
    public void clear() {
        map.clear();
        size=0;
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        } else {
            if (size>=0)
            insertElement(r, size+1);
            size++;
        }
    }

    @Override
    public Resume[] getAll() {
        // copy pasta from http://www.baeldung.com/convert-map-values-to-array-list-set
        Collection<Resume> values = map.values();
        Resume[]resumes= values.toArray(new Resume[values.size()]);
        return resumes;
    }

    @Override
    protected int getIndex(String uuid) {
        for (Map.Entry<String, Resume> entry : map.entrySet()) {
            if (entry.getValue().getUuid().equals(uuid))
                return Integer.parseInt(entry.getKey());
        }
        return -1;
    }

    @Override
    protected void updateMethod(int index, Resume r) {
        map.put(String.valueOf(index),r);
    }

    @Override
    protected void insertElement(Resume r, int index) {
        map.put(String.valueOf(index),r);
    }

    @Override
    protected Resume getMethod(int index) {
        return map.get(index);
    }

    @Override
    protected void deleteMethod(int index) {
        map.remove(String.valueOf(index));
    }
}
