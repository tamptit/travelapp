package com.travel.service;

import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;


}
