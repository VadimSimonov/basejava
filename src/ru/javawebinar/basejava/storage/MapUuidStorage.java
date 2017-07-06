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
        return uuid;
    }

    @Override
    protected void doUpdate(Resume r, Object uuid) {
        //map.put(String.valueOf(searchKey),r);
        map.put(String.valueOf(uuid),r);
    }

    @Override
    protected boolean isExist(Object uuid) {
       // return uuid != null;
        return map.containsKey(String.valueOf(uuid));
    }

    @Override
    protected void doSave(Resume r, Object uuid) {
        if (map.size()>=0)
            map.put(String.valueOf(uuid), r);
    }

    @Override
    protected Resume doGet(Object uuid) {
        return map.get(String.valueOf(uuid));
    }

    @Override
    protected void doDelete(Object uuid) {
        map.remove(String.valueOf(uuid));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    protected List<Resume> getAllSortedMethod(List<Resume> result) {
        result = map.values().stream().collect(Collectors.toList());
        return result;
    }

    @Override
    public int size() {
        return map.size();
    }
}
