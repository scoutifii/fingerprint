package com.scoutifii.biometric.infrastructure.fingerprint.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scoutifii.biometric.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenGenerator {
	@Autowired
	private JwtConfig jwtConfig;
	
	public String generateToken(String username){
		Claims claims = Jwts.claims().setSubject(username);
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + jwtConfig.getExpiration());
		
		return Jwts.builder().setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
				.compact();
	}

}
