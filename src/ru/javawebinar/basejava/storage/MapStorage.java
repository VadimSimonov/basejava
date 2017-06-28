package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by simonov on 6/27/17.
 */
public class MapStorage extends AbstractStorage {
    protected HashMap<String,Resume> map = new HashMap<>();
    int size=0;

    @Override
    public void clear() {
        map.clear();
        size=0;
    }

    @Override
    public void update(Resume r) {
        map.put(String.valueOf(getIndex(String.valueOf(r))),r);
    }


    public void save(String str,Resume r) {
        map.put(str,r);
        size++;
    }

    @Override
    public Resume get(String uuid) {
        return map.get(String.valueOf(getIndex(uuid)));
    }

    @Override
    public void delete(String uuid) {
        map.remove(String.valueOf(getIndex(uuid)));
        size--;
    }

    @Override
    public Resume[] getAll() {
        // copy pasta from http://www.baeldung.com/convert-map-values-to-array-list-set
        Collection<Resume> values = map.values();
        Resume[]resumes= values.toArray(new Resume[values.size()]);
        return resumes;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected int getIndex(String uuid) {
        for (Map.Entry<String, Resume> entry : map.entrySet()) {
            if (entry.getValue().getUuid().equals(uuid))
                return Integer.parseInt(entry.getKey());
        }
        return -1;
    }
}
