package com.dreamflow.api.storage.service;

import com.dreamflow.api.exception.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
    @Value("${media.storage.root}")
    private String storageRoot;

    public String storeAudioFile(String jobId, File convertedFile){
        try{
            Path uploadDirectory = Paths.get(storageRoot, "songs");

            Path targetPath = uploadDirectory.resolve(
                    jobId+".mp3"
            );

            Files.createDirectories(uploadDirectory);

            Files.move(
                    convertedFile.toPath(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "songs/" + jobId + ".mp3";
        }catch (IOException e){
            throw new StorageException("Failed to Store file");
        }
    }


}
