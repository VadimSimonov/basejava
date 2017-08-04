package ru.javawebinar.basejava.storage;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new AbstractPathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamStrategy()));
    }
}
