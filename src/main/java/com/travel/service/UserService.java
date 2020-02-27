package com.travel.service;

import com.travel.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByEmail(String email);
    void save(User user);


}
