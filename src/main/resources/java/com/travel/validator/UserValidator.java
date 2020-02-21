package com.travel.validator;

import com.travel.entity.ErrorMessage;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

@RestControllerAdvice
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    public ArrayList<ErrorMessage> handleAllException(User user) {
        ArrayList<ErrorMessage> list = new ArrayList<>();
        String username = user.getUsername();
        String email  = user.getEmail();
        if (userRepository.findByUsername(username) != null) {
            list.add(new ErrorMessage(400, "Username exist"));
        }
        if (userRepository.findByEmail(email) != null){
            list.add(new ErrorMessage(400, "Email exist"));
        }
        return list;
    }


}
