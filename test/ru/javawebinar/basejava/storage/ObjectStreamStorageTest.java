package ru.javawebinar.basejava.storage;

public class ObjectStreamStorageTest extends AbstractStorageTest {

    public ObjectStreamStorageTest() {
        super((Storage) new ObjectStreamStorage());
    }
}
