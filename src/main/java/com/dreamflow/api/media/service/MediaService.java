package com.dreamflow.api.media.service;
import com.dreamflow.api.media.dto.UploadRequest;
import com.dreamflow.api.media.dto.UploadResponse;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.entity.UploadStatus;
import com.dreamflow.api.song.repository.SongMetadataRepository;
import com.dreamflow.api.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private SongRepository songRepository;
    private SongMetadataRepository songMetadataRepository;

    public UploadResponse HandleUpload(UploadRequest uploadRequest, MultipartFile songFile){
        String jobId = UUID.randomUUID().toString();

        Song song = new Song();
        song.setJobId(jobId);
        song.setSongName(uploadRequest.title());
        song.setUploadStatus(UploadStatus.INPROCESS);

        songRepository.save(song);

        // process async code here

        return new UploadResponse(
                jobId,
                song.getSongName(),
                song.getUploadStatus()
        );
    }
}
