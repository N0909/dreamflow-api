package com.dreamflow.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secretkey}")
    private String key;
    @Value("${jwt.expiry}")
    private int expiry;

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> claims, String subject){
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiry))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token){
        final Claims claims = extractAllClaims(token);
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public String extractUsername(String token){
        final Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token, String subject){
        final Claims claims = extractAllClaims(token);
        return claims.getSubject().equals(subject) && !isTokenExpired(token);
    }
}
