package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

/**
 * Created by simonov on 8/1/17.
 */
public class PathStorage {
    Strategy strategy;

    public PathStorage(ObjectStreamStorage objectStreamStorage) {
    }

    public void setStrategy(Strategy strategy) {

    }

    public void ExecutedoWrite(Resume r, OutputStream os){
        try {
            strategy.doWrite(r,os);
        } catch (IOException e) {
            throw new StorageException("Write error",null,e);
        }
    }

    public void ExecutedoRead(InputStream is) throws IOException {
        strategy.doRead(is);
    }
}
