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
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    private CustomUserDetailsService customUserDetailsService; private SimpMessagingTemplate template;


//    @RequestMapping(path="/follow", method=POST)
//    public ResponseEntity follow(@Payload Message message) {
//        String text = "[" + new Date() + "]:" + "Good Afternoon";
//        long idPlan = Long.parseLong(message.getSender());
//
//        //String userEmail = headerAccessor.getUser().getName();
//       // Long userId = userRepository.findByEmail(userEmail).map(u -> u.getId()).orElseThrow(() -> new NullPointerException(Constants.ERROR));
//
//        //Plan plan = planInteractorService.updateFollowPlanInteractor(idPlan, userId, true).getPlan();
//        //String userReceived = plan.getUser().getEmail();
//       // String userReceived = headerAccessor.getUser().getName();
//       // messagingTemplate.convertAndSendToUser(userReceived, "/topic/follow", message );
//        return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
//    }


    @RequestMapping(path="/greetings", method=POST)
    public void greet(String greeting) {
        String text = "Good Afternoon" ;
        this.messagingTemplate.convertAndSendToUser("tamptit2016@gmail.com", "/topic/greetings", text);
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        String username = headerAccessor.getUser().getName();
        messagingTemplate.convertAndSendToUser(username, "/queue/reply", message);
    }
   // @MessageMapping("/notification.followPlan")
//    public void followPlan(@Payload Message message, Principal principal) {
//        long idPlan = Long.parseLong(message.getSender());
//        String userEmail = principal.getName();
//        Long userId = userRepository.findByEmail(userEmail).map(u -> u.getId()).orElseThrow(() -> new NullPointerException(Constants.ERROR));
//        Plan plan = planInteractorService.updateFollowPlanInteractor(idPlan, userId, true).getPlan();
//        String userReceived = plan.getUser().getEmail();
//        message.setContent(userId + "follow plan id: " + idPlan );
//        messagingTemplate.convertAndSendToUser(userReceived, "/queue/reply", message);
//    }
//




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

