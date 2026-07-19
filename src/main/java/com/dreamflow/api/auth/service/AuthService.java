package com.dreamflow.api.auth.service;

import com.dreamflow.api.auth.dto.*;
import com.dreamflow.api.auth.entity.Role;
import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.exception.exceptions.IllegalAuthException;
import com.dreamflow.api.exception.exceptions.IllegalTokenException;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.security.CustomeUserDetailsService;
import com.dreamflow.api.security.JwtService;
import com.dreamflow.api.util.service.email.EmailService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomeUserDetailsService userDetailsService;
    private final EmailService emailService;
    private static final String REFRESH = "refresh";
    private static final String ACCESS = "access";
    private Set<Role> allowedRoles = Set.of(Role.USER, Role.ARTIST);

    @Transactional
    public LoginResponse signUp(SignupRequest input){

        if (input.username()==null || input.email()==null || input.password()==null || input.role()==null){
            throw new IllegalAuthException("Invalid Request");
        }

        if (input.username().isBlank() || input.email().isBlank() || input.password().isBlank()){
            throw new IllegalAuthException("A field is blank or only contains spaces");
        }

        if (!input.email().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
            throw new IllegalAuthException("Invalid Email");
        }

        if (!allowedRoles.contains(input.role())) {
            throw new IllegalAuthException(
                    "Invalid role"
            );
        }

        if(userRepository.existsByEmail(input.email())){
            throw new IllegalAuthException("Email already in use");
        }

        User user = new User();
        user.setUsername(input.username());
        user.setEmail(input.email());
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(input.role());

        User createdUser = userRepository.save(user);

        Map<String, Object> claims = Map.of(
                "userId", createdUser.getUserId(),
                "type",ACCESS,
                "role", "ROLE_"+createdUser.getRole()
        );

        Map<String, Object> refreshClaims = Map.of(
                "userId", createdUser.getUserId(),
                "type", REFRESH
        );

        String accessToken = jwtService.generateToken(claims, createdUser.getEmail(), 30*60*1000);

        String refreshToken = jwtService.generateToken(refreshClaims, createdUser.getEmail(), 7*24*60*60*1000);

        String welcomeText = "Hello " + input.username() + ",\n\n" +
                "Welcome to DreamFlow Music Streaming Platform!\n\n" +
                "This is a test welcome email for our application. " +
                "If you received this message by mistake, please feel free to ignore it.\n\n" +
                "Enjoy the music!\n" +
                "- DreamFlow Team";

        String subject = "Welcome to Dreamflow.";

        emailService.sendMail(input.email(), subject, welcomeText);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse login(LoginRequest input) {

        if (input.email()==null || input.password()==null){
            throw new IllegalAuthException("A Field is null or Wrong Request Body");
        }

        if (input.email().isBlank() || input.password().isBlank()){
            throw new IllegalAuthException("A field is blank or only contains spaces");
        }

        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.email(),
                            input.password()
                    )
            );
        }catch(AuthenticationException ex){
            throw new IllegalAuthException("Bad Credentials");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails userDetails)){
            throw new IllegalAuthException("Invalid authentication principal");
        }

        Map<String, Object> claims = Map.of(
                "userId", userDetails.getUserId(),
                "role", userDetails.getAuthorities()
                            .stream()
                            .findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER"),
                "type",ACCESS
        );

        String accessToken = jwtService.generateToken(claims, userDetails.getUsername(), 30*60*1000);

        Map<String, Object> refreshClaims = Map.of(
                "userId", userDetails.getUserId(),
                "type", REFRESH
        );

        String refreshToken = jwtService.generateToken(refreshClaims, userDetails.getUsername(), 7*24*60*60*1000);

        return new LoginResponse(accessToken, refreshToken);
    }

    public RefreshResponse generateAccessToken(String refreshToken){

        if (refreshToken==null){
            throw new IllegalTokenException("Token is Invalid");
        }

        if (refreshToken.isBlank()){
            throw new IllegalTokenException("Token is Invalid");
        }

        String type;
        try{
             type = jwtService.extractClaim(refreshToken, claims -> claims.get("type", String.class));
        }catch(SignatureException ex){
            throw new IllegalTokenException("Token is Invalid");
        }

        if (!type.equals(REFRESH)){
            throw new IllegalTokenException("Not a Refresh Token");
        }

        if (jwtService.isTokenExpired(refreshToken)){
            throw new IllegalTokenException("Provided token is expired");
        }

        String email = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(refreshToken,userDetails)){
            throw new IllegalTokenException("Token is Invalid");
        }

        int userId = jwtService.extractClaim(refreshToken, claims -> claims.get("userId", Integer.class));

        Map<String, Object> claims = Map.of(
                "userId", userId,
                "role",userDetails.getAuthorities()
                        .stream()
                        .findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER"),
                "type", ACCESS
        );

        String accessToken = jwtService.generateToken(claims, email, 30*60*1000);

        return new RefreshResponse(accessToken);
    }
}
