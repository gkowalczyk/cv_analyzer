package com.example.cv_analyzer.infrastructure.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
public class DocumentService implements Function<DocumentService.Request, List<Document>> {

    @Override
    public List<Document> apply(DocumentService.Request request) {
        try {
            return getMyCv(new URL(request.url));
        } catch (MalformedURLException e) {
            log.error("Invalid URL provided for CV parsing: {}", request.url(), e);
            throw new RuntimeException("Invalid CV URL", e);
        }
    }

    public List<Document> getMyCv(URL url) throws MalformedURLException {

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(new UrlResource(url));
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> documents = tokenTextSplitter.apply(tikaDocumentReader.get());
        return documents;
    }

    public record Request(String url) {
    }
}
