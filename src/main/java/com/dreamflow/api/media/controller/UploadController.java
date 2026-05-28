package com.dreamflow.api.media.controller;

import com.dreamflow.api.media.dto.UploadRequest;
import com.dreamflow.api.media.dto.UploadResponse;
import com.dreamflow.api.media.service.MediaService;
import com.dreamflow.api.song.entity.UploadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {
    private final MediaService mediaService;

    @PostMapping()
    public ResponseEntity<UploadResponse> handleSongUpload(UploadRequest uploadRequest,@RequestPart("song-file") MultipartFile songFile){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(mediaService.handleUpload(uploadRequest, songFile));
    }
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
