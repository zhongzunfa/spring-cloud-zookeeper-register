package com.web;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/hello")
    public String GetHelloString(@RequestParam String userName){
        return "Hello:"+userName+" ,I am from 8822 port !";
    }

}
