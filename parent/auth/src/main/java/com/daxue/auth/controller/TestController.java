package com.daxue.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daxue0929
 * @date 2022/7/23
 */

@RestController
public class TestController {


    //测试nacos配置中心是否生效
    @Value("${test}")
    public String value;

    @GetMapping("/test")
    public String test() {
        return value;
    }
}
