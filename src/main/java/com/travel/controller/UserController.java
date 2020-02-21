package com.travel.controller;

import com.travel.config.JwtTokenProvider;
import com.travel.dto.UserForm;
import com.travel.entity.ErrorMessage;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired private UserRepository userRepository;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody UserForm requestBody) {
        try {
            String username = requestBody.getEmail();
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, requestBody.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/register")
    public ResponseEntity registerUser(
            @Valid @RequestBody UserForm userForm
    ) {
        List<ErrorMessage> listError = validate(userForm);
        if (listError.isEmpty()) {
            User user = new User();
            user.setFullName(userForm.getFullName());
            user.setdOB(userForm.getdOfB());
            user.setGender(userForm.isGender());
            user.setUsername(userForm.getUsername());
            user.setEmail(userForm.getEmail());
            user.setPassword(passwordEncoder.encode(userForm.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok().body("OK");
        } else {
            return ResponseEntity.badRequest().body(listError);
        }
    }

    @GetMapping(value = "/userprofile")
    public ResponseEntity<Object> getUserProfileFromToken() {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        String email = au.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            UserForm userForm = new UserForm();
            userForm.setUsername(user.getUsername());
            userForm.setEmail(user.getEmail());
            userForm.setdOfB(user.getdOB());
            userForm.setGender(user.isGender());
            userForm.setFullName(user.getFullName());
            return new ResponseEntity<>(userForm, HttpStatus.OK);
        }
        return new ResponseEntity<>("\"Fail to load user profile\"", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public List<ErrorMessage> validate(UserForm userForm) {
        List<ErrorMessage> list = new ArrayList<>();
        String username = userForm.getUsername();
        String email = userForm.getEmail();
        if (userRepository.findByUsername(username).orElse(null) != null) {
            list.add(new ErrorMessage(400, "Username exist"));
        }
        if (userRepository.findByEmail(email).orElse(null) != null  ) {
            list.add(new ErrorMessage(400, "Email exist"));
        }
        return list;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

}
