package com.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.dto.PlanProfileRespone;
import com.travel.dto.ProfileForm;
import com.travel.dto.UserForm;
import com.travel.dto.UserPageResponse;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping(value = "/profile")
    public ResponseEntity<Object> getUserProfileFromToken() {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        String email = au.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            UserForm userForm = new UserForm();
            userForm.setUsername(user.getUsername());
            userForm.setEmail(user.getEmail());
            userForm.setdOfB(user.getdOfB());
            userForm.setGender(user.isGender());
            userForm.setFullName(user.getFullName());
            return new ResponseEntity<>(userForm, HttpStatus.OK);
        }
        return new ResponseEntity<>(Constants.FAIL_TO_LOAD_USERDETAILS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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
        ProfileForm profileForm = new ProfileForm();
        User user = userRepository.findById(id).orElse(null);

        com.travel.model.User userModel = new com.travel.model.User(user.getId(), user.getUsername(), user.getEmail(),
                user.getFullName(), user.getdOfB(), user.isGender(), user.getJoinDate());
        //com.travel.model.User userModel = objectMapper.convertValue(user, com.travel.model.User.class);

        profileForm.setUser(userModel);
        List<PlanInteractor> planInteractors = user.getPlanInteractors();

        List<Plan> myPlan = user.getPlans();
        List<PlanProfileRespone> myPlanProfile = myPlan.stream().map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImage(), p.getPlanInteractors()
                .size())).collect(Collectors.toList());

        //List<PlanInteractor> planInteractors = planInteractorRepository.findByUserId(user.getId());

        List<Plan> planFollowList = planInteractors.stream()
                .filter(p -> p.getStatus() == Constants.USER_FOLLOW_STATUS || p.getStatus() == Constants.USER_JOIN_REQUEST)
                .map(p -> p.getPlan())
                .sorted((p1, p2) -> p1.getCreatedDay().before(p2.getCreatedDay()) ? 1 : -1)
                .collect(Collectors.toList());

        List<PlanProfileRespone> listFollowPlan = planFollowList.stream()
                .map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImage(), p.getPlanInteractors().size()))
                .collect(Collectors.toList());

        List<Plan> planJoinList = planInteractors.stream()
                .filter(p -> p.getStatus() == Constants.USER_JOINED)
                .map(p -> p.getPlan())
                .sorted((p1, p2) -> p1.getCreatedDay().before(p2.getCreatedDay()) ? 1 : -1)
                .collect(Collectors.toList());

        List<PlanProfileRespone> listJoinPlan = planJoinList.stream()
                .map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImage(), p.getPlanInteractors().size()))
                .collect(Collectors.toList());

        profileForm.setListFollowPlan(new ArrayList<>());
        profileForm.setListJoinPlan(new ArrayList<>());
        profileForm.setListMyPlan(new ArrayList<>());

        if (myPlanProfile.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                profileForm.getListMyPlan().add(myPlanProfile.get(i));
            }
        } else {
            profileForm.setListMyPlan(myPlanProfile);
        }

        if (listFollowPlan.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                profileForm.getListFollowPlan().add(listFollowPlan.get(i));
            }
        } else {
            profileForm.setListFollowPlan(listFollowPlan);
        }

        if (listJoinPlan.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                profileForm.getListJoinPlan().add(listJoinPlan.get(i));
            }
        } else {
            profileForm.setListJoinPlan(listJoinPlan);
        }

        return ResponseEntity.ok().body(profileForm);
    }

}
