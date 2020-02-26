package com.travel.service;

import com.travel.entity.Plan;
import com.travel.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    public Plan saveOrUpdate (Plan plan)
    {
        // logic
        return planRepository.save(plan);
    }

}
