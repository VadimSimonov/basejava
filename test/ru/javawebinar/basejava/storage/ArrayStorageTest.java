package ru.javawebinar.basejava.storage;

import org.hamcrest.core.Every;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ArrayStorageTest extends AbstractArrayStorageTest{
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

}