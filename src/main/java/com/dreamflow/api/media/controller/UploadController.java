package com.dreamflow.api.media.controller;

import com.dreamflow.api.media.dto.UploadRequest;
import com.dreamflow.api.media.dto.UploadResponse;
import jakarta.mail.Multipart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @PostMapping()
    public void handleSongUpload(UploadRequest uploadRequest, @RequestParam("song-file") MultipartFile file){
        //TODO
    }
}
