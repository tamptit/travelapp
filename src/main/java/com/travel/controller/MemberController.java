package com.travel.controller;


import com.travel.entity.User;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "api/member")
public class MemberController {


    static final int NUM_DAY = -7;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/new-comer")
    public ResponseEntity getNewComers() {
        List<User> listNewComer = new ArrayList<User>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, NUM_DAY);
        Date agoDate = calendar.getTime();
        listNewComer = userRepository.findAllWithJoinDateAfter(agoDate);
        return ResponseEntity.ok().body(listNewComer);
    }

}
