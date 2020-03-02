package com.travel.controller;


import com.travel.dto.UserPageResponse;
import com.travel.entity.User;
import com.travel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping(value = "api/member")
public class MemberController {

    private static final int NUM_DAY = -7;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/new-comer")
    public UserPageResponse getNewComers(@PathParam(value = "page") int page) {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByNewComer = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("joinDate").descending());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, NUM_DAY);
        Date agoDate = calendar.getTime();

        Page<User> memberPager =
                userRepository.findAllWithJoinDateAfter(agoDate,sortedByNewComer);
        UserPageResponse response = new UserPageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(memberPager.getTotalPages());
        response.setUsers(memberPager.getContent());
        return response;
    }


}
