package com.example.application.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.helger.commons.annotation.Singleton;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Singleton
public class FileStorage {
    private Path tempDirectory;

    private synchronized void createTempDirectory() {
        if (tempDirectory == null) {
            try {
                tempDirectory = Files.createTempDirectory("uploads");
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp directory", e);
            }
        }
    }

    public String addFile(MultipartFile file) {
        createTempDirectory(); // Ensure the directory is created
        var filename = UUID.randomUUID().toString();
        Path filePath = Paths.get(tempDirectory.toString(), filename);
        try {
            Files.write(filePath, file.getBytes());
            System.out.println("File uploaded to " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        return filename;
    }

    public Resource getFile(String filename) {
        createTempDirectory(); // Ensure the directory is created
        Path filePath = Paths.get(tempDirectory.toString(), filename);
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            return new ByteArrayResource(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}
