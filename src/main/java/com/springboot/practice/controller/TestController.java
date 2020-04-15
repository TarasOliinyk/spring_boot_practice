package com.springboot.practice.controller;

import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    public String get() {
        return "HELLO WORLD!";
    }
}
