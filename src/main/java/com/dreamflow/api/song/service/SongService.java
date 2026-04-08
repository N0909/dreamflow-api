package com.dreamflow.api.song.service;

import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.dto.StreamResponse;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final Map<Integer, String> cache = new ConcurrentHashMap<>();

    public Page<SongDTO> getSongs(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return songRepository.findSongs(pageable);
    }

    public SongDTO getSong(int songId){
        SongDTO song = songRepository.findSongById(songId).orElseThrow(
                ()->new IllegalStateException("Song with id "+songId+" doesn't exist"));
        return song;
    }

    public StreamResponse streamSong(int songId, String rangeHeader) throws IOException {
        String songPath = cache.computeIfAbsent(
                songId,
                key->
                songRepository.findById(songId).orElseThrow(
                        ()->new IllegalStateException("Song with id "+songId+" doesn't exist")
            ).getSongPath()
        );

        File file = new File(songPath);
        long fileLength = file.length();

        // Case 1: Range Not Present then just give full file
        if (rangeHeader==null){
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return new StreamResponse(resource, 0, fileLength-1, fileLength, false);
        }
        String range = rangeHeader.replace("bytes=", "");
        String[] parts = range.split("-");
        // Case 2: Range Present
        long start = Long.parseLong(parts[0]);
        long end;

        if (parts.length > 1 && !parts[1].isEmpty()) {
            end = Long.parseLong(parts[1]);
        } else {
            end = Math.min(start + 1024 * 1024, fileLength - 1);
        }

        end = Math.min(end, fileLength - 1);

        try(RandomAccessFile raf = new RandomAccessFile(file, "r")){
            raf.seek(start);

            byte[] data = new byte[(int) (end-start+1)];
            raf.readFully(data);

            ByteArrayResource byteArrayResource = new ByteArrayResource(data);

            return new StreamResponse(byteArrayResource, start, end, fileLength, true);
        }
    }

    // for clearing cache after 10 mins
    @Scheduled(fixedRate = 600000)
    public void clearCache(){
        cache.clear();
    }
}
