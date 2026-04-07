package com.dreamflow.api.song.repository;

import com.dreamflow.api.song.entity.Song;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@DataJpaTest
public class SongRepositoryTest {
    @Autowired
    private SongRepository songRepository;

    @Test
    void shouldFetchAllSongs(){
        Song song1 = new Song();
        song1.setSongName("Name");
        song1.setSongPath("w");
        song1.setDurationMs(213144);

        Song song2 = new Song();
        song2.setSongName("Name");
        song2.setSongPath("w");
        song2.setDurationMs(213144);

        songRepository.save(song1);
        songRepository.save(song2);

        List<Song> songList = songRepository.getAllSongs();

        assertEquals("",2, songList.size());
    }
}
