package com.fmjava.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/loginName")
    public Map ShowName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        HashMap map = new HashMap<>();
        map.put("username",name);
        return map;
    }
}
