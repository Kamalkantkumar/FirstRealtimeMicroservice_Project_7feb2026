package com.example.demo.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	private static final String SECRET="my-secret-key-my-secret-key-my-secret-key-my-secret-key";
	
	public String generateToken(String username,String role) {
		return Jwts.builder()
		.subject(username)
		.claim("role",role)
		.issuedAt(new Date())
		.expiration(new Date(System.currentTimeMillis()+1000*60*60))
		.signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
		.compact();
	}
}
