package io.moomin.reflect;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

public class ReflectTest {
    public static void main(String[] args) throws Exception {
        //可以创建任意类的对象，可以执行任意方法
        //不能改变该类的任何代码
        /*Person person = new Person();
        person.eat();*/
        Properties properties = new Properties();
        ClassLoader classLoader = ReflectTest.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("pro.properties");
        properties.load(resourceAsStream);

        String className = properties.getProperty("className");
        String methodName = properties.getProperty("methodName");

        Class<?> aClass = Class.forName(className);
        Object o = aClass.newInstance();
        Method method = aClass.getMethod(methodName);
        method.invoke(o);
    }
}
