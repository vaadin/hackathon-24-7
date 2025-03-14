package com.example.application.services;

import org.jspecify.annotations.NonNull;
import org.springframework.web.multipart.MultipartFile;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class UploadService {
    private final FileStorage fileStorage;

    public UploadService(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @NonNull
    public String uploadFile(@NonNull MultipartFile file) {
        //throw new RuntimeException("Upload failed");
        return fileStorage.addFile(file);
    }
}
