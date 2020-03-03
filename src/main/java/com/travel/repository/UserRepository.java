package com.travel.repository;

import com.travel.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query(value = "select u from User u where u.joinDate >= :joinDate")
    Page<User> findAllWithJoinDateAfter(@Param("joinDate")Date joinDate, Pageable pageable);

    User findByPrincipalId(String principalId);

    @Query("select us from User us where us.joinDate >= :joinDate order by us.joinDate")
    List<User> findAllWithJoinDateAfter(@Param("joinDate") Date joinDate);





}
