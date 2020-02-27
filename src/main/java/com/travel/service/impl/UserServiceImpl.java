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
    UserRepository userRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {

    }

}