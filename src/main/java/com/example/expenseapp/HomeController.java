package com.example.expenseapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // ✅ Must be @Controller
public class HomeController {

    @GetMapping("/")  // ✅ Maps root URL to index.html
    public String home() {
        return "forward:/index.html";  // ✅ Correctly serves static index.html
    }
}