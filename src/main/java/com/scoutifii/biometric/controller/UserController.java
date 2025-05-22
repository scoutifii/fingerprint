package com.scoutifii.biometric.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scoutifii.biometric.infrastructure.fingerprint.service.JwtTokenGenerator;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private JwtTokenGenerator jwtTokenGenerator;
	
	@GetMapping("/jwt-token")
	public String getJwtToken(@RequestParam String username) {
		return jwtTokenGenerator.generateToken(username);
	}
	
	@GetMapping("/oauth2-login")
	public String oauth2Login() {
		return "redirect:/oauth2/authorization/client-id";
	}
	

}
