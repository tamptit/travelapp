package com.travel.controller;

import com.travel.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping(value = "/api")
public class MainController {

    @GetMapping("/")
    public String userRegistry() {
        return "ok";
    }

}

