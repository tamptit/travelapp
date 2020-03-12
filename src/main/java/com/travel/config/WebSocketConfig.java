package com.travel.config;

import com.travel.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(defaultHandshakeHandler())
                .addInterceptors(getInterceptot())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "queue","user");
        registry.setUserDestinationPrefix("user");
    }

    private DefaultHandshakeHandler defaultHandshakeHandler(){
        return new DefaultHandshakeHandler(){
            @Override
            public Principal determineUser(ServerHttpRequest request, WebSocketHandler handler, Map<String, Object> attributes) {
                String token = (String) attributes.get(Constants.JWT_HEADER);
                try {
                    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
                        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return authentication;
                    }
                } catch (Exception ex) {
                    LOGGER.error("Could not set user authentication in security context", ex);
                }
                return null;
            }
        };
    }

    private HandshakeInterceptor getInterceptot() {
        return new HandshakeInterceptor(){
            @Override
            public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
                if (serverHttpRequest instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
                    String token = servletRequest.getServletRequest().getHeader(Constants.JWT_HEADER);
                    String user = servletRequest.getServletRequest().getHeader("user");
                    HttpSession session = servletRequest.getServletRequest().getSession();
                    map.put("sessionId", session.getId());
                    map.put(Constants.JWT_HEADER, token);
                    map.put("user", user);
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

            }

        };
    }
}
