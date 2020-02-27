package com.travel.controller;

import com.travel.config.JwtTokenProvider;
import com.travel.dto.LoginForm;
import com.travel.dto.TokenDto;
import com.travel.dto.UserForm;
import com.travel.dto.ErrorMessage;
import com.travel.entity.PasswordResetToken;
import com.travel.entity.User;
import com.travel.repository.PasswordTokenRepository;
import com.travel.repository.UserRepository;
import com.travel.service.MailService;
import com.travel.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/api/auth")
public class UserController {

    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    static final int EXPIRATION = 60;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Value("${url.resetPassword}")
    private String urlFrontEnd;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailService emailService;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

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
            return ResponseEntity.badRequest().body(new ErrorMessage(Constants.USERNAME_PASSWORD_WRONG));
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logout(){
        return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
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
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
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
        return new ResponseEntity<>(Constants.FAIL_TO_LOAD_USERDETAILS, HttpStatus.INTERNAL_SERVER_ERROR);
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

    // Process form submission from forgotPassword page
    @Value("${url}")
    private String link;

    @PostMapping(value = "/forgot", produces = "application/json")
    public String processForgotPasswordForm(@RequestParam("mail") String mail) {

        Optional<User> optional = userRepository.findByEmail(mail);

        if (!optional.isPresent()) {
            return ("We didn't find an account for that e-mail address");
        } else {
            // Generate random 36-character string token for reset password
            // get token by User.. setToken .. save(resettoken)
            User user = optional.get();
            Calendar date = Calendar.getInstance();
            date.add(Calendar.HOUR, 1);
            Date date_expired = date.getTime();
            PasswordResetToken passwordResetToken = passwordTokenRepository.findByUser(user);
            if (passwordResetToken == null) { // table chua co row cua user nay
                passwordResetToken = new PasswordResetToken();

                passwordResetToken.setUser(user);
                passwordResetToken.setToken(UUID.randomUUID().toString());
                passwordResetToken.setExpiryDate(date_expired);

            } else {
                passwordResetToken.setToken(UUID.randomUUID().toString());
                passwordResetToken.setExpiryDate(date_expired);
            }
            passwordTokenRepository.save(passwordResetToken);   // save passwordToken

            //String appUrl = request.getScheme() + "://" + request.getServerName();
            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("travelapp.travel2020@gmail.com");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");

            String text = link + "/api/auth?token=" + passwordResetToken.getToken();
            passwordResetEmail.setText("To reset your password, click the link below:\n" + text);
            emailService.sendEmail(passwordResetEmail);
            // Add success message to view

            return ("A password reset link has been sent to " + user.getEmail());
        }

    }

    // Display form to reset password
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String displayResetPasswordPage(@RequestParam("token") String token, @RequestParam("token") String newPassword) {
        //Optional<User> user = passwordTokenRepository.findByToken(token);
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
        Optional<User> user = Optional.ofNullable(passwordResetToken.getUser());

        if (user.isPresent()) { // Token found in DB
            return "display form reset";
        } else { // Token not found in DB
            return "Link is an invalid password reset link.";
        }
    }

    // Process reset password form
    @Transactional
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String setNewPassword(@RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

        // Find the user associated with the reset token
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(requestParams.get("token"));
        Optional<User> user = Optional.ofNullable(passwordResetToken.getUser());
        //Optional<User> user = passwordTokenRepository.findByToken(requestParams.get("token"));
        Date date = new Date();
        if (date.before(passwordResetToken.getExpiryDate())) {
            User resetUser = user.get();
            //PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(requestParams.get("token"));
            // Set new password
            resetUser.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
            // Set the reset token to null so it cannot be used again
            //resetUser.setResetToken(null);
            // Save user
            userRepository.save(resetUser);
            passwordResetToken.setToken(null);
            passwordResetToken.setExpiryDate(null);
            passwordTokenRepository.save(passwordResetToken);
            return "redirect login";
        } else {
            return ("Oops!  This is an invalid password reset link."); // link khong con ton tai
        }
    }

}
