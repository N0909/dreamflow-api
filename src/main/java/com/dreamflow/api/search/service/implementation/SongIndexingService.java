package com.dreamflow.api.search.service.implementation;

import com.dreamflow.api.exception.exceptions.ResourceNotFoundException;
import com.dreamflow.api.search.dto.EmbeddingRequest;
import com.dreamflow.api.search.entity.SongDocument;
import com.dreamflow.api.search.repository.SongSearchRepository;
import com.dreamflow.api.search.service.EmbeddingService;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.entity.SongMetadata;
import com.dreamflow.api.song.repository.SongMetadataRepository;
import com.dreamflow.api.song.repository.SongRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SongIndexingService {
    private final EmbeddingService embeddingClient;
    private final SongSearchRepository songSearchRepository;
    private final SongRepository songRepository;
    private final SongMetadataRepository songMetadataRepository;

    public SongIndexingService(EmbeddingService embeddingClient, SongSearchRepository songSearchRepository,
            SongRepository songRepository, SongMetadataRepository songMetadataRepository) {
        this.embeddingClient = embeddingClient;
        this.songSearchRepository = songSearchRepository;
        this.songRepository = songRepository;
        this.songMetadataRepository = songMetadataRepository;
    }

    @Async("indexExecutor")
    public void indexSong(Song song, SongMetadata songMetadata) {
        try {
            String songName = song.getSongName() != null ? song.getSongName() : "";
            String tags = songMetadata.getSongTags() != null ? songMetadata.getSongTags() : "";
            String genre = songMetadata.getSongGenre() != null ? songMetadata.getSongGenre() : "";
            String lyrics = songMetadata.getSongLyrics() != null ? songMetadata.getSongLyrics() : "";
            String combinedText = (songName + " " + tags + " " + genre).trim();

            EmbeddingRequest embeddingRequest = new EmbeddingRequest(combinedText);
            float[] embedding = embeddingClient.getEmbedding(embeddingRequest);

            SongDocument songDocument = new SongDocument();
            songDocument.setSongId(song.getSongId());
            songDocument.setSongName(songName);
            songDocument.setTags(tags);
            songDocument.setGenre(genre);
            songDocument.setLyrics(lyrics);
            songDocument.setEmbedding(embedding);

            songSearchRepository.save(songDocument);
        } catch (Exception e) {
            // ignoring for now will log it in future
        }
    }

    @Async("indexExecutor")
    public void indexSong(Song song) {
        try {

            SongMetadata songMetadata = songMetadataRepository.findBySong_SongId(
                    song.getSongId()).orElseThrow(
                            () -> new ResourceNotFoundException("Song with id " + song.getSongId() + " doesn't exist"));

            String songName = song.getSongName() != null ? song.getSongName() : "";
            String tags = songMetadata.getSongTags() != null ? songMetadata.getSongTags() : "";
            String genre = songMetadata.getSongGenre() != null ? songMetadata.getSongGenre() : "";
            String lyrics = songMetadata.getSongLyrics() != null ? songMetadata.getSongLyrics() : "";
            String combinedText = (songName + " " + tags + " " + genre).trim();

            EmbeddingRequest embeddingRequest = new EmbeddingRequest(combinedText);
            float[] embedding = embeddingClient.getEmbedding(embeddingRequest);

            SongDocument songDocument = new SongDocument();
            songDocument.setSongId(song.getSongId());
            songDocument.setSongName(songName);
            songDocument.setTags(tags);
            songDocument.setGenre(genre);
            songDocument.setLyrics(lyrics);
            songDocument.setEmbedding(embedding);

            songSearchRepository.save(songDocument);
        } catch (Exception e) {
            // ignoring for now will log it in future
        }
    }

    @Async("indexExecutor")
    public void indexSong(int songId) {
        try {

            Song song = songRepository.findById(songId).orElseThrow(
                    () -> new ResourceNotFoundException("song not found"));

            SongMetadata songMetadata = songMetadataRepository.findBySong_SongId(
                    song.getSongId()).orElseThrow(
                            () -> new ResourceNotFoundException("Song with id " + song.getSongId() + " doesn't exist"));

            String songName = song.getSongName() != null ? song.getSongName() : "";
            String tags = songMetadata.getSongTags() != null ? songMetadata.getSongTags() : "";
            String genre = songMetadata.getSongGenre() != null ? songMetadata.getSongGenre() : "";
            String lyrics = songMetadata.getSongLyrics() != null ? songMetadata.getSongLyrics() : "";
            String combinedText = (songName + " " + tags + " " + genre).trim();

            EmbeddingRequest embeddingRequest = new EmbeddingRequest(combinedText);
            float[] embedding = embeddingClient.getEmbedding(embeddingRequest);

            SongDocument songDocument = new SongDocument();
            songDocument.setSongId(song.getSongId());
            songDocument.setSongName(songName);
            songDocument.setTags(tags);
            songDocument.setGenre(genre);
            songDocument.setLyrics(lyrics);
            songDocument.setEmbedding(embedding);

            songSearchRepository.save(songDocument);

        } catch (Exception e) {
            // ignoring for now will log it in future
        }
    }
}
