package io.moomin.test;

import io.moomin.junit.Calculator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTetst {
    @Before
    public void init(){
        System.out.println("init...");
    }
    @After
    public void close(){
        System.out.println("clsoe...");
    }
    @Test
    public void testAdd() {
        Calculator c = new Calculator();
        int res = c.add(1, 2);
        System.out.println(res);
        Assert.assertEquals(3,res);
    }
}
