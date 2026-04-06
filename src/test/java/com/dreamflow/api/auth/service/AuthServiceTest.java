package com.dreamflow.api.auth.service;

import com.dreamflow.api.auth.dto.LoginRequest;
import com.dreamflow.api.auth.dto.LoginResponse;
import com.dreamflow.api.auth.dto.SignupRequest;
import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;
    private SignupRequest request;
    private LoginRequest loginRequest;

    @BeforeEach
    void setup(){
        request = new SignupRequest("test", "test@email.com", "test");
    }

    @Test
    void shouldSignupSuccessfully(){
        // Mock Behaviour
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded_password");

        User savedUser = new User();
        savedUser.setUserId(1);
        savedUser.setEmail(request.email());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(), eq(request.email()))).thenReturn("mocked_jwt_token");

        // call method
        LoginResponse response = authService.signUp(request);

        // assertions
        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.token());

        // verify interactions
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            authService.signUp(request);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldLoginSuccessfully(){
        // Mock Behaviour
        loginRequest = new LoginRequest("test@gmail.com", "test");

        CustomUserDetails userDetails = new CustomUserDetails(
                1,
                "test@gmail.com",
                "encoded_password"
        );

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtService.generateToken(any(), eq("test@gmail.com"))).thenReturn(
                "mocked_jwt"
        );

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mocked_jwt", response.token());

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(any(), eq("test@gmail.com"));
    }

    @Test
    void shouldThrowExceptionWhenPrincipalInvalid() {

        loginRequest = new LoginRequest("test@email.com", "test");

        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn("invalid_principal");
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        assertThrows(IllegalStateException.class, () -> {
            authService.login(loginRequest);
        });
    }


}
