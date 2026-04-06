package com.dreamflow.api.auth.controller;

import com.dreamflow.api.auth.dto.LoginResponse;
import com.dreamflow.api.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthService authService;

    @Test
    void shouldLoginSuccessfully() throws Exception{
        String requestJson = """ 
                        {"email":"nikhil@gmail.com","password":"password"}
                """;
        when (authService.login(any()))
                .thenReturn(new LoginResponse("mocked_jwt"));

        mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .value("mocked_jwt"));
    }
}
