package com.travel.repository;

import com.travel.entity.PasswordResetToken;
import com.travel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    //Optional<User> findByToken(String token);

    PasswordResetToken findByUser(User user);

    PasswordResetToken findByToken(String token);

}
