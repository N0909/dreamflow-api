package com.dreamflow.api.search.repository;

import com.dreamflow.api.search.entity.SongDocument;
import com.dreamflow.api.song.entity.Song;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

//@SpringBootTest
public class SongSearchRepositoryTest {
//    @Autowired
    private SongSearchRepository songSearchRepository;

//    @Test
    public void testSaveSong(){
        SongDocument songDocument = new SongDocument();
        songDocument.setSongId(1);
        songDocument.setSongName("name");
        songDocument.setTags("tags");
        songDocument.setGenre("genre");
        float[] dummyEmbedding = new float[384];
        Random random = new Random();
        for (int i = 0; i < 384; i++) {
            dummyEmbedding[i] = random.nextFloat();
        }
        songDocument.setEmbedding(dummyEmbedding);

        songSearchRepository.save(songDocument);

    }
}
