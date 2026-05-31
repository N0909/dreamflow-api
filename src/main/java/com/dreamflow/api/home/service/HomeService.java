package com.dreamflow.api.home.service;

import com.dreamflow.api.home.dto.HomeResponseDTO;
import com.dreamflow.api.playlist.dto.PlaylistResponse;
import com.dreamflow.api.playlist.service.PlaylistService;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final SongService songService;
    private final PlaylistService playlistService;

    public HomeResponseDTO getHomeData(int pageNo, int pageSize, int playlistSize){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        CompletableFuture<Page<SongDTO>> songDTOListFuture = getSongsAsync(pageNo, pageSize);
        CompletableFuture<List<PlaylistResponse>>  playlistResponseListFuture = getPlaylistAsync(userDetails.getUserId(), playlistSize);

        Page<SongDTO> songDTOPage = songDTOListFuture.join();
//        Page<SongDTO> songDTOPage = songService.getSongs(0,20);
        List<PlaylistResponse> playlistResponseList = playlistResponseListFuture.join();
//        List<PlaylistResponse> playlistResponseList = playlistService.getTopNPlaylist(5);

        return new HomeResponseDTO(
                songDTOPage,
                playlistResponseList
        );
    }

    @Async("homeExecutor")
    protected CompletableFuture<Page<SongDTO>> getSongsAsync(int page_no, int page_size){
        return CompletableFuture.completedFuture(
                songService.getSongs(page_no, page_size)
        );
    }

    @Async("homeExecutor")
    protected CompletableFuture<List<PlaylistResponse>> getPlaylistAsync(int userId,int n){
        return CompletableFuture.completedFuture(
                playlistService.getTopNPlaylist(n, userId)
        );
    }
}
