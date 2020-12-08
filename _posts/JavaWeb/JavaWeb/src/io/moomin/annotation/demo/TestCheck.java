package io.moomin.annotation.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

public class TestCheck {
    public static void main(String[] args) throws IOException {
        //创建计算器对象
        Calculator c = new Calculator();
        //获取字节码文件对象
        Class<? extends Calculator> aClass = c.getClass();
        //获取所有方法
        Method[] method = aClass.getMethods();
        int num = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter("bug.txt"));
        for (Method m :
                method) {
            if (m.isAnnotationPresent(Check.class)) {
                try {
                    m.invoke(c);
                } catch (Exception e) {
                    num++;
                    bw.write(m.getName() + "出异常了");
                    bw.newLine();
                    bw.write("异常的名称"+ e.getCause().getClass().getSimpleName());
                    bw.newLine();
                    bw.write("异常的原因"+e.getCause().getMessage());
                    bw.newLine();
                }
            }
        }
        bw.write("本次测试一共出现"+num+"次异常");
        bw.flush();
        bw.close();
        //判断方法上是否有Check注解
        //有执行
        //捕获异常
    }
}
