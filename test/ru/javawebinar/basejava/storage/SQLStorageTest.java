package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;

public class SQLStorageTest extends AbstractStorageTest {

    public SQLStorageTest() {
        super(new SqlStorage(Config.get().getDbUrl(),Config.get().getDbUser(),Config.get().getDbPassword()));
    }
}