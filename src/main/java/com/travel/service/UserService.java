package com.travel.service;

import com.travel.entity.User;

import java.util.Optional;

public interface UserService {

    public Optional<User> findUserByEmail(String email);
    public void save(User user);


}
