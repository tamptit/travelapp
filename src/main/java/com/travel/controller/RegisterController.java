package com.travel.controller;

import com.travel.dto.UserForm;
import com.travel.model.AuthProvider;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import com.travel.validator.MapValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.travel.utils.Constants.EMAIL_ALREADY_EXISTS;
import static com.travel.utils.Constants.USERNAME_ALREADY_EXISTS;

@RestController
@RequestMapping(value = "/api/register")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MapValidationError mapValidationErrorService;

    @PutMapping
    public ResponseEntity registerUser(
            @Valid @RequestBody UserForm userForm,
            BindingResult result
    ) {
        ResponseEntity<?> errorMap = mapValidationErrorService. mapValidation(result);
        if (errorMap != null) return errorMap;
        Map<String,String> errors = validate(userForm);
        if (errors.isEmpty()) {
            User user = new User();
            user.setFullName(userForm.getFullName());
            user.setdOfB(userForm.getdOfB());
            user.setGender(userForm.isGender());
            user.setUsername(userForm.getUsername());
            user.setEmail(userForm.getEmail());
            user.setPassword(passwordEncoder.encode(userForm.getPassword()));
            user.setJoinDate(new Date());
            user.setProvider(AuthProvider.local);
            userRepository.save(user);
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        } else {
            return ResponseEntity.badRequest().body(errors);
        }
    }

    public  Map<String,String> validate(UserForm userForm) {
        Map<String,String> errors = new HashMap<>();
        String username = userForm.getUsername();
        String email = userForm.getEmail();
        if (userRepository.findByUsername(username).orElse(null) != null) {
            errors.put("username",USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.findByEmail(email).orElse(null) != null) {
            errors.put("email",EMAIL_ALREADY_EXISTS);
        }
        return errors;
    }

}
