package com.dreamflow.api.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtServiceTest {
    @Autowired
    private JwtService jwtService;
    private String token;
    private Map<String, Object> claims;

    @BeforeEach
    void setup(){
        claims = new HashMap<>();
        claims.put("id", 10);
        claims.put("email", "test@email.com");

        token = jwtService.generateToken(claims, "test@");
    }

    @Test
    void shouldGenerateToken(){
        assertNotNull(token);
    }

    @Test
    void shouldBeValid(){
        boolean isValid = jwtService.isTokenValid(token, "test@");
        assertTrue(isValid);
    }

    @Test
    void shouldNotBeValid(){
        boolean isValid = jwtService.isTokenValid(token, "test");
        assertFalse(isValid);
    }
}
