package com.travel.service.impl;

import com.travel.entity.User;
import com.travel.repository.UserRepository;
import com.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByResetToken(String resetToken) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByResetToken(String resetToken) {
        return Optional.empty();
    }

    @Override
    public void save(User user) {

    }
}
