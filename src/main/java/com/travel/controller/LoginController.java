package com.travel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travel.config.JwtTokenProvider;
import com.travel.dto.UserForm;
import com.travel.entity.User;
import com.travel.repository.PasswordTokenRepository;
import com.travel.repository.UserRepository;
import com.travel.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.travel.utils.Constants.USERNAME_PASSWORD_WRONG;


@RestController
@RequestMapping(value = "/api/")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(
            @RequestBody UserForm userForm
    ) {
        try {
            String username = userForm.getUsername();
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, userForm.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return new ResponseEntity<>(token, HttpStatus.OK);

        } catch (Exception e) {
            new ResponseEntity<>(USERNAME_PASSWORD_WRONG, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/user")
    public ResponseEntity<?> user(@AuthenticationPrincipal User principal) {
        try {
            String token = jwtTokenProvider.generateTokenFromGoogle(principal.getId(),principal.getUsername());
            return ResponseEntity.ok().body(token);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body("");
        }
    }




}
