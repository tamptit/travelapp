package com.travel.service;

import com.travel.entity.Plan;
import com.travel.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    public Plan saveOrUpdate (Plan plan)
    {
        // logic

        Date currentDate = new Date();
        plan.setCreatedDay(currentDate);
        
        return planRepository.save(plan);
    }

}
