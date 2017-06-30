package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    protected Resume getMethod(int index) {
        return storage[index];
    }

    @Override
    protected void deleteMethod(int index) {
        fillDeletedElement(index);
        storage[size - 1] = null;
    }
    @Override
    protected void updateMethod(int index, Resume r) {
        storage[index] = r;
    }

    protected abstract void fillDeletedElement(int index);
    protected abstract int getIndex(String uuid);
}
