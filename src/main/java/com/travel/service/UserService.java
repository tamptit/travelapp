package com.travel.service;

import com.travel.entity.User;

import java.util.Optional;

public interface UserService {

    public Optional<User> findUserByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    public void save(User user);

    Optional<User> findUserByResetToken(String token);
}
