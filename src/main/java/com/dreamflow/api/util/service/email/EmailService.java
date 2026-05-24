package com.dreamflow.api.util.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Async("emailExecutor")
    public void sendWelcomeMail(String toEmail, String username){
        SimpleMailMessage message = new SimpleMailMessage();

        String text = "Hello " + username + ",\n\n" +
                "Welcome to DreamFlow Music Streaming Platform!\n\n" +
                "This is a test welcome email for our application. " +
                "If you received this message by mistake, please feel free to ignore it.\n\n" +
                "Enjoy the music!\n" +
                "- DreamFlow Team";

        message.setTo(toEmail);
        message.setSubject("Welcome to Dreamflow Backend Api");
        message.setText(text);

        javaMailSender.send(message);
    }
}
