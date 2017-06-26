package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import static org.hamcrest.CoreMatchers.*;

public abstract class AbstractArrayStorageTest {
    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    private Storage storage = new ArrayStorage();

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid33";

    private static final Resume UpdateUiid = new Resume(UUID_1);
    private static final Resume NotExistUiid = new Resume(UUID_4);
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
        Assert.assertEquals(0,storage.size());
    }

    @Test
    public void update() throws Exception {
        storage.update(UpdateUiid);
        Assert.assertEquals(UpdateUiid,storage.get(UpdateUiid.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(NotExistUiid);
    }

    @Test
    public void getAll() throws Exception {
        Resume[]str=storage.getAll();
        Assert.assertEquals(new Resume(UUID_1),str[0]);
        Assert.assertEquals(new Resume(UUID_2),str[1]);
        Assert.assertEquals(new Resume(UUID_3),str[2]);
    }

    @Test
    public void save() throws Exception {
        storage.save(SaveUiid);
        Assert.assertEquals(SaveUiid,storage.get(SaveUiid.getUuid()));
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() throws Exception {
        try {
            for (int i = 4; i <=AbstractArrayStorage.STORAGE_LIMIT ; i++) {
                storage.save(new Resume());
        }
        }catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(String.valueOf(UpdateUiid));
        Assert.assertThat(UpdateUiid,equalTo(storage.get(UpdateUiid.getUuid())));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(NotExistUiid.getUuid());
    }

    @Test
    public void get() throws Exception {
        Assert.assertThat(UpdateUiid,equalTo(storage.get(UpdateUiid.getUuid())));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}