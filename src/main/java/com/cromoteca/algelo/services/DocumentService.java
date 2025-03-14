package com.cromoteca.algelo.services;

import java.nio.file.Path;

import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.cromoteca.algelo.entities.Document;
import com.cromoteca.algelo.repositories.DocumentRepository;
import com.github.javafaker.Faker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;
import com.vaadin.hilla.exception.EndpointException;

@BrowserCallable
@AnonymousAllowed
public class DocumentService extends CrudRepositoryService<Document, Long, DocumentRepository> {
    private Faker faker = new Faker();
    private Path storage = Path.of(System.getProperty("user.dir"), "docs");

    public long storeDocument(@Nullable Long documentId, MultipartFile file) {
        Document document = null;

        if (documentId != null) {
            document = getRepository().findById(documentId).orElse(null);
        }

        var pdfId = faker.number().randomNumber();
        var filePath = storage.resolve(pdfId + ".pdf");

        try {
            file.transferTo(filePath);
        } catch (Exception e) {
            throw new EndpointException("Failed to store document", e);
        }

        if (document != null) {
            document.setPdfId(pdfId);
            document = getRepository().save(document);
        }

        return pdfId;
    }
}
