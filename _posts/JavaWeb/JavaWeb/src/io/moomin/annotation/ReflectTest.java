package io.moomin.annotation;

import java.lang.reflect.Method;

@Pro(className = "io.moomin.annotation.Demo1",methodName = "show")
public class ReflectTest {
    public static void main(String[] args) throws Exception {
        //可以创建任意类的对象，可以执行任意方法
        //不能改变该类的任何代码
        //解析注解
        //获取该类的字节码文件对象
        Class<ReflectTest> reflectTestClass = ReflectTest.class;
        //获取上边的注解对象
        //其实就是在内存中生成一个该注解接口的子类实现对象
        /*
        public class ProImpl implements Pro{
            public String className(){
                return "io.moomin.annotation.Demo1";
            }
            public String methodName(){
                return "show";
            }
        }
        * */
        Pro an = reflectTestClass.getAnnotation(Pro.class);
        //调用注解对象中定义的抽象方法（属性），获取返回值
        String s = an.className();
        String s1 = an.methodName();
        /*System.out.println(s);
        System.out.println(s1);*/
        Class<?> aClass = Class.forName(s);
        Method method = aClass.getMethod(s1);
        Object o = aClass.newInstance();
        method.invoke(o);
    }
}
