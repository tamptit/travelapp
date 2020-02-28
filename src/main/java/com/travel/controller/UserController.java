package com.travel.controller;

import com.travel.config.JwtTokenProvider;
import com.travel.dto.TokenDto;
import com.travel.dto.UserForm;
import com.travel.model.ErrorMessage;
import com.travel.entity.PasswordResetToken;
import com.travel.entity.User;
import com.travel.repository.PasswordTokenRepository;
import com.travel.repository.UserRepository;
import com.travel.service.MailService;
import com.travel.utils.CookieUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/api/auth")
public class UserController {

    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    static final int EXPIRATION = 60;

    static final int HOUR_EXPIRE = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String jwtTokenCookieName = "JWT-TOKEN";

    @Value("${url}")
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
            return new ResponseEntity<>(token, HttpStatus.OK);

        } catch (Exception e) {
            new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletResponse httpServletResponse, HttpServletRequest request) {
        CookieUtil.clear(httpServletResponse, jwtTokenCookieName, request.getServerName());
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
        if (jwtTokenProvider.validateToken(tokenDto.getToken())) {
            return ResponseEntity.ok().body(true);
        } else {
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
                return ResponseEntity.ok().body("ok");
            }
        }


}
