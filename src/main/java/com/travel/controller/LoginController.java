//package com.travel.controller;
//
//import com.travel.config.JwtTokenProvider;
//import com.travel.dto.UserForm;
//import com.travel.entity.User;
//import com.travel.model.ErrorMessage;
//import com.travel.repository.PasswordTokenRepository;
//import com.travel.repository.UserRepository;
//import com.travel.service.MailService;
//import com.travel.utils.CookieUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping(value = "/api/auth")
//public class LoginController {
//
//    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
//
//    static final int EXPIRATION = 60;
//
//    static final int HOUR_EXPIRE = 1;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
//
//    private static final String jwtTokenCookieName = "JWT-TOKEN";
//
//    @Value("${url}")
//    private String urlFrontEnd;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    private MailService emailService;
//
//    @Autowired
//    private PasswordTokenRepository passwordTokenRepository;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//
//
//    @PostMapping(value = "/login")
//    public ResponseEntity<Object> login(
//            HttpServletResponse response,
//            HttpServletRequest request,
//            @RequestBody UserForm userForm
//    ) {
//        try {
//            String username = userForm.getUsername();
//            Authentication authentication =
//                    authenticationManager.authenticate(
//                            new UsernamePasswordAuthenticationToken(username, userForm.getPassword()));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String token = jwtTokenProvider.generateToken(authentication);
//            return new ResponseEntity<>(token, HttpStatus.OK);
//
//        } catch (Exception e) {
//            new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//    @PostMapping(value = "/logout")
//    public void logout(HttpServletResponse httpServletResponse, HttpServletRequest request) {
//        CookieUtil.clear(httpServletResponse, jwtTokenCookieName, request.getServerName());
//    }
//
//
//}
