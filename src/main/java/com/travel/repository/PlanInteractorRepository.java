package com.travel.repository;

import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanInteractorRepository extends JpaRepository<Plan, Long> {

    List<PlanInteractor> findByUser(User user);

}
