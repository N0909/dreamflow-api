package com.dreamflow.api.util.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class AsyncEmailService {

    private final ExecutorService executorService;
    private final EmailService emailService;

    public CompletableFuture<Void> sendWelcomeEmail(String email, String username){
        return CompletableFuture.runAsync(()->{
            emailService.sendWelcomeMail(
                    email,
                    username
            );
        },executorService);
    }

}
