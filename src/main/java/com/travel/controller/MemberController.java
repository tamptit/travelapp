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
import java.util.function.Consumer;
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

    /**
     * method get list my plan creat by me
     * @param id
     * @return
     */

    @GetMapping(value = "/{id}/myplan")
    public ResponseEntity getMyPlan(@PathVariable Long id) {

        List<Plan> myPlan = userRepository.findById(id).map(user -> user.getPlans()).orElseGet(() -> null);
        //List<Plan> myPlan = user.getPlans();
        List<PlanProfileRespone> myPlanProfile = myPlan.stream()
                .map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImageCover(), p.getPlanInteractors()
                        .size())).collect(Collectors.toList());

        return ResponseEntity.ok().body(myPlanProfile);
    }
    /**
     *
     * @param id
     * method getMyFollow.
     * @return
     */
    @GetMapping(value = "/{id}/follow")
    public ResponseEntity getMyFollow(@PathVariable Long id) {
        //User user = userRepository.findById(id).orElse(null);
        List<PlanInteractor> planInteractors = userRepository.findById(id).map(user1 -> user1.getPlanInteractors()).orElseGet(() -> null);
        //List<PlanInteractor> planInteractors = user.getPlanInteractors();

        List<Plan> planFollowList = planInteractors.stream()
                .filter(p -> p.getFollow() == 1)
                .map(p -> p.getPlan())
                .sorted((p1, p2) -> p1.getCreatedDay().before(p2.getCreatedDay()) ? 1 : -1)
                .collect(Collectors.toList());

        List<PlanProfileRespone> listFollowPlan = planFollowList.stream()
                .map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImageCover(), p.getPlanInteractors().size()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listFollowPlan);
    }

    /**
     * method get list my joining plan
     * @param id
     * @return
     */

    @GetMapping(value = "/{id}/join")
    public ResponseEntity getMyJoin(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        List<PlanInteractor> planInteractors = user.getPlanInteractors();
        List<Plan> planJoinList = planInteractors.stream()
                .filter(p -> p.getJoin() == Constants.USER_JOINED)
                .map(PlanInteractor::getPlan)
                .sorted((p1, p2) -> p1.getCreatedDay().before(p2.getCreatedDay()) ? 1 : -1)
                .collect(Collectors.toList());

        List<PlanProfileRespone> listJoinPlan = planJoinList.stream()
                .map(p -> new PlanProfileRespone(p.getId(), p.getName(), p.getImageCover(), p.getPlanInteractors().size()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listJoinPlan);
    }

}
