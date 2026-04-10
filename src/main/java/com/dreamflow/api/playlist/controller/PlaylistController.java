package com.dreamflow.api.playlist.controller;

import com.dreamflow.api.playlist.dto.PlaylistRequest;
import com.dreamflow.api.playlist.dto.PlaylistResponse;
import com.dreamflow.api.playlist.entity.Playlist;
import com.dreamflow.api.playlist.service.PlaylistService;
import com.dreamflow.api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;


    @PostMapping()
    public ResponseEntity<PlaylistResponse> createPlaylist(@RequestBody PlaylistRequest request){
        CustomUserDetails details = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert details != null;
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playlistService.createPlaylist(details.getUserId(), request));
    }

    @GetMapping()
    public ResponseEntity<List<PlaylistResponse>> getAllPlaylist(){
        CustomUserDetails details = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert details != null;
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playlistService.getAllPlaylist(details.getUserId()));
    }
}
