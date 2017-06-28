package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.ListStorage;
import ru.javawebinar.basejava.storage.MapStorage;

import java.util.LinkedHashMap;

/**
 * Test ru.javawebinar.basejava.storage.ArrayStorage
 */
public class MapMainTestStorage {
    static final MapStorage MAP_STORAGE = new MapStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume("uuid1");
        Resume r2 = new Resume("uuid2");
        Resume r3 = new Resume("uuid3");

        MAP_STORAGE.save("1",r1);
        MAP_STORAGE.save("2",r2);
        MAP_STORAGE.save("3",r3);

        printAll();
        System.out.println("Get r1: " + MAP_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + MAP_STORAGE.size());
        System.out.println("Get dummy: " + MAP_STORAGE.get("dummy"));

        printAll();
        MAP_STORAGE.delete(r1.getUuid());
        printAll();
        MAP_STORAGE.update(r2);
        printAll();
        MAP_STORAGE.clear();
        printAll();

        System.out.println("Size: " + MAP_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : MAP_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
