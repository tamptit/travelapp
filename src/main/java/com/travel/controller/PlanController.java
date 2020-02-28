package com.travel.controller;

import com.travel.dto.PageResponse;
import com.travel.entity.Plan;
import com.travel.repository.PlanRepository;
import com.travel.service.PlanService;
import com.travel.validator.MapValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/plan")
@CrossOrigin
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepository planRepository;
    public static final int TOTAL_ROW_IN_PAGE = 10;
    @Autowired
    private MapValidationError mapValidationErrorService;

    @RequestMapping(method = RequestMethod.GET)
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
        Page<Plan> hotPager = planRepository.findAll(
                PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "activeUser").and(Sort.by(Sort.Direction.DESC, "followUser"))));
        PageResponse response = new PageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(hotPager.getTotalPages());
        response.setPlans(hotPager.getContent());
        return response;
    }

    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

}