package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private Path directory;

    protected abstract void doWrite(Resume r, OutputStream os) throws IOException;

    protected abstract Resume doRead(InputStream is) throws IOException;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        try {
            return Math.toIntExact(Files.list(Paths.get(directory.toUri())).count());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected Path getSearchKey(String uuid) {
         return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Path Path) {
        try {
            doWrite(r, new FileOutputStream(Path.toFile().toString()));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(Path Path) {
        return Files.exists(Path);
    }

    @Override
    protected void doSave(Resume r, Path Path) {
        try {
            Files.createFile(Path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create Path " + directory.toAbsolutePath(), null, e);
        }
        doUpdate(r, Path);
    }

    @Override
    protected Resume doGet(Path Path) {
        try {
            return doRead(new FileInputStream(Path.toFile()));
        } catch (IOException e) {
            throw new StorageException("Path read error",Path.toString(), e);
        }
    }

    @Override
    protected void doDelete(Path Path) {
        try {
            Files.delete(Path);
        } catch (IOException e) {
            throw new StorageException("delete Path error", null, e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        try {
           return Files.list(directory)
                   .map(i->doGet(i))
                   .collect(Collectors.toList());

        } catch (IOException e) {
            throw new StorageException("Directory error",null,e);
        }


    }
}
