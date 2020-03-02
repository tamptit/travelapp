package com.travel.controller;


import com.travel.dto.MyUserForm;
import com.travel.dto.UserPageResponse;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
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

    private MyUserForm myUserForm ;

    @GetMapping(value = "/new-comer")
    public UserPageResponse getNewComers(@PathParam(value = "page") int page) {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByNewComer = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("joinDate").descending());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, NUM_DAY);
        Date agoDate = calendar.getTime();

        Page<User> memberPager =
                userRepository.findAllWithJoinDateAfter(agoDate,sortedByNewComer);
        UserPageResponse response = new UserPageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(memberPager.getTotalPages());
        response.setUsers(memberPager.getContent());
        return response;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUserPlan(@PathVariable Long id){
        User user = userRepository.findById(id).get();

        com.travel.model.User userModel = new com.travel.model.User(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getdOB(), user.isGender(), user.getJoinDate());
        myUserForm.setUser(userModel);
        List<PlanInteractor> planInteractors = planInteractorRepository.findByUser(user);
        //List<PlanInteractor> joinPlan =  planInteractors.stream().filter(p -> p.getStatus() == 0).collect(Collectors.toList());
        //List<Plan> plans = planInteractors.stream().map(p -> p.getPlan()).collect(Collectors.toList());
        List<Plan> flowPlan = new ArrayList<Plan>();
        List<Plan> joinPlan = new ArrayList<Plan>();
        for (PlanInteractor p : planInteractors) {
            if (p.getStatus() == 0) {
                flowPlan.add(p.getPlan());
            }
            if (p.getStatus() == 1) {
                joinPlan.add(p.getPlan());
            }
        }

        myUserForm.setPlanInteractor(planInteractors);
        myUserForm.setJoinPlan(joinPlan);
        myUserForm.setFlowPlan(flowPlan);
        return ResponseEntity.ok().body(myUserForm);
    }

}
