package io.moomin.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.moomin.domain.Person;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JacksonTest {
    @Test
    public void test1() throws IOException {
        Person person = new Person();
        person.setAge(23);
        person.setName("张三");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(person);
        System.out.println(s);
        objectMapper.writeValue(new File("d://a.txt"),person);
        objectMapper.writeValue(new FileWriter("d://b.txt"),person);
    }
    @Test
    public void test2() throws IOException {
        Person person = new Person();
        person.setAge(23);
        person.setName("张三");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(person);
        System.out.println(s);
    }
    @Test
    public void test3() throws IOException {
        Person person = new Person();
        person.setAge(23);
        person.setName("张三");
        Person person1 = new Person();
        person1.setAge(23);
        person1.setName("张三");
        Person person2 = new Person();
        person2.setAge(23);
        person2.setName("张三");
        ArrayList<Person> people = new ArrayList<>();
        people.add(person);
        people.add(person1);
        people.add(person2);

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(people);
        //[{},{},{}]
        System.out.println(s);
    }
    @Test
    public void test4() throws IOException {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("name","张三");
        stringObjectHashMap.put("age","23");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(stringObjectHashMap);
        //{"":"","":""}
        System.out.println(s);
    }
    @Test
    public void test5() throws IOException {
        String json = "{\"name\":\"张三\",\"age\":\"23\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(json, Person.class);
        System.out.println(person);
    }
}
