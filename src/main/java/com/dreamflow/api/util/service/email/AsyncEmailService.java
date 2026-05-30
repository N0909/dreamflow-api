package com.dreamflow.api.util.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Deprecated
public class AsyncEmailService {

    private final ExecutorService executorService;
    private final EmailService emailService;


    public CompletableFuture<Void> sendWelcomeEmail(String email, String username, String body){
        return CompletableFuture.runAsync(()->{
            emailService.sendMail(
                    email,
                    username,
                    body
            );
        },executorService);
    }

}
