package com.travel.controller;

import com.travel.entity.Plan;
import com.travel.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class PlanController {
    @Autowired
    private PlanRepository planRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @PostMapping("/plan")
    public ResponseEntity setPlan(@RequestBody Plan plan) throws ParseException {
        planRepository.save(plan);
        return ResponseEntity.ok().body(plan);
    }

    // lấy 10 kế hoạch mới nhất
    @GetMapping("/list")
    public List<Plan> getLatestPlan(@RequestBody Plan plan) throws ParseException {
        Page<Plan> page = planRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));
        return page.getContent();
    }

    //     lấy 10 kế hoạch HOT nhất
    @GetMapping("/hot")
    public List<Plan> getHotPlan(@RequestBody Plan plan) throws ParseException {
        Page<Plan> page = planRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "activeUser").and(Sort.by(Sort.Direction.DESC, "followUser"))));
        return page.getContent();
    }
}