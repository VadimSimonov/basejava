package ru.javawebinar.basejava.storage;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by simonov on 6/29/17.
 */
public class ListStorageTest extends AbstractStorageTest {
    public ListStorageTest() {
        super(new ListStorage());
    }
}