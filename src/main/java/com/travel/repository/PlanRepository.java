package com.travel.repository;

import com.travel.entity.Plan;
import com.travel.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByUser(User user);

    List<Plan> findAllByOrderByCreatedDayDesc();

    @Query(value = "SELECT pl.*\n" +
            "FROM (select  pi.plan_id\n" +
            "\t  from public.plan_interactor pi group by pi.plan_id order by count(DISTINCT pi.user_id) desc\n" +
            ") pi JOIN public.plan pl ON pl.id = pi.plan_id", nativeQuery = true)
    List<Plan> findListHotPlan();

}
