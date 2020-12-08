package io.moomin.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        Lenovo lenovo = new Lenovo();

        //动态代理增强
        //三个参数
        // 类加载器:真实对象.getClass().getClassLoader()
        //接口数组:真实对象.getClass().getInterfaces()
        //处理器:new InvocationHandler()

        SaleComputer proxy = (SaleComputer) Proxy.newProxyInstance(lenovo.getClass().getClassLoader(), lenovo.getClass().getInterfaces(), new InvocationHandler() {
            /*
            代理逻辑编写的方法:代理对象调用的所有方法都会触发该方法执行
            参数
                proxy对象
                method:代理对象调用的方法,被封装为对象
                args:代理对象调用的方法时,传递的实际参数
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                System.out.println("invoke 执行");
                //增强参数
                //判断是否是sale方法
                if (method.getName().equals("sale")) {
                    double money = (double) args[0];
                    money = money * 0.85;
                    //使用真实对象调用该方法
                    String invoke = (String) method.invoke(lenovo,money);
                    return invoke + "鼠标垫";
                } else {
                    Object invoke = method.invoke(lenovo, args);
                    return invoke;
                }


            }
        });

        String sale = proxy.sale(8000);
        System.out.println(sale);
        proxy.show();
    }
}
