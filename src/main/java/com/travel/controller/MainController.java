package com.travel.controller;

import com.travel.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin(origins = "localhost:8080/travel.com", maxAge = 3600)
@RestController
@RequestMapping(value = "/test")
public class MainController {

    @GetMapping("/")
    public String userRegistry() {
        return "ok";
    }

    @GetMapping("/ok")
    public String testHeroku() {
        return "ok";
    }

}

