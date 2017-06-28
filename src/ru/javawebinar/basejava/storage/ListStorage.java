package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

/**
 * Created by simonov on 6/27/17.
 */
public class ListStorage extends AbstractStorage {
    protected ArrayList<Resume> list = new ArrayList<>();
    @Override
    public void clear() {
       list.clear();
       size=0;
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        }  else {
            if (size==0) {
                insertElement(r, size);
                size++;
            }else if (size>0){
                insertElement(r, size);
                size++;}
        }
    }

    @Override
    public Resume[] getAll() {
        Resume[]array=new Resume[list.size()];
        array = list.toArray(array);
        return array;
    }


    @Override
    protected int getIndex(String uuid) {
        return list.indexOf(new Resume(uuid));
    }
    @Override
    protected void updateMethod(int index, Resume r) {
        list.set(index,r);
    }
    @Override
    protected void insertElement(Resume r, int index) {
        list.add(index,r);
    }
    @Override
    protected Resume getMethod(int index) {
        return list.get(index);
    }
    @Override
    protected void deleteMethod(int index) {
        list.remove(index);
        list.trimToSize();
    }
}
