package com.example.securitytest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daxue0929
 * @date 2022/7/27
 */

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
