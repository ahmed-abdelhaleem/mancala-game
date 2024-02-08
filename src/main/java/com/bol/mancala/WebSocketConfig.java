package com.bol.mancala;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Bean
	public HandshakeInterceptor webSocketHandler() {
		return new SockJSHandShakeHandler();
	}

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    
    	config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/game");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	registry.addEndpoint("/play").addInterceptors(webSocketHandler()).withSockJS();
    }
    
}