package com.travel.service;

import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanInteractorRepository planInteractorRepository;

    //                     public void findUserById()

    public PlanInteractor checkInteracPlan(Long id) {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user;
        Plan plan;
        user = userRepository.findByEmail(au.getName()).get();  // sao cho nay lai findbyEmail?? & au.getName()
        plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan, user).orElse(null);
        if (interactor != null) {
            return interactor;
        }
        return null;
    }


}
