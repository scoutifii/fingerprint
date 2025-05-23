package com.scoutifii.biometric.interceptors;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpSession;

public class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {
	  private static final Logger logger = LoggerFactory.getLogger(com.scoutifii.biometric.interceptors.HttpSessionIdHandshakeInterceptor.class);
	  
	  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
	    logger.info("Handshake interceptor called!");
	    if (request instanceof ServletServerHttpRequest) {
	      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
	      HttpSession session = (HttpSession) servletRequest.getServletRequest().getSession(false);
	      if (session != null)
	        attributes.put("sessionId", session.getId()); 
	    } 
	    return true;
	  }
	  
	  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {}
	}