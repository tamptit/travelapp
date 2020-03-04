package com.travel.repository;

import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanInteractorRepository extends JpaRepository<Plan, Long> {

    List<PlanInteractor> findByUserId(Long id);

//    SELECT pi.cou , pl.*
//    FROM (select count(DISTINCT pi.user_id) as cou, pi.plan_id
//    from public.plan_interactor pi group by plan_id order by cou
//) pi
//    JOIN public.plan pl ON pl.id = pi.plan_id



}
