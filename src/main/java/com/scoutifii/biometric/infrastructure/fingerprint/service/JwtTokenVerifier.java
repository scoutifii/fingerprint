package com.scoutifii.biometric.infrastructure.fingerprint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scoutifii.biometric.config.JwtConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtTokenVerifier {
	@Autowired
	private JwtConfig jwtConfig;
	
	public boolean verifyToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token);
			return true;
		} catch(SignatureException |
				MalformedJwtException |
				UnsupportedJwtException |
				IllegalArgumentException e) {
			return false;
			
		}
	}

}
