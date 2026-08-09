package com.dreamflow.api.admin.moderation.service;

import com.dreamflow.api.admin.moderation.dto.HiddenSongDTO;
import com.dreamflow.api.admin.moderation.dto.ModerationResponseDTO;
import com.dreamflow.api.admin.moderation.dto.PendingSongDTO;
import com.dreamflow.api.exception.exceptions.ModerationException;
import com.dreamflow.api.exception.exceptions.ResourceNotFoundException;
import com.dreamflow.api.search.elastic.indexing.SongIndexingService;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.entity.VisibilityStatus;
import com.dreamflow.api.song.repository.SongRepository;
import com.dreamflow.api.song.service.SongService;
import com.dreamflow.api.util.service.email.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModerationService {
    private final SongRepository songRepository;
    private final EmailService emailService;
    private final SongIndexingService songIndexingService;
    private final SongService songService;

    public ModerationService(SongRepository songRepository, EmailService emailService, SongIndexingService songIndexingService, SongService songService){
        this.songRepository = songRepository;
        this.emailService = emailService;
        this.songIndexingService = songIndexingService;
        this.songService = songService;
    }

    public List<PendingSongDTO> getAllPendingSongs(){
        return songRepository.findAllPendingViewStatus();
    }

    public List<HiddenSongDTO> getAllHiddenSongs() {return songRepository.findAllHiddenViewStatus();}

    @Transactional
    public ModerationResponseDTO approveSong(int songId){
        Song song =songRepository.findBySongId(songId).orElseThrow(
                () -> new ResourceNotFoundException("Song with id " + songId + " doesn't exist")
        );

        if (song.getVisibilityStatus() != VisibilityStatus.PENDING) {
            throw new ModerationException("Song is not in pending review.");
        }

        song.setVisibilityStatus(VisibilityStatus.APPROVED);
        songRepository.save(song);


        String subject = "Your song has been approved on DreamFlow";

        String body = String.format("""
        Hello %s,

        Great news!

        Your song "%s" has been reviewed and approved by our moderation team. It is now available for streaming on DreamFlow.

        Thank you for sharing your music with us. We wish you the very best on your musical journey!

        Regards,
        DreamFlow Team
        """,
                song.getUploadedBy().getUsername(),
                song.getSongName()
        );

        emailService.sendMail(song.getUploadedBy().getEmail(), subject, body);
        songIndexingService.indexSong(song);

        return new ModerationResponseDTO(song.getSongId(), song.getSongName(), song.getVisibilityStatus());
    }

    public ModerationResponseDTO rejectSong(int songId){
        Song song =songRepository.findBySongId(songId).orElseThrow(
                () -> new ResourceNotFoundException("Song with id " + songId + " doesn't exist")
        );

        if (song.getVisibilityStatus() != VisibilityStatus.PENDING) {
            throw new ModerationException("Song is not in pending review.");
        }

        song.setVisibilityStatus(VisibilityStatus.REJECTED);
        songRepository.save(song);

        String subject = "Your song could not be approved on DreamFlow";

        String body = String.format("""
        Hello %s,

        Thank you for uploading your song "%s" to DreamFlow.

        After review by our moderation team, we were unable to approve your submission because it does not meet our platform's content guidelines.

        You are welcome to make the necessary changes and submit your song again for review.

        Thank you for your understanding.

        Regards,
        DreamFlow Team
        """,
                song.getUploadedBy().getUsername(),
                song.getSongName()
        );

        emailService.sendMail(song.getUploadedBy().getEmail(), subject, body);

        return new ModerationResponseDTO(song.getSongId(), song.getSongName(), song.getVisibilityStatus());
    }

    public ModerationResponseDTO hideSong(int songId){
        Song song =songRepository.findBySongId(songId).orElseThrow(
                () -> new ResourceNotFoundException("Song with id " + songId + " doesn't exist")
        );

        song.setVisibilityStatus(VisibilityStatus.HIDDEN);
        songRepository.save(song);

        String subject = "Your song has been hidden on DreamFlow";

        String body = String.format("""
        Hello %s,

        Your song "%s" has been removed from public visibility on DreamFlow following a moderation review.

        As a result, the song is no longer available for streaming on the platform.

        If you believe this action was taken in error or you would like more information, please contact our support team.

        Thank you for your understanding.

        Regards,
        DreamFlow Team
        """,
                song.getUploadedBy().getUsername(),
                song.getSongName()
        );

        emailService.sendMail(song.getUploadedBy().getEmail(), subject, body);

        return new ModerationResponseDTO(song.getSongId(), song.getSongName(), song.getVisibilityStatus());
    }
    @Transactional
    public ModerationResponseDTO unhideSong(int songId){
        Song song =songRepository.findBySongId(songId).orElseThrow(
                () -> new ResourceNotFoundException("Song with id " + songId + " doesn't exist")
        );

        if (song.getVisibilityStatus() != VisibilityStatus.HIDDEN){
            throw new ModerationException("Song is not hidden.");
        }

        song.setVisibilityStatus(VisibilityStatus.APPROVED);

        songRepository.save(song);
        String subject = "Your song is visible again on DreamFlow";

        String body = String.format("""
        Hello %s,

        Your song "%s" has been restored and is now visible and available for streaming on DreamFlow.

        Thank you for being a part of DreamFlow.

        Regards,
        DreamFlow Team
        """,
                song.getUploadedBy().getUsername(),
                song.getSongName()
        );

        emailService.sendMail(song.getUploadedBy().getEmail(), subject, body);

        return new ModerationResponseDTO(song.getSongId(), song.getSongName(), song.getVisibilityStatus());
    }
}
