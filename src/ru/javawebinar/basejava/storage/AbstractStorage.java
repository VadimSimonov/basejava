package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

/**
 * Created by simonov on 6/27/17.
 */
public abstract class AbstractStorage implements Storage {
    protected static final int STORAGE_LIMIT = 5;
    protected int size = 0;

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            updateMethod(index, r);
        }
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        }
            insertElement(r, index);
            size++;

    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        NotExistMethod(index,uuid);
        return getMethod(index);
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        NotExistMethod(index,uuid);
            deleteMethod(index);
            size--;
        }

    @Override
    public int size() {
        return size;
    }

    private void NotExistMethod(int index, String uuid) {
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract int getIndex(String uuid);
    protected abstract void updateMethod(int index, Resume r);
    protected abstract void insertElement(Resume r, int index);
    protected abstract Resume getMethod(int index);
    protected abstract void deleteMethod(int index);
}
