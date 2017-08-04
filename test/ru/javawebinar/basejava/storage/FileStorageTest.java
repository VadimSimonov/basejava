package ru.javawebinar.basejava.storage;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {super(new AbstractFileStorage(STORAGE_DIR,new ObjectStreamStrategy()));
    }
}
