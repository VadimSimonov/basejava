package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.MapResumeStorage;

import java.time.LocalDate;

/**
 * Test ru.javawebinar.basejava.storage.ArrayStorage
 */
public class MainTestResumeStorage {
    static final MapResumeStorage ARRAY_STORAGE = new MapResumeStorage();

    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(1991,1,3);LocalDate startDate2 = LocalDate.of(1991,1,3);
        LocalDate endDate = LocalDate.of(1991,1,3);LocalDate endDate2 = LocalDate.of(1991,1,3);

        Resume r1 = new Resume("Петрович Иван Васильевич");
        OrganizationSection OS = new OrganizationSection("Gazprom","gazprom.ru");
        Organization organization= new Organization(startDate,endDate,"admin","windows");
        Organization organization1= new Organization(startDate2,endDate2,"admin2","windows2");
        r1.setContacts(Contacts.MAIL,"mail@mail.ru");
        OS.setListsJob(organization);
        OS.setListsJob(organization1);
        r1.setSection(SectionType.EDUCATION,OS);

        /*
        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
        */
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}
