package com.travel.service;

import com.travel.dto.PageResponse;
import com.travel.dto.PlanProfileRespone;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.model.ErrorMessage;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
public class MemberService {

    @Autowired
    private UserRepository userRepository;

    public User getMyJoin(Long id, Pageable pageable) {
        User user = userRepository.findById(id).orElseThrow(() -> new ArrayIndexOutOfBoundsException(Constants.USER_NOT_EXIST));
        return user;

    }


}
