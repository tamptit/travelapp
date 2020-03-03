package com.travel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travel.config.JwtTokenProvider;
import com.travel.dto.PageResponse;
import com.travel.entity.Plan;
import com.travel.entity.User;
import com.travel.repository.PlanRepository;
import com.travel.validator.MapValidationError;
import com.travel.service.PlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/auth/")
@CrossOrigin
public class PlanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanController.class);

    @Autowired
    private PlanService planService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private MapValidationError mapValidationErrorService;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    @GetMapping()
    @ResponseBody
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    //Them ke hoach
    @PostMapping("add-plan")
    public ResponseEntity<?> createNewPlan(@Valid @RequestBody Plan plan, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;
        Plan plan1 = planService.saveOrUpdate(plan);
        return new ResponseEntity<Plan>(plan1, HttpStatus.CREATED);
    }

    // lấy 10 kế hoạch mới nhất
    @GetMapping("/list")
    public PageResponse getLatestPlan(@PathParam(value = "page") int page) throws ParseException {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByCreatedDay = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("createdDay").descending());
        Page<Plan> planPager = planRepository.findAll(sortedByCreatedDay);
        PageResponse response = new PageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(planPager.getTotalPages());
        response.setPlans(planPager.getContent());
        return response;

    }

    //     lấy 10 kế hoạch HOT nhất
    @GetMapping("/hot")
    public PageResponse getHotPlan(@PathParam(value = "page") int page) throws ParseException {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByCountUser = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("countUser").descending());
        Page<Plan> hotPager = planRepository.findAll(sortedByCountUser);
        PageResponse response = new PageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(hotPager.getTotalPages());
        response.setPlans(hotPager.getContent());
        return response;
    }

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<?> user(@AuthenticationPrincipal User principal) {
        try {
            String token = jwtTokenProvider.generateTokenFromGoogle(principal.getId(),principal.getUsername());
            return ResponseEntity.ok().body(token);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body("");
        }
    }


}