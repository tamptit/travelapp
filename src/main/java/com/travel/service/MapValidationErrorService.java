package com.travel.service;

import com.travel.entity.Plan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
@Service
public class MapValidationErrorService {
    public ResponseEntity<?> mapValidationService( BindingResult result)  {
        if (result.hasErrors())
        {
            Map<String,String> errorMap = new HashMap<>();

            for (FieldError error: result.getFieldErrors())
            {
                errorMap.put(error.getField(),error.getDefaultMessage());
            }

            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
