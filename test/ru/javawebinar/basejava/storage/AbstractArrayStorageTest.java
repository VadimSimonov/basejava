package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import static org.hamcrest.CoreMatchers.*;

public abstract class AbstractArrayStorageTest {
    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    private Storage storage = new ArrayStorage();
    private Storage storageTest = new ArrayStorage();

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final Resume UpdateUiid = new Resume(UUID_1);
    private static final Resume SaveUiid = new Resume("muhaha");

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertThat(storage.getAll(),is(storageTest.getAll()));
    }

    @Test
    public void update() throws Exception {
        storage.update(UpdateUiid);
        Assert.assertEquals(UpdateUiid,storage.get(String.valueOf(UpdateUiid)));

    }

    @Test
    public void getAll() throws Exception {
        Resume[]str=storage.getAll();
        Assert.assertEquals(new Resume(UUID_1),storage.get(UUID_1));
        Assert.assertEquals(new Resume(UUID_2),storage.get(UUID_2));
        Assert.assertEquals(new Resume(UUID_3),storage.get(UUID_3));
    }

    @Test
    public void save() throws Exception {
        storage.save(SaveUiid);
        Assert.assertEquals(SaveUiid,storage.get(String.valueOf(SaveUiid)));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(String.valueOf(UpdateUiid));
        Assert.assertThat(UpdateUiid,not(equalTo(storage.get(String.valueOf(UpdateUiid)))));
    }

    @Test
    public void get() throws Exception {
        Assert.assertThat(UpdateUiid,equalTo(storage.get(String.valueOf(UpdateUiid))));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}