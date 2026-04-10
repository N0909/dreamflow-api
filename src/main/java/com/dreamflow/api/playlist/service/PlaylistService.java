package com.dreamflow.api.playlist.service;

import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.playlist.dto.PlaylistRequest;
import com.dreamflow.api.playlist.dto.PlaylistResponse;
import com.dreamflow.api.playlist.entity.Playlist;
import com.dreamflow.api.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    public PlaylistResponse createPlaylist(int userId, PlaylistRequest request){
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("User with Id Not Found"));

        Playlist playlist = new Playlist();
        playlist.setPlaylistName(request.playlistName());
        playlist.setCreatedAt(LocalDateTime.now());
        user.addPlaylist(playlist);

        Playlist createdPlaylist = playlistRepository.save(playlist);

        return new PlaylistResponse(createdPlaylist.getPlaylistId(), createdPlaylist.getPlaylistName(),playlist.getCreatedAt());
    }

    public List<PlaylistResponse> getAllPlaylist(int userId){
        List<PlaylistResponse> playlists = playlistRepository.getAllPlaylists(userId);

        return playlists;
    }
}
