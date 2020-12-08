package io.moomin.utils;

import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverter implements Converter<String,Date> {


    @Override
    public Date convert(String s) {
        if (s == null) {
            throw new RuntimeException("请传入数据");
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(s);
        } catch (Exception e) {
            throw new RuntimeException("类型转换出现错误");
        }
    }
}
