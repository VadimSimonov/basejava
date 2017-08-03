package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

/**
 * Created by simonov on 8/1/17.
 */
public class FileStorage extends AbstractFileStorage {
    Strategy strategy;

    protected FileStorage(File directory,Strategy strategy) {
        super(directory);
        this.strategy=strategy;
    }


    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }


    @Override
    protected void doWrite(Resume r, OutputStream os) throws IOException {
        try {
            strategy.doWrite(r,os);
        } catch (IOException e) {
            throw new StorageException("Write error",null,e);
        }
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        return strategy.doRead(is);
    }
}
