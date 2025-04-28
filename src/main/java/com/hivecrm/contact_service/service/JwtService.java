package com.hivecrm.contact_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;

@Service
public class JwtService {

    private final PublicKey secretKey;

    public JwtService(PublicKey secretKey) {
        this.secretKey = secretKey;
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // Will throw if invalid
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public User getUserDetails(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);
        return new User(claims.getSubject(), "",
                roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

