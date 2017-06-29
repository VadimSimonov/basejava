package ru.javawebinar.basejava.storage;

import static org.junit.Assert.*;

/**
 * Created by simonov on 6/29/17.
 */
public class MapStorageTest extends AbstractStorageTest{

    public MapStorageTest() {
        super(new MapStorage());
    }
}