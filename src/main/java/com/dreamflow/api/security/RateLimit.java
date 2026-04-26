package com.dreamflow.api.security;
import com.dreamflow.api.util.GeneralResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimit extends OncePerRequestFilter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${rate_limit.threshold}")
    private int threshold;
    @Value("${rate_limit.time_limit}")
    private int timelimit;
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String ip = req.getRemoteAddr();
        String key = "rate_limit::"+ip;

        Long count = stringRedisTemplate.opsForValue().increment(key);

        if (count==1){
            stringRedisTemplate.expire(key, timelimit, TimeUnit.SECONDS);
        }

        if (count <= threshold){
            filterChain.doFilter(req, res);
            return;
        }
        Long expiry = stringRedisTemplate.getExpire(key);
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(
                        new GeneralResponse("error","too many request wait "+expiry+" seconds")
                );

        res.setStatus(429);
        res.setContentType("application/json");
        res.getWriter().write(jsonString);
    }
}
