package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

/**
 * Created by simonov on 8/1/17.
 */
public class FileStorage {
    Strategy strategy;
    File directory;

    public FileStorage(File directory,Strategy strategy) {
        this.strategy = strategy;
        this.directory=directory;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
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
