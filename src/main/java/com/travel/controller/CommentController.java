package com.travel.controller;


import com.travel.dto.CommentDto;
import com.travel.dto.CommentForm;
import com.travel.dto.UserDto;
import com.travel.entity.Comment;
import com.travel.entity.Plan;
import com.travel.entity.User;
import com.travel.repository.CommentRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/comment")
public class CommentController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    CommentRepository commentRepository;

    @PutMapping(value = "/{id}")
    public ResponseEntity addComment(@PathVariable Long id, @RequestBody CommentForm commentForm) {

        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(au.getName()).get();  // sao cho nay lai findbyEmail?? & au.getName()
        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        Comment comment = new Comment(commentForm.getContent(), user, plan, new Date());
        commentRepository.save(comment);
        if (comment != null) {
            //return ResponseEntity.ok().body(Constants.MESSAGE);
            return ResponseEntity.ok().body(new CommentDto(new UserDto(user), comment.getContent(), comment.getTime()));
        } else {
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        }
    }

    @GetMapping(value = "/{id}/comments")
    public ResponseEntity getListCommentByPlan(@PathVariable Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        List<CommentDto> commentDtos =
                commentRepository.findByPlan(new Plan(plan.getId())).stream()
                                 .map(Comment::convertCommentToDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(commentDtos);
    }


}
