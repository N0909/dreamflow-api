package com.dreamflow.api.media.service;
import com.dreamflow.api.song.entity.UploadStatus;
import com.dreamflow.api.util.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailService emailService;
    private final SimpMessagingTemplate ws;

    public void notifySuccess(String jobId,String username ,String email, String songName){
        sendEmail(email, username, songName);
        pushWs(jobId, UploadStatus.COMPLETED, songName + " is ready to play");
    }

    public void notifyFailure(String jobId, String email, String username, String songName, String reason){
        sendEmail(email, username, songName, reason);
        pushWs(jobId, UploadStatus.FAILED, songName + " upload is failed", reason);
    }

    private void sendEmail(String email, String username, String songName) {
        String subject = "Your uploaded song is ready";

        String body = String.format(
                "Hello %s,\n\n" +
                        "Your uploaded song \"%s\" is now ready to play.\n\n" +
                        "Thank you for using our platform.",
                username,
                songName
        );

        emailService.sendMail(email, subject, body);
    }

    private void sendEmail(String email, String username, String songName, String reason) {
        String subject = "Song upload failed";

        String body = String.format(
                "Hello %s,\n\n" +
                        "Your uploaded song \"%s\" could not be processed.\n" +
                        "Reason: %s\n\n" +
                        "Please try again later.\n\n" +
                        "Thank you for using our platform.",
                username,
                songName,
                reason
        );

        emailService.sendMail(email, subject, body);
    }

    private void pushWs(String jobId, UploadStatus uploadStatus, String message){
        System.out.println("Pushing WS to userId: " + jobId + " status: " + uploadStatus);
        ws.convertAndSend("/topic/songs/" + jobId,
                (Object) Map.of("status", uploadStatus, "message", message));
    }

    private void pushWs(String jobId, UploadStatus uploadStatus, String message, String reason){
        System.out.println("Pushing WS to userId: " + jobId + " status: " + uploadStatus);
        ws.convertAndSend("/topic/songs/" + jobId,
                (Object) Map.of("status", uploadStatus, "message", message, "reason", reason));
    }
}
