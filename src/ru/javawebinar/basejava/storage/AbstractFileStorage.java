package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * gkislin
 * 22.07.2016
 */
public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;


    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        File[]files=directory.listFiles();
        if (files!=null)
        for (File f:files) {
            f.delete();
        }
    }

    @Override
    public int size() {
        try {
            return directory.listFiles().length;
        }catch (NullPointerException e)
        {
            throw new NullPointerException("directory is empty");
        }

    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r,file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }

    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        }catch (IOException e)
        {
            throw new StorageException("Read file error",file.getName(),e);
        }
    }

    @Override
    protected void doDelete(File file) {
            if(!file.delete());
            throw new StorageException("File deleted error",file.getName());
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume>list=new ArrayList<>();
        File[]files=directory.listFiles();
        if (files!=null) {
            for (File f : files) {
                list.add(doGet(f));
            }
        }else throw new StorageException("Directory is clear",null);
            return list;
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;
    protected abstract Resume doRead(File file) throws IOException;
}
