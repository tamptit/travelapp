package com.travel.controller;

import com.travel.entity.ErrorMessage;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import com.travel.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private ErrorMessage errorMessage;

//    @Autowired
//    UserValidator userValidator;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User findByIdUser(@PathVariable(name = "idUser") Long id) {

        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/registry")
    public ResponseEntity registerUser(@Valid @RequestBody User user) {
        List<ErrorMessage> listError = checkExist(user);
        if (listError.isEmpty()) {
            userRepository.save(user);
        } else {
            return ResponseEntity.badRequest().body(listError);
        }
        return ResponseEntity.ok().body(user);
    }

    public List<ErrorMessage> checkExist(User user) {
        List<ErrorMessage> list = new ArrayList<>();
        String username = user.getUsername();
        String email = user.getEmail();
        if (userRepository.findByUsername(username) != null) {
            list.add(new ErrorMessage(400, "Username exist"));
        }
        if (userRepository.findByEmail(email) != null) {
            list.add(new ErrorMessage(400, "Email exist"));
        }
        if (user.getPasswordConfirm().equals(user.getPassword())){
            list.add(new ErrorMessage(400, "Password is not matched."));
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
