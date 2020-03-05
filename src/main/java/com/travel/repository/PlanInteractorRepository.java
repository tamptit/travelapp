package com.travel.repository;

import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanInteractorRepository extends JpaRepository<PlanInteractor, Long> {

    List<PlanInteractor> findByUserId(Long id);
}
