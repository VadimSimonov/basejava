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
        List<ExtensionSection>listE=new ArrayList<>();
        LocalDate startDate =LocalDate.of(1993,1,2);
        LocalDate endDate=LocalDate.of(1996,1,2);
     //   list.add(0,new Organization("Образование","http://google.ru",startDate,endDate,"санкт-питербург","Аспирантура (программист С, С++)"));
        Resume r3=new Resume("Петрович");
      //  Organization r4 = new Organization(0,new ExtensionSection(startDate,endDate,"Инженер (программист Fortran, C)","tetet"), list);

        r3.setSections(SectionType.EDUCATION,new OrganizationSection(list));
        r3.setContacts(ContactType.PHONE,"+8-909-123-45-67");
        //listE.add(0,new ExtensionSection(startDate,endDate,"Инженер (программист Fortran, C)","tetet"));
        Organization r1 = new Organization("Petr","http://google.ru");
        r1.addExtension(new ExtensionSection(startDate,endDate,"piterburg","description"));


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
