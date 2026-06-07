package com.dreamflow.api.media.service;
import com.dreamflow.api.exception.exceptions.*;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.entity.UploadStatus;
import com.dreamflow.api.song.repository.SongRepository;
import com.dreamflow.api.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class MediaProcessService {
    private final ExecutorService executorService;
    private final Tika tika;
    private final ClamavClient clamavClient;
    private final StorageService storageService;
    private final SongRepository songRepository;
    private final NotificationService notificationService;

    @Async("mediaProcessingExecutor")
    public void processUpload(String jobId, Path tempFile, CustomUserDetails userDetails){
            Song song = songRepository.findByJobId(jobId).orElseThrow(()->new ResourceNotFoundException("not found"));
            File file = null;
            try{
                validateMimeType(tempFile);
                scanForVirus(tempFile);
                file = convertToMp3(jobId,tempFile);
                long durationMs = extractDurationMs(file);
                String path = storageService.storeAudioFile(jobId, file);

                song.setSongPath(path);
                song.setUploadStatus(UploadStatus.COMPLETED);
                song.setDurationMs(durationMs);

                songRepository.save(song);

                // getUsername is actually email here
                // getName is the real username
                notificationService.notifySuccess(jobId, userDetails.getName(), userDetails.getUsername(), song.getSongName());

            }catch (IllegalMimeTypeException exception){
                markFailed(song, "invalid audio format");
                notificationService.notifyFailure(jobId, userDetails.getUsername(), userDetails.getName(), song.getSongName(), exception.getMessage());
            }catch (VirusDetectedException exception){
                markFailed(song, "file contains viruses");
                notificationService.notifyFailure(jobId, userDetails.getUsername(), userDetails.getName(), song.getSongName(), exception.getMessage());
            }catch (AudioConversionException exception){
                markFailed(song, "failed to convert file");
                notificationService.notifyFailure(jobId, userDetails.getUsername(), userDetails.getName(), song.getSongName(), exception.getMessage());
            }catch(StorageException exception){
                markFailed(song, "failed to store file");
                notificationService.notifyFailure(jobId, userDetails.getUsername(), userDetails.getName(), song.getSongName(), exception.getMessage());
            }
            catch (Exception exception) {
                exception.printStackTrace();
                markFailed(song, "internal processing failed");
                notificationService.notifyFailure(jobId, userDetails.getUsername(), userDetails.getName(), song.getSongName(), exception.getMessage());
            }finally {
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
    }

    private void validateMimeType(Path tempFile) {
        try{
            File songFile = new File(tempFile.toUri());
            String detectedType = tika.detect(songFile);

            if (!detectedType.startsWith("audio/")){
                throw new IllegalMimeTypeException("Invalid file type");
            }
        }catch (IOException exception){
            exception.printStackTrace();
            throw new FileProcessingException("Failed to read file");
        }
    }

    private void scanForVirus(Path tempFile) {
        ScanResult scanResult;

        try (FileInputStream songFile = new FileInputStream(tempFile.toFile())) {
            scanResult = clamavClient.scan(songFile);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to read file for virus scan");
        } catch (Exception e) {
            throw new VirusScanUnavailableException("Virus scan service unavailable");
        }

        if (scanResult instanceof ScanResult.VirusFound){
            throw new VirusDetectedException("uploaded file contains virus");
        }

        if (!(scanResult instanceof ScanResult.OK)){
            throw new VirusScanUnavailableException("Virus scan service unavailable");
        }
    }

    private File convertToMp3(String jobId, Path songPath){
        Path inputPath = null;
        Path outputPath = null;
        try {
            inputPath = Files.createTempFile(
                    jobId,
                    ".tmp"
            );

            Files.copy(
                    songPath,
                    inputPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            outputPath = Files.createTempFile(
                    jobId,
                    ".mp3"
            );

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg",
                    "-y",
                    "-i",
                    inputPath.toString(),
                    "-vn",
                    "-ar",
                    "44100",
                    "-ac",
                    "2",
                    "-b:a",
                    "192k",
                    outputPath.toString()
            );

            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);

            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if (exitCode !=0 ){
                throw new AudioConversionException(
                        "File conversion failed"
                );
            }

            return outputPath.toFile();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new FileProcessingException("Failed to read file");
        } finally {
            if (inputPath!=null){
                try{
                    Files.deleteIfExists(inputPath);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private long extractDurationMs(File audioFile){
        try{

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffprobe",
                    "-v","error",
                    "-show_entries", "format=duration",
                    "-of","default=noprint_wrappers=1:nokey=1",
                    audioFile.getAbsolutePath()
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            String output = new String(
              process.getInputStream().readAllBytes()
            );

            double durationSeconds = Double.parseDouble(output.trim());

            return (long) (durationSeconds * 1000);
        }catch (IOException e){
            System.out.println(e.getCause());
            throw new RuntimeException("Failed to extract duration", e);
        }
    }


    private void markFailed(Song song,String message){
        song.setUploadStatus(UploadStatus.FAILED);
        song.setFailReason(message);
        songRepository.save(song);
    }
}
