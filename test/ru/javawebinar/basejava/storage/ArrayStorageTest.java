package ru.javawebinar.basejava.storage;

import org.hamcrest.core.Every;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ArrayStorageTest extends AbstractArrayStorageTest{
    private Storage storage = new ArrayStorage();
    private int size;

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
        size=storage.size();
    }

    @Test
    public void getIndex() throws Exception {
        //Assert.assertThat(storage,hasItems(UUID_1,UUID_2,UUID_3));
        Assert.assertEquals(new Resume(UUID_1),storage.get(String.valueOf(UUID_1)));
        Assert.assertEquals(new Resume(UUID_2),storage.get(String.valueOf(UUID_2)));
        Assert.assertEquals(new Resume(UUID_3),storage.get(String.valueOf(UUID_3)));



    }
}