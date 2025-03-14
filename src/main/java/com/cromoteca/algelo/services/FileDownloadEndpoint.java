package com.cromoteca.algelo.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDownloadEndpoint {

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }

        var filePath = Path.of(System.getProperty("user.dir"), "docs", id + ".pdf");
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            var resource = new InputStreamResource(Files.newInputStream(filePath));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".pdf\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }

    }
}
