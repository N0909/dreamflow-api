package com.dreamflow.api.song.service;

import com.dreamflow.api.exception.exceptions.ResourceNotFoundException;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.dto.StreamResponse;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.repository.SongRepository;
import com.dreamflow.api.util.LRUCache;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SongService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private final SongRepository songRepository;
//    private final Map<Integer, String> cache = Collections.synchronizedMap(new LRUCache<>(100));

    public Page<SongDTO> getSongs(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return songRepository.findSongs(pageable);
    }

    @Cacheable(value="song", key="#songId")
    public SongDTO getSong(int songId){
        SongDTO song = songRepository.findSongById(songId).orElseThrow(
                ()->new ResourceNotFoundException("Song with id "+songId+" doesn't exist"));
        return song;
    }

    private String getSongPath(int songId){
        // First Search in the Redis Cache
        Object path= stringRedisTemplate.opsForValue().get("songPath::"+songId);

        if (path==null){ // If Cache miss
            // Fetch from db
            path = songRepository.findById(songId).orElseThrow(
                    () -> new ResourceNotFoundException("Song with id "+songId+" doesn't exist")
            ).getSongPath();
            // store in cache for 120 seconds
            stringRedisTemplate.opsForValue().set("songPath::"+songId,(String) path, 120, TimeUnit.SECONDS);
        }
        return (String) path;
    }

    public StreamResponse streamSong(int songId, String rangeHeader) throws IOException {
        String songPath = getSongPath(songId);

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
}
