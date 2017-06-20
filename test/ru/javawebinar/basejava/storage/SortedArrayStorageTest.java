package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

/**
 * Created by simonov on 6/21/17.
 */
public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    private SortedArrayStorage storage = new SortedArrayStorage();
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";


    @Override
    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));

    }
    @Test
    public void getIndex() throws Exception {
        Assert.assertEquals(0,storage.getIndex(UUID_1));
    }

}