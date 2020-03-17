package com.travel.controller;

import com.travel.config.CustomUserDetailsService;
import com.travel.config.JwtTokenProvider;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.model.Message;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.service.PlanInteractorService;
import com.travel.utils.Constants;
import org.apache.http.auth.BasicUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanInteractorService planInteractorService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        String username = headerAccessor.getUser().getName();
        messagingTemplate.convertAndSendToUser(username, "/queue/reply", message);
    }

    @MessageMapping("/notification.followPlan")
    public void followPlan(@Payload Message message, Principal principal) {
        long idPlan = Long.parseLong(message.getSender());
        String userEmail = principal.getName();
        Long userId = userRepository.findByEmail(userEmail).map(u -> u.getId()).orElseThrow(() -> new NullPointerException(Constants.ERROR));
        Plan plan = planInteractorService.updateFollowPlanInteractor(idPlan, userId, true);
        String userReceived = plan.getUser().getEmail();
        messagingTemplate.convertAndSendToUser(userReceived, "/queue/reply", message);
    }


//    @MessageMapping("/follow")
//    public Message followPlan(@Payload Message message,
//                               SimpMessageHeaderAccessor headerAccessor) {
////        Add username in websocket session
////        long planId = Long.parseLong(message.getSender());
////        long userId = Long.parseLong(headerAccessor.getUser().getName());
//        try {
////            PlanInteractor planInteractor = planInteractorService.followPlan(planId, userId);
//            message.setType(Message.MessageType.SUCCESS);
////            User user = planInteractor.getPlan().getUser();
////            messagingTemplate.convertAndSendToUser(user.getId().toString(), "/queue/reply", message);
//            messagingTemplate.convertAndSendToUser("13", "/queue/reply", message);
//        } catch (IllegalArgumentException e) {
//            message.setType(Message.MessageType.FAIL);
//        }
//        messagingTemplate.convertAndSendToUser("13", "/queue/reply", message);
//        return message;
//    }
}

