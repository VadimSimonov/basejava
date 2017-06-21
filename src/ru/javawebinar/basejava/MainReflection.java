package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class MainReflection {

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        Resume r = new Resume();

        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        // TODO : invoke r.toString via reflection
        // java-course.ru/begin/reflection
        Class f = r.getClass();
        Resume sc = (Resume) f.newInstance();
        java.lang.reflect.Method method= f.getMethod("toString");
        String string = (String) method.invoke(sc);
        System.out.println(string);

        System.out.println(r);


    }
}