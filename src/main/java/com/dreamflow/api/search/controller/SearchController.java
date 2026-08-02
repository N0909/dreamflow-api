package com.dreamflow.api.search.controller;

import com.dreamflow.api.search.dto.SongSearchResponse;
import com.dreamflow.api.search.service.implementation.SongSearchService;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SongSearchService songSearchService;

    public SearchController(SongSearchService songSearchService) {
        this.songSearchService = songSearchService;
    }

    @GetMapping("")
    public ResponseEntity<SongSearchResponse> getSearchResponse(
            @RequestParam(name = "query", value = "") String query) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(songSearchService.searchSong(query));
    }
}
