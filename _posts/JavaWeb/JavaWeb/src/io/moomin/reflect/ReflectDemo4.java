package io.moomin.reflect;

import io.moomin.domain.Person;

import java.lang.reflect.Method;

public class ReflectDemo4 {
    public static void main(String[] args) throws Exception {
        Class<Person> personClass = Person.class;
        Method eat = personClass.getMethod("eat");
        Person person = new Person();
        eat.invoke(person);

        Method eat1 = personClass.getMethod("eat",String.class);
        eat1.invoke(person, "apple");

        Method[] methods = personClass.getMethods();
        for (Method m :
                methods) {
            System.out.println(m.getName());
            System.out.println(m);
        }
        String name = personClass.getName();
        System.out.println(name);
    }
}
