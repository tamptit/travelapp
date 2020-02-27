//package com.travel.controller;
//
//import com.travel.entity.ErrorMessage;
//import com.travel.entity.PasswordResetToken;
//import com.travel.entity.User;
//import com.travel.model.GenericResponse;
//import com.travel.repository.PasswordTokenRepository;
//import com.travel.repository.UserRepository;
//import com.travel.service.MailService;
//import com.travel.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//
//
//
//@RestController
//@RequestMapping(value = "/api/auth")
//public class PasswordResetTokenController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private MailService emailService;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
////    @Autowired
////    private Optional<PasswordResetToken> passwordResetToken;
//
//    @Autowired
//    private PasswordTokenRepository passwordTokenRepository;
//
//    @GetMapping(value = "/pass")
//    public String resetPassTest() {
//        return "ok";
//    }
//
//    // Display forgotPassword page
////    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
////    public String displayForgotPasswordPage() {
////        return ("Check mail");
////    }
//
//
//
//}
