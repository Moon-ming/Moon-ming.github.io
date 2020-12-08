package io.moomin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/anno")
public class AnnoController {
    @RequestMapping("testRequstParam")
    public String testRequestParam(@RequestParam(name = "username")String name) {
        System.out.println(name);
        return "success";
    }
    @RequestMapping("testRequstBody")
    public String testRequestBody(@RequestBody String body) {
        System.out.println(body);
        return "success";
    }
    @RequestMapping("testPathVariable/{pathid}")
    public String testPathVariable(@PathVariable(name = "pathid") String id) {
        System.out.println(id);
        return "success";
    }
}
