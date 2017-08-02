package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tirael on 02.08.2017.
 */
public interface Strategy {
    void doWrite(Resume r, OutputStream os) throws IOException;
    Resume doRead(InputStream is) throws IOException;
}
