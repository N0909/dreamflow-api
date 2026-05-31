package com.dreamflow.api.playlist.service;

import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.exception.exceptions.ResourceAlreadyExistException;
import com.dreamflow.api.exception.exceptions.ResourceNotFoundException;
import com.dreamflow.api.playlist.dto.*;
import com.dreamflow.api.playlist.entity.Playlist;
import com.dreamflow.api.playlist.entity.PlaylistSong;
import com.dreamflow.api.playlist.repository.PlaylistRepository;
import com.dreamflow.api.playlist.repository.PlaylistSongRepository;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.repository.SongRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final SongRepository songRepository;


    @Transactional
    public PlaylistResponse createPlaylist(PlaylistRequest request){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        boolean playlistExist = playlistRepository.existsByPlaylistNameAndUser_UserId(request.playlistName(), userDetails.getUserId());

        if (playlistExist){
            throw new ResourceAlreadyExistException("Playlist with name "+request.playlistName()+" Already exist");
        }

        User user = userRepository.findById(userDetails.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not found"));

        Playlist playlist = new Playlist();
        playlist.setPlaylistName(request.playlistName());
        playlist.setCreatedAt(LocalDateTime.now());
        user.addPlaylist(playlist);

        Playlist createdPlaylist = playlistRepository.save(playlist);

        return new PlaylistResponse(createdPlaylist.getPlaylistId(), createdPlaylist.getPlaylistName(),playlist.getCreatedAt());
    }

    @Transactional
    public PlaylistResponse updatePlaylistDetails(int playlistId, PlaylistRequest request){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        Playlist playlist = playlistRepository.findByPlaylistIdAndUser_UserId(playlistId, userDetails.getUserId()).orElseThrow(()->new ResourceNotFoundException("Not Found"));

        playlist.setPlaylistName(request.playlistName());

        return new PlaylistResponse(playlist.getPlaylistId(), playlist.getPlaylistName(), playlist.getCreatedAt());
    }

    public List<PlaylistResponse> getAllPlaylist(){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        List<PlaylistResponse> playlists = playlistRepository.getAllPlaylists(userDetails.getUserId());

        return playlists;
    }

    @Transactional
    public SongAddedResponse addSongInPlaylist(int playlistId, AddNewSongRequest songRequest){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        Playlist playlist = playlistRepository.findByPlaylistIdAndUser_UserId(playlistId, userDetails.getUserId()).orElseThrow(()->new ResourceNotFoundException("Playlist Not Found"));

        Song song = songRepository.findById(songRequest.songId()).orElseThrow(()->new ResourceNotFoundException("Song not found"));

        boolean songAlreadyExists = playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(playlist.getPlaylistId(), song.getSongId());

        if (songAlreadyExists){
            throw new ResourceAlreadyExistException(song.getSongName()+" already exists in playlists");
        }

        PlaylistSong playlistSong = new PlaylistSong();

        playlist.addPlaylistSongList(playlistSong);
        song.addPlaylistSong(playlistSong);

        PlaylistSong created = playlistSongRepository.save(playlistSong);

        return new SongAddedResponse(
                created.getPlaylist().getPlaylistId(),
                created.getPlaylist().getPlaylistName(),
                created.getSong().getSongId(),
                created.getSong().getSongName()
        );
    }

    public List<PlaylistResponse> getTopNPlaylist(int n){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        return playlistRepository.findTopNRecent(userDetails.getUserId(), PageRequest.of(0, n));
    }

    public List<PlaylistResponse> getTopNPlaylist(int n, int userId){
        return playlistRepository.findTopNRecent(userId, PageRequest.of(0, n));
    }

    public PlaylistSongResponse getPlaylistSongs(int playlistId){
         CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

         Playlist playlist = playlistRepository.findByPlaylistIdAndUser_UserId(playlistId, userDetails.getUserId()).orElseThrow(()->new ResourceNotFoundException("Playlist doesn't exist"));

         List<PlaylistSong> playlistSongs = playlistSongRepository.findByPlaylist_PlaylistId(playlist.getPlaylistId());

         List<SongDTO> songs = playlistSongs.stream()
                 .map(
                         playlistSong->new SongDTO(
                         playlistSong.getSong().getSongId(),
                         playlistSong.getSong().getSongName(),
                         playlistSong.getSong().getDurationMs()
                    )
                ).toList();

         return new PlaylistSongResponse(
                 playlist.getPlaylistId(),
                 playlist.getPlaylistName(),
                 playlist.getCreatedAt(),
                 songs
         );
    }

    @Transactional
    public void deletePlaylistSong(int playlistId, int songId){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        Playlist playlist = playlistRepository.findByPlaylistIdAndUser_UserId(playlistId, userDetails.getUserId()).orElseThrow(()->new ResourceNotFoundException("Playlist Not Found"));

        boolean isRemoved = playlist.getPlaylistSongList()
                .removeIf(play -> play.getSong().getSongId()==songId);

        if (!isRemoved){
            throw new ResourceNotFoundException("Song With Id "+ songId + " Not Found in playlist");
        }
    }

}
