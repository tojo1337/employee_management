package com.example.empmanage.mapper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathMapping {
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
