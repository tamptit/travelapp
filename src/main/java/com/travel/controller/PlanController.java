package com.travel.controller;

import com.travel.entity.Plan;
import com.travel.repository.PlanRepository;
import com.travel.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth/")
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepository planRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    //Them ke hoach
    @PostMapping("/plan")
    public ResponseEntity<?> createNewPlan(@Valid @RequestBody Plan plan, BindingResult result)  {
        if (result.hasErrors())
        {
            Map<String,String> errorMap = new HashMap<>();

            for (FieldError error: result.getFieldErrors())
            {
                errorMap.put(error.getField(),error.getDefaultMessage());
            }

            return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);
        }
        Plan plan1 = planService.saveOrUpdate(plan);
        return new ResponseEntity<Plan>(plan, HttpStatus.CREATED);
    }

    // lấy 10 kế hoạch mới nhất
    @GetMapping("/list")
    @ResponseBody
    public List<Plan> getLatestPlan( Plan plan) throws ParseException {
        Page<Plan> page = planRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));
        return page.getContent();
    }
    //     lấy 10 kế hoạch HOT nhất
    @GetMapping("/hot")
    @ResponseBody
    public List<Plan> getHotPlan( Plan plan) throws ParseException {
        Page<Plan> page = planRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "activeUser").and(Sort.by(Sort.Direction.DESC, "followUser"))));
        return page.getContent();
    }
    @GetMapping("/user")
    @ResponseBody
    public Principal user (Principal principal)
    {
        return principal;
    }

}