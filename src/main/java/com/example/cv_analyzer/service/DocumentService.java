package com.example.cv_analyzer.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.function.Function;

@Service
public class DocumentService implements Function<DocumentService.Request, List<Document>> {

    @Override
    public List<Document> apply(DocumentService.Request request) {
        try {
            return getMyCv();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Document> getMyCv() throws MalformedURLException {

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(new UrlResource("https://bykowski.pl/cv.pdf"));
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> documents = tokenTextSplitter.apply(tikaDocumentReader.get());
        return documents;
    }

    public record Request(String location) {
    }
}
