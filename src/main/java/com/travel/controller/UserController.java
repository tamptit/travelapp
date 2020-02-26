package com.travel.controller;

import com.travel.config.JwtConfiguration;
import com.travel.config.JwtTokenProvider;
import com.travel.dto.LoginForm;
import com.travel.dto.TokenDto;
import com.travel.dto.UserForm;
import com.travel.dto.ErrorMessage;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import com.travel.utils.Constaints;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/auth")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired private UserRepository userRepository;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity login(
            @Valid @RequestBody LoginForm loginForm
    ) {
        try {
            String username = loginForm.getUsername();
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, loginForm.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return new ResponseEntity<>(token, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logout(){
        return ResponseEntity.ok().body(Constaints.SUCCESS_MESSAGE);
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
            return ResponseEntity.ok().body(Constaints.SUCCESS_MESSAGE);
        } else {
            return ResponseEntity.badRequest().body(listError);
        }
    }

    @PostMapping(value = "/validate")
    public ResponseEntity validate(
            @RequestBody TokenDto tokenDto
    ) {
        if(jwtTokenProvider.validateToken(tokenDto.getToken())){
            return ResponseEntity.ok().body(true);
        }else{
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping(value = "/user-profile")
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
        return new ResponseEntity<>(Constaints.FAIL_TO_LOAD_USERDETAILS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public List<ErrorMessage> validate(UserForm userForm) {
        List<ErrorMessage> list = new ArrayList<>();
        String username = userForm.getUsername();
        String email = userForm.getEmail();
        if (userRepository.findByUsername(username).orElse(null) != null) {
            list.add(new ErrorMessage("Username exist"));
        }
        if (userRepository.findByEmail(email).orElse(null) != null  ) {
            list.add(new ErrorMessage("Email exist"));
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
