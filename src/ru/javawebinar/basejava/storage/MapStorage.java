package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;

/**
 * Created by simonov on 6/27/17.
 */
public class MapStorage extends AbstractStorage {
    protected HashMap<Resume,Resume> map = new HashMap<>();

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void update(Resume r) {
        super.update(r);
    }

    @Override
    public void save(Resume r) {
        super.save(r);
    }

    @Override
    public Resume get(String uuid) {
        return super.get(uuid);
    }

    @Override
    public void delete(String uuid) {
        super.delete(uuid);
    }

    @Override
    public Resume[] getAll() {
        return super.getAll();
    }

    @Override
    public int size() {
        return super.size();
    }
}
