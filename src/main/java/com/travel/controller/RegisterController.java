package com.travel.controller;

import com.travel.config.JwtTokenProvider;
import com.travel.dto.TokenDto;
import com.travel.dto.UserForm;
import com.travel.model.ErrorMessage;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/api/register")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PutMapping
    public ResponseEntity registerUser(
            @Valid @RequestBody UserForm userForm
    ) {
        List<ErrorMessage> listError = validate(userForm);
        if (listError.isEmpty()) {
            User user = new User();
            user.setFullName(userForm.getFullName());
            user.setdOfB(userForm.getdOfB());
            user.setGender(userForm.isGender());
            user.setUsername(userForm.getUsername());
            user.setEmail(userForm.getEmail());
            user.setPassword(passwordEncoder.encode(userForm.getPassword()));
            user.setJoinDate(new Date());
            userRepository.save(user);
            return ResponseEntity.ok().body("OK");
        } else {
            return ResponseEntity.badRequest().body(listError);
        }
    }

    public List<ErrorMessage> validate(UserForm userForm) {
        List<ErrorMessage> list = new ArrayList<>();
        String username = userForm.getUsername();
        String email = userForm.getEmail();

        if (userRepository.findByUsername(username).orElse(null) != null) {
            list.add(new ErrorMessage("This user already exists"));
        }
        if (userRepository.findByEmail(email).orElse(null) != null) {
            list.add(new ErrorMessage("This email already exists"));
        }
        return list;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        List<ErrorMessage> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(new ErrorMessage(error.getDefaultMessage())));

        return errors;
    }

}
