package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@Controller
@Controller
public class SimpleRestController {
//    ajax 화면 보여주는 GETMAPPING
//    @GetMapping("/ajax")
//    public String ajax() {
//        return "ajax";
//    }
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    // 여기서 객체를 받음 post 형태로
    @PostMapping("/send")
//    @ResponseBody
    public Person test(@RequestBody Person p){
        // 서버에서 객체를 받는다.
        System.out.println("p = " + p);
        p.setName("ABC");
        p.setAge(p.getAge() + 10);
        return p; //여기서 반환을 함
    }
    @PostMapping("/send2")
//    @ResponseBody
    public Person test2(@RequestBody Person p){
        // 서버에서 객체를 받는다.
        System.out.println("p = " + p);
        p.setName("ABC");
        p.setAge(p.getAge() + 10);
        return p; //여기서 반환을 함
    }
}
