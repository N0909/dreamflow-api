package com.dreamflow.api.search.elastic.indexing;

import com.dreamflow.api.exception.exceptions.ResourceNotFoundException;
import com.dreamflow.api.search.dto.EmbeddingRequest;
import com.dreamflow.api.search.elastic.entity.SongDocument;
import com.dreamflow.api.search.elastic.embedding.EmbeddingService;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.entity.SongMetadata;
import com.dreamflow.api.song.repository.SongMetadataRepository;
import com.dreamflow.api.song.repository.SongRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SongIndexingService {
    private final EmbeddingService embeddingClient;
    private final SongRepository songRepository;
    private final SongMetadataRepository songMetadataRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public SongIndexingService(EmbeddingService embeddingClient, ElasticsearchOperations elasticsearchOperations,
            SongRepository songRepository, SongMetadataRepository songMetadataRepository) {
        this.embeddingClient = embeddingClient;
        this.elasticsearchOperations = elasticsearchOperations;
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

            elasticsearchOperations.save(songDocument);
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

            elasticsearchOperations.save(songDocument);
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

            elasticsearchOperations.save(songDocument);

        } catch (Exception e) {
            // ignoring for now will log it in future
        }
    }
}
