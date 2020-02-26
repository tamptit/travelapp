package com.travel.controller;

import com.travel.dto.PageResponse;
import com.travel.entity.Plan;
import com.travel.repository.PlanRepository;
import com.travel.service.MapValidationErrorService;
import com.travel.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping(value = "/api/auth/")
@CrossOrigin
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Value("10")
    private int totalRowInPage;

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
        return new ResponseEntity<Plan>(plan, HttpStatus.CREATED);
    }

    // lấy 10 kế hoạch mới nhất
    @GetMapping("/list")
    public PageResponse getLatestPlan(@PathParam(value = "page") int page) throws ParseException {
//        Page<Plan> page = planRepository.findAll(
//                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));
//        return page.getContent();
        page = page <= 0 ? 0 : page - 1;
        Page<Plan> planPager = planRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id")));
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