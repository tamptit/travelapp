//package com.travel.config;
//
//
//import com.travel.utils.Constants;
//import org.apache.http.auth.BasicUserPrincipal;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//import java.security.Principal;
//import java.util.List;
//
//@Configuration
//@EnableWebSocketMessageBroker
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
//public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//
//    }
//}