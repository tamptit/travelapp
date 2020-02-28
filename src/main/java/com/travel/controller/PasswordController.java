package com.travel.controller;

import com.travel.entity.PasswordResetToken;
import com.travel.entity.User;
import com.travel.model.ErrorMessage;
import com.travel.repository.PasswordTokenRepository;
import com.travel.repository.UserRepository;
import com.travel.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "/api/")
public class PasswordController {

    static final int HOUR_EXPIRE = 1;

    @Value("${url}")
    private String urlFrontEnd;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetToken passwordResetToken;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private MailService emailService;

    @PostMapping(value = "/forgot", produces = "application/json")
    public ResponseEntity processForgotPasswordForm(@RequestBody Map<?,?> mail) {

        Optional<User> optional = userRepository.findByEmail(mail.get("mail").toString());

        if (!optional.isPresent()) {
            return ResponseEntity.ok().body("ok mail");
        } else {
            User user = optional.get();
            Calendar date = Calendar.getInstance();
            date.add(Calendar.HOUR, HOUR_EXPIRE);
            Date date_expired = date.getTime();
            PasswordResetToken passwordResetToken = passwordTokenRepository.findByUser(user);
            if (passwordResetToken == null) {                           // table chua co row cua user nay
                passwordResetToken = new PasswordResetToken();
                passwordResetToken.setUser(user);
                passwordResetToken.setToken(UUID.randomUUID().toString());
                passwordResetToken.setExpiryDate(date_expired);
            } else {
                passwordResetToken.setToken(UUID.randomUUID().toString());
                passwordResetToken.setExpiryDate(date_expired);
            }
            passwordTokenRepository.save(passwordResetToken);           // save passwordToken
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("travelapp.travel2020@gmail.com");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");

            String text = urlFrontEnd + "?token=" + passwordResetToken.getToken();
            passwordResetEmail.setText("To reset your password, click the link below:\n" + text);
            emailService.sendEmail(passwordResetEmail);

            return ResponseEntity.ok().body("ok");
        }
    }

    // Display form to reset password
    @RequestMapping(value = "/valid-reset-password", method = RequestMethod.GET)
    public ResponseEntity validateResetPasswordPage(@RequestParam Map<String, String> requestParams) {
        //String newPassword = requestParams.get("password").toString();
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(requestParams.get("token").toString());
        if (passwordResetToken == null) {                                            // check token in table
            return ResponseEntity.badRequest().body(new ErrorMessage("Invalid"));
        } else if (new Date().before(passwordResetToken.getExpiryDate())) {           // check date_expired
            return ResponseEntity.badRequest().body(new ErrorMessage("Invalid"));
        } else {
            return ResponseEntity.ok().body("tk");
        }
    }
    // Process reset password form
    @Transactional
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity setNewPassword (@RequestBody Map <String, Object > requestParams){

        // Find PasswordResetToken by token
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(requestParams.get("token").toString());
        Optional<User> user = Optional.ofNullable(passwordResetToken.getUser());

        if (new Date().before(passwordResetToken.getExpiryDate())) {
            User resetUser = user.get();
            if (requestParams.get("password").toString().isEmpty()) {                  // Set new password
                return ResponseEntity.ok().body(new ErrorMessage("Password should be minimum of 6 characters"));
            }
            resetUser.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password").toString()));

            userRepository.save(resetUser);         // Save user
            passwordResetToken.setToken(null);
            passwordTokenRepository.save(passwordResetToken);
            //passwordResetToken.save(passwordResetToken);
            return ResponseEntity.ok().body("logined");

        } else {
            return ResponseEntity.badRequest().body("InValid");
        }
    }

}
