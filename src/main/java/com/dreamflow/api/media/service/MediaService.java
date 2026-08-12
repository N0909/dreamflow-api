package com.dreamflow.api.media.service;
import com.dreamflow.api.media.dto.UploadRequest;
import com.dreamflow.api.media.dto.UploadResponse;
import com.dreamflow.api.auth.security.CustomUserDetails;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.entity.SongMetadata;
import com.dreamflow.api.song.entity.UploadStatus;
import com.dreamflow.api.song.entity.VisibilityStatus;
import com.dreamflow.api.song.repository.SongMetadataRepository;
import com.dreamflow.api.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final SongRepository songRepository;
    private final SongMetadataRepository songMetadataRepository;
    private final MediaProcessService mediaProcessService;

    public UploadResponse handleUpload(UploadRequest uploadRequest, MultipartFile songFile){
        String jobId = UUID.randomUUID().toString();

        Song song = new Song();
        song.setJobId(jobId);
        song.setSongName(uploadRequest.title());
        song.setSongPath("");
        song.setUploadStatus(UploadStatus.INPROCESS);
        song.setVisibilityStatus(VisibilityStatus.PENDING);

        SongMetadata songMetadata = new SongMetadata();
        songMetadata.setSongTags(uploadRequest.tags());
        songMetadata.setSongGenre(uploadRequest.genre());
        songMetadata.setSong(song);

        song.setSongMetadata(songMetadata);

        songRepository.save(song);

        try{
            Path tempFile = Files.createTempFile(
                jobId,
                    ".tmp"
            );

            songFile.transferTo(tempFile);

            CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            mediaProcessService.processUpload(jobId, tempFile, userDetails);
        }catch (IOException exception){
            exception.printStackTrace();
        }

        return new UploadResponse(
                jobId,
                song.getSongName(),
                song.getUploadStatus()
        );
    }
}
