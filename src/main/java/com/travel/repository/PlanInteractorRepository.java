package com.travel.repository;

import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlanInteractorRepository extends JpaRepository<PlanInteractor, Long> {

    PlanInteractor findByPlanAndUser( Plan plan , User user);

    //@Transactional
    //@Query(value = "delete from PlanInteractor pI where pI.user.id =  :idUser and pI.plan.id = :idPlan")
    //Void deletePlanAndUser( @Param("idPlan") Long idPlan , @Param("idUser") Long idUser);
    //Void deletePlanInteractorByPlanAndUser( Plan plan , User user );
}
