package com.travel.repository;

import com.travel.entity.Plan;
import com.travel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    //List<Plan> findByUser(User user);

}
