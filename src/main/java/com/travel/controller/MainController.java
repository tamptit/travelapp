package com.travel.controller;

import com.travel.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;

@RestController
@RequestMapping(value = "/test")
public class MainController {

    @GetMapping("/")
    public String userRegistry() {
        return "ok";
    }
    @RequestMapping("/user")
    @ResponseBody
    public Principal user (Principal principal)
    {
        return principal;
    }

    @GetMapping("/ok")
    public String testHeroku() {
        return "ok";
    }

}

