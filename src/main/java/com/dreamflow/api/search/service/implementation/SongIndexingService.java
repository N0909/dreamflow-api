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

import java.util.List;

@Service
public class SongIndexingService {
    private final EmbeddingService embeddingClient;
    private final SongSearchRepository songSearchRepository;
    private final SongRepository songRepository;
    private final SongMetadataRepository songMetadataRepository;

    public SongIndexingService(EmbeddingService embeddingClient, SongSearchRepository songSearchRepository, SongRepository songRepository, SongMetadataRepository songMetadataRepository){
        this.embeddingClient = embeddingClient;
        this.songSearchRepository = songSearchRepository;
        this.songRepository = songRepository;
        this.songMetadataRepository = songMetadataRepository;
    }

    public void reIndexAll(){
            List<Song> songs = songRepository.findAll();
            for (Song song : songs){
                try{
                    indexSong(song);
                }catch (Exception e){
                    System.out.println(song.getSongName()+" "+e.getCause());
                }
            }
    }

    @Async("indexExecutor")
    public void indexSong(Song song, SongMetadata songMetadata){
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
        songDocument.setSongTags(tags);
        songDocument.setSongGenre(genre);
        songDocument.setLyrics(lyrics);

        songSearchRepository.save(songDocument);
    }

    @Async("indexExecutor")
    public void indexSong(Song song){
        SongMetadata songMetadata = songMetadataRepository.findBySongId(
                song.getSongId()
        );

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
        songDocument.setSongTags(tags);
        songDocument.setSongGenre(genre);
        songDocument.setLyrics(lyrics);

        songSearchRepository.save(songDocument);
    }

    @Async("indexExecutor")
    public void indexSong(int songId){
        Song song = songRepository.findById(songId).orElseThrow(
                ()->new ResourceNotFoundException("song not found")
        );

        SongMetadata songMetadata = songMetadataRepository.findBySongId(
                song.getSongId()
        );

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
        songDocument.setSongTags(tags);
        songDocument.setSongGenre(genre);
        songDocument.setLyrics(lyrics);

        songSearchRepository.save(songDocument);
    }
}
