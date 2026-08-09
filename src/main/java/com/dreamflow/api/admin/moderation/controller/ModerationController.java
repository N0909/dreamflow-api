package com.dreamflow.api.admin.moderation.controller;

import com.dreamflow.api.admin.moderation.dto.HiddenSongDTO;
import com.dreamflow.api.admin.moderation.dto.ModerationResponseDTO;
import com.dreamflow.api.admin.moderation.dto.PendingSongDTO;
import com.dreamflow.api.admin.moderation.service.ModerationService;
import com.dreamflow.api.song.dto.StreamResponse;
import com.dreamflow.api.song.entity.VisibilityStatus;
import com.dreamflow.api.song.service.SongService;
import kotlin.internal.HidesMembers;
import org.jspecify.annotations.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/moderation")
public class ModerationController {
    private final ModerationService moderationService;
    private final SongService songService;

    public ModerationController(ModerationService moderationService, SongService songService){
        this.moderationService = moderationService;
        this.songService = songService;
    }

    @GetMapping("/pending-songs")
    public ResponseEntity<List<PendingSongDTO>> getAllPendingSongs(){
        List<PendingSongDTO> pendingSongs = moderationService.getAllPendingSongs();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(pendingSongs);
    }

    @GetMapping("/hidden-songs")
    public ResponseEntity<List<HiddenSongDTO>> getAllHiddenSongs(){
        List<HiddenSongDTO> hiddenSongs = moderationService.getAllHiddenSongs();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(hiddenSongs);
    }

    @PatchMapping("/approve-song")
    public ResponseEntity<ModerationResponseDTO> approveSong(@RequestParam("song-id") int songId){
        ModerationResponseDTO moderationResponseDTO = moderationService.approveSong(songId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(moderationResponseDTO);
    }

    @PatchMapping("/reject-song")
    public ResponseEntity<ModerationResponseDTO> rejectSong(@RequestParam("song-id") int songId){
        ModerationResponseDTO moderationResponseDTO = moderationService.rejectSong(songId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(moderationResponseDTO);
    }

    @PatchMapping("/hide-song")
    public ResponseEntity<ModerationResponseDTO> hideSong(@RequestParam("song-id") int songId){
        ModerationResponseDTO moderationResponseDTO = moderationService.hideSong(songId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(moderationResponseDTO);
    }

    @PatchMapping("/unhide-song")
    public ResponseEntity<ModerationResponseDTO> unhideSong(@RequestParam("song-id") int songId){
        ModerationResponseDTO moderationResponseDTO = moderationService.unhideSong(songId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(moderationResponseDTO);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> getSongStream(
            @PathVariable("id") int id,
            @RequestHeader(value="Range", required = false) String rangeHeader
    ) throws IOException {
        String songPath = songService.getSongPath(id, VisibilityStatus.PENDING);

        StreamResponse response = songService.streamSong(songPath, rangeHeader);

        if (!response.isParital()){
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(response.fileLength()))
                    .body(response.resource());
        }
        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH,
                        String.valueOf(response.end()- response.start()+1)
                ).header(HttpHeaders.CONTENT_RANGE, "bytes "+response.start()+"-"+response.end()+"/"+response.fileLength())
                .body(response.resource());
    }
}
