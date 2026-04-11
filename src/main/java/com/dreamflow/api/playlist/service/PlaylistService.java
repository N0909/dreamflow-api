package com.dreamflow.api.playlist.service;

import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.playlist.dto.*;
import com.dreamflow.api.playlist.entity.Playlist;
import com.dreamflow.api.playlist.entity.PlaylistSong;
import com.dreamflow.api.playlist.repository.PlaylistRepository;
import com.dreamflow.api.playlist.repository.PlaylistSongRepository;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.repository.SongRepository;
import jakarta.transaction.Transactional;
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
    private final PlaylistSongRepository playlistSongRepository;
    private final SongRepository songRepository;

    @Transactional
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

    @Transactional
    public SongAddedResponse addSongInPlaylist(int playlistId, AddNewSongRequest songRequest){
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(()->new IllegalStateException("Playlist Not Found"));

        Song song = songRepository.findById(songRequest.songId()).orElseThrow(()->new IllegalStateException("Song not found"));

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

    public PlaylistSongResponse getPlaylistSongs(int playlistId){
         List<PlaylistSong> playlistSongs = playlistSongRepository.findByPlaylist_PlaylistId(playlistId);

         if (playlistSongs.isEmpty()){
             return null;
         }

         List<SongDTO> songs = playlistSongs.stream()
                 .map(
                         playlistSong->new SongDTO(
                         playlistSong.getSong().getSongId(),
                         playlistSong.getSong().getSongName(),
                         playlistSong.getSong().getDurationMs()
                    )
                ).toList();

         return new PlaylistSongResponse(
                 playlistSongs.getFirst().getPlaylist().getPlaylistId(),
                 playlistSongs.getFirst().getPlaylist().getPlaylistName(),
                 playlistSongs.getFirst().getPlaylist().getCreatedAt(),
                 songs
         );
    }
}
