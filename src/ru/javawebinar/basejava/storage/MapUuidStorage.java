package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO implement
// TODO create new MapStorage with search key not uuid
public class MapUuidStorage extends AbstractStorage {
    private Map<String, Resume> map = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        for (Map.Entry<String, Resume> entry : map.entrySet()) {
            if (entry.getValue().getUuid().equals(uuid))
                return entry.getKey();
        }
        return null;
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        map.put(String.valueOf(searchKey),r);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        if (size>=0)
            map.put(String.valueOf(size+1), r);
        size++;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return map.get(String.valueOf(searchKey));
    }

    @Override
    protected void doDelete(Object searchKey) {
        map.remove(String.valueOf(searchKey));
        size--;
    }

    @Override
    public void clear() {
        map.clear();
        size=0;
    }
/*
    @Override
    public Resume[] getAll() {
        return new Resume[0];
    }
    */

    @Override
    public List<Resume> getAllSorted() {
        List<Resume>result = map.values().stream().collect(Collectors.toList());
        result.sort(new UuidComparator());
        return result;
    }

    @Override
    public int size() {
        return size;
    }
}
