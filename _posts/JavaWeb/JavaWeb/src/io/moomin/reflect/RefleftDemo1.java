package io.moomin.reflect;

import io.moomin.domain.Person;

public class RefleftDemo1 {
    public static void main(String[] args) throws Exception {
        Class aClass = Class.forName("io.moomin.domain.Person");
        System.out.println(aClass);
        Class<Person> personClass = Person.class;
        System.out.println(personClass);
        Person person = new Person();
        Class<? extends Person> aClass1 = person.getClass();
        System.out.println(aClass1);

        System.out.println(aClass == personClass);
        System.out.println(personClass == aClass1);
    }
}
