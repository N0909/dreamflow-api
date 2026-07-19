package com.dreamflow.api.config;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import xyz.capybara.clamav.ClamavClient;

@Configuration
public class MediaConfig {
    @Value("${clam_av.server_address}")
    private String clamAvServer;
    @Value("${clam_av.server_port}")
    private int clamAvPort;

    @Bean
    public Tika tika(){
        return new Tika();
    }

    @Bean
    public ClamavClient clamavClient(){
        return new ClamavClient(clamAvServer, clamAvPort);
    }

}
