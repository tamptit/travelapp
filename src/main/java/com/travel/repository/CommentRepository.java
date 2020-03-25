package com.travel.repository;

import com.travel.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query(value = "select * from Comment cm where cm.user.id = :idUser " +
//            "and cm.plan.idPlan = :idPlan")
//    List<Comment> findByUserAndPlan(@Param("idUser") long idUser, @Param("idPlan") long idPlan ) ;

    //List<Comment> findByUserAndPlan() ;

}
