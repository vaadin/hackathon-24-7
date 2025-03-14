package com.cromoteca.algelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cromoteca.algelo.entities.Document;
import com.cromoteca.algelo.repositories.DocumentRepository;
import com.github.javafaker.Faker;

@Component
public class DataInitializer implements CommandLineRunner {

    private Faker faker = new Faker();

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public void run(String... args) throws Exception {
        var dataDir = Path.of(System.getProperty("user.dir"), "docs");
        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } else {
            Files.createDirectories(dataDir);
        }

        for (int i = 1; i <= 20; i++) {
            var doc = new Document();
            doc.setName(faker.book().title());
            if (faker.random().nextBoolean()) {
                var pdfId = faker.number().randomNumber();
                doc.setPdfId(pdfId);
                generatePdf(dataDir.resolve(pdfId + ".pdf"));
            }
            documentRepository.save(doc);
        }
    }

    private void generatePdf(Path pdfFile) throws IOException {
        String fakeContent = faker.lorem().paragraphs(3).toString();

        try (var doc = new PDDocument()) {
            var page = new PDPage();
            doc.addPage(page);

            try (var contentStream = new PDPageContentStream(doc, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(25, 750);
                contentStream.showText(fakeContent);
                contentStream.endText();
            }

            doc.save(pdfFile.toFile());
        }
    }
}
