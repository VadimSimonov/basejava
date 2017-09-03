package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.sql.SQLHelper;

public class SQLStorageTest extends AbstractStorageTest {

    public SQLStorageTest() {
        super(new SqlStorage(new SQLHelper(Config.get().getDbUrl(),Config.get().getDbUser(),Config.get().getDbPassword())));
    }
}