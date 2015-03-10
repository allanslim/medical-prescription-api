package com.codewarrior.csc686.project;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}