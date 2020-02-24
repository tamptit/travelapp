package com.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.config.JwtTokenProvider;
import com.travel.dto.TokenDto;
import com.travel.dto.UserForm;
import com.travel.entity.ErrorMessage;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import com.travel.utils.CookieUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static final String jwtTokenCookieName = "JWT-TOKEN";

    @Autowired private UserRepository userRepository;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestBody UserForm userForm

    ) {
        try {
            String username = userForm.getUsername();
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, userForm.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            CookieUtil.create(response, jwtTokenCookieName, token, false, -1, request.getServerName());
            return new ResponseEntity<>(token, HttpStatus.OK);

        } catch (Exception e) {
            new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletResponse httpServletResponse,HttpServletRequest request){
        CookieUtil.clear(httpServletResponse, jwtTokenCookieName,request.getServerName());
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
        return new ResponseEntity<>("\"Fail to load user profile\"", HttpStatus.INTERNAL_SERVER_ERROR);
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
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put("message", error.getDefaultMessage()));
        return errors;
    }

}
