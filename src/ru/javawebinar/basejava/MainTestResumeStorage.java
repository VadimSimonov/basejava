package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.MapResumeStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Test ru.javawebinar.basejava.storage.ArrayStorage
 */
public class MainTestResumeStorage {
    static final MapResumeStorage ARRAY_STORAGE = new MapResumeStorage();

    public static void main(String[] args) {
        List<Organization>list=new ArrayList<>();
        LocalDate startDate =LocalDate.of(1993,1,2);  LocalDate startDate2 =LocalDate.of(1997,1,2);
        LocalDate endDate=LocalDate.of(1996,1,2);   LocalDate endDate2=LocalDate.of(1999,1,2);

        Resume r3=new Resume("Петрович");
        r3.setSections(SectionType.EDUCATION,new OrganizationSection(list));
        r3.setContacts(ContactType.PHONE,"+8-909-123-45-67");
        Organization o=new Organization("Vasya","yandex.ru");
        r3.setSections(SectionType.ACHIEVEMENT,new Organization("Vasya","yandex.ru"));
        r3.setSections(SectionType.ACHIEVEMENT,o.addExtension(new Position(startDate,endDate,"piterburg","description")));

/*
        Organization r1 = new Organization("Petr","http://google.ru");
        r1.addExtension(new Position(startDate,endDate,"piterburg","description"));
        r1.addExtension(new Position(startDate2,endDate2,"piterburg2","description2"));
*/
        //ARRAY_STORAGE.save(r1);
        //ARRAY_STORAGE.save(r2);
        //ARRAY_STORAGE.save(r1);
        printAll();

        /*
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
