package com.dreamflow.api.song.controller;

import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.dto.StreamResponse;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @GetMapping()
    public ResponseEntity<List<SongDTO>> getSongs(){
        List<SongDTO> songList = songService.getSongs();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(songList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSong(@PathVariable("id") int id){
        SongDTO song = songService.getSong(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(song);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> getSongStream(
            @PathVariable("id") int id,
            @RequestHeader(value="Range", required = false) String rangeHeader
    ) throws IOException  {
        StreamResponse response = songService.streamSong(id, rangeHeader);

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
