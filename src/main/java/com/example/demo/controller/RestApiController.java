package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@CrossOrigin
public class RestApiController {

    // @Autowired
    // private UserRepository userRepository;

    @GetMapping("/test")
    public String test() {
        System.out.println("This is controller test");
        return "This is controller test";
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("This is login test");
        return "This is login test";
    }
}
