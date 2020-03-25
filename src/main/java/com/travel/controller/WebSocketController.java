package com.travel.controller;

import com.travel.config.CustomUserDetailsService;
import com.travel.entity.Plan;
import com.travel.model.Message;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.service.PlanInteractorService;
import com.travel.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class WebSocketController {

    @Autowired(required=true)
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanInteractorService planInteractorService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @RequestMapping(value="/greetings", method=POST)
    public void greet(String greeting) {
        String text = "Good Afternoon" ;
        messagingTemplate.convertAndSendToUser("tamptit2016@gmail.com", "/topic/greetings", text);
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        String username = headerAccessor.getUser().getName();
        messagingTemplate.convertAndSendToUser( username, "/queue/reply", message);
    }
//    @MessageMapping("/notification")
//    public void followPlan(@Payload Message message, Principal principal) {
//        long idPlan = Long.parseLong(message.getSender());
//        String userEmail = principal.getName();
//        Long userId = userRepository.findByEmail(userEmail).map(u -> u.getId()).orElseThrow(() -> new NullPointerException(Constants.ERROR));
//        Plan plan = planInteractorService.updateFollowPlanInteractor(idPlan, userId, true).getPlan();
//        String userReceived = plan.getUser().getEmail();
//        messagingTemplate.convertAndSendToUser(userReceived, "/queue/reply", message);
//    }

    @PostMapping("/api/followPlan")
    public void followPlan(Message message, Principal principal) {
        String userEmail = principal.getName();
        long idPlan = Long.parseLong(message.getContent());
        Long userId = userRepository.findByEmail(userEmail).map(u -> u.getId()).orElseThrow(() -> new NullPointerException(Constants.ERROR));
        Plan plan = planInteractorService.updateFollowPlanInteractor(16, userId, true).getPlan();
        String userReceived = plan.getUser().getEmail();
        messagingTemplate.convertAndSendToUser(userReceived, "/queue/reply", "hello");
//         messagingTemplate.convertAndSend("/queue/reply", userReceived);
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

