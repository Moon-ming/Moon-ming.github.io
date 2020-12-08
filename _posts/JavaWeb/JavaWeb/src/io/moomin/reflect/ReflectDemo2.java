package io.moomin.reflect;

import io.moomin.domain.Person;

import java.lang.reflect.Field;

public class ReflectDemo2 {
    public static void main(String[] args) throws Exception {
        Class<Person> personClass = Person.class;
        Field[] fields = personClass.getFields();
        for (Field field :
                fields) {
            System.out.println(field);
        }
        Field a = personClass.getField("a");
        System.out.println(a);
        Person person = new Person();
        Object o = a.get(person);
        System.out.println(o);
        a.set(person,"zhangsan");
        System.out.println(person);

        Field[] declaredFields = personClass.getDeclaredFields();
        for (Field dec :
                declaredFields) {
            System.out.println(dec);
        }
        Field age = personClass.getDeclaredField("age");
        age.setAccessible(true);//暴力反射
        Object o1 = age.get(person);
        System.out.println(o1);
    }
}
