package com.dreamflow.api.playlist.controller;

import com.dreamflow.api.playlist.dto.*;
import com.dreamflow.api.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/me/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @PostMapping()
    public ResponseEntity<PlaylistResponse> createPlaylist(@RequestBody PlaylistRequest request){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playlistService.createPlaylist(request));
    }

    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> updatePlaylist(@PathVariable int playlistId, @RequestBody PlaylistRequest request){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playlistService.updatePlaylistDetails(playlistId, request));
    }

    @GetMapping()
    public ResponseEntity<List<PlaylistResponse>> getAllPlaylist(){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playlistService.getAllPlaylist());
    }

    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<SongAddedResponse> addSongInPlaylist(@PathVariable("playlistId") int playlistId, @RequestBody AddNewSongRequest songRequest){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playlistService.addSongInPlaylist(playlistId, songRequest));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistSongResponse> getPlaylistSongs(@PathVariable("playlistId") int playlistId){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(playlistService.getPlaylistSongs(playlistId));
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> deleteSongFromPlaylist(@PathVariable("playlistId") int playlistId, @PathVariable("songId") int songId){
        playlistService.deletePlaylistSong(playlistId, songId);
        return ResponseEntity.noContent().build();
    }
}
