package io.moomin.proxy;

public class Lenovo implements SaleComputer {

    @Override
    public String sale(double money) {
        System.out.println("花了"+money+"元买了一台拯救者");
        return "lenovo";
    }

    @Override
    public void show() {
        System.out.println("展示");
    }
}
