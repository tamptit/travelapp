package com.travel.service;

import com.travel.config.UserPrincipal;
import com.travel.dto.PlanForm;
import com.travel.entity.Plan;
import com.travel.entity.User;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

}
