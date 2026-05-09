package com.dreamflow.api.home.service;

import com.dreamflow.api.home.dto.HomeResponseDTO;
import com.dreamflow.api.playlist.dto.PlaylistResponse;
import com.dreamflow.api.playlist.dto.PlaylistSongResponse;
import com.dreamflow.api.playlist.entity.Playlist;
import com.dreamflow.api.playlist.service.PlaylistService;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final SongService songService;
    private final PlaylistService playlistService;
    private final ExecutorService executorService;

    public HomeResponseDTO getHomeData(int userId){

        CompletableFuture<Page<SongDTO>> songsFuture =
                CompletableFuture.supplyAsync(()->
                   songService.getSongs(0,10),
                    executorService
                );

        CompletableFuture<List<PlaylistResponse>> playlistFuture =
                CompletableFuture.supplyAsync(()->
                        playlistService.getAllPlaylist(userId),
                        executorService
                );

        Page<SongDTO> songDTOList = songsFuture.join();
        List<PlaylistResponse> playlistResponseList = playlistFuture.join();

        return new HomeResponseDTO(
                songDTOList,
                playlistResponseList
        );
    }
}
