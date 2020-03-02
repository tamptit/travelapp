package com.travel.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.dto.MyUserForm;
import com.travel.dto.UserPageResponse;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import jdk.vm.ci.meta.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/member")
public class MemberController {

    private static final int NUM_DAY = -7;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    PlanInteractorRepository planInteractorRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping(value = "/new-comer")
    public UserPageResponse getNewComers(@PathParam(value = "page") int page) {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByNewComer = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("joinDate").descending());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, NUM_DAY);
        Date agoDate = calendar.getTime();

        Page<User> memberPager =
                userRepository.findAllWithJoinDateAfter(agoDate, sortedByNewComer);
        UserPageResponse response = new UserPageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(memberPager.getTotalPages());
        response.setUsers(memberPager.getContent());
        return response;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUserPlan(@PathVariable Long id) {
        MyUserForm myUserForm = new MyUserForm();
        User user = userRepository.findById(id).orElse(null);

        com.travel.model.User userModel = new com.travel.model.User(user.getId(), user.getUsername(), user.getEmail(),
                user.getFullName(), user.getdOfB(), user.isGender(), user.getJoinDate());
        //com.travel.model.User userModel = objectMapper.convertValue(user, com.travel.model.User.class);
        myUserForm.setUser(userModel);
        List<PlanInteractor> planInteractors = planInteractorRepository.findByUser(user);
        //List<Plan> joinPlan = planInteractors.stream().filter(p -> p.getStatus() == Constants.USER_JOINED).map(p -> p.getPlan()).collect(Collectors.toList());
        List<Plan> flowPlan = planInteractors.stream().filter(p -> p.getStatus() == Constants.USER_FOLLOW_STATUS || p.getStatus() == Constants.USER_JOIN_REQUEST).map(p -> p.getPlan()).collect(Collectors.toList());
        //myUserForm.setJoinPlan(joinPlan);
        myUserForm.setFlowPlan(flowPlan);
        return ResponseEntity.ok().body(myUserForm);
    }

}
