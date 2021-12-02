package com.nineclock.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/echo")
    public String echo(){
        return "OK";
    }
}
