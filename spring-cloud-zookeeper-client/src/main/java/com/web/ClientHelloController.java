package com.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.remote.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientHelloController {

    @Autowired
    HelloRemote HelloRemote;

    @RequestMapping("/hello/{userName}")
    public String index(@PathVariable("userName") String userName) {
        return HelloRemote.hello(userName);
    }

}
