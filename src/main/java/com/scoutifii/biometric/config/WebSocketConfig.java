package com.scoutifii.biometric.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.scoutifii.biometric.interceptors.HttpSessionIdHandshakeInterceptor;

@SpringBootApplication(exclude = {GsonAutoConfiguration.class})  //To indicate that it is a Spring configuration class
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.scoutifii.biometric.infrastructure.fingerprint.repository"})
@EntityScan(basePackages = {"com.scoutifii.biometric.infrastructure.fingerprint"})
@ComponentScan(basePackages = {"com.scoutifii.biometric.*"})
@Configuration
@EnableWebSocketMessageBroker  //Enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	@Value("${allowed.origins}")
	private String allowedOrigins;
	  
	public void configureMessageBroker(MessageBrokerRegistry config) {
	    config.enableSimpleBroker(new String[] { "/topic", "/queue" });
	    config.setApplicationDestinationPrefixes(new String[] { "/digital-persona" });
	  }
	  
	public void registerStompEndpoints(StompEndpointRegistry registry) {
	   HttpSessionIdHandshakeInterceptor interceptor = new HttpSessionIdHandshakeInterceptor();
	    for (String allowedOrigin : this.allowedOrigins.split(", ")) {
	      registry.addEndpoint(new String[] { "init", "/enroll", "/search", "/delete" })
	      .setAllowedOrigins(new String[] { allowedOrigin })
	      .setAllowedOriginPatterns(new String[] { "*" })
	      .addInterceptors(new HandshakeInterceptor[] { 
	    		  (HandshakeInterceptor)interceptor })
	      .withSockJS();
	    } 
	  }
}
