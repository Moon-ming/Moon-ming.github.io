package io.moomin.reflect;

import io.moomin.domain.Person;

import java.lang.reflect.Constructor;

public class ReflectDemo3 {
    public static void main(String[] args) throws Exception {
        Class<Person> personClass = Person.class;
        Constructor<Person> constructor = personClass.getConstructor(String.class, int.class);
        System.out.println(constructor);
        Object o = constructor.newInstance("张三", 23);
        System.out.println(o);
        constructor.setAccessible(true);

        Person instance = personClass.newInstance();
        System.out.println(instance);
    }
}
