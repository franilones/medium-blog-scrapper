package com.franilones.medium_blog_scrapper.infraestructure.adapters;

import com.franilones.medium_blog_scrapper.domain.ports.DocumentFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupDocumentFetcher implements DocumentFetcher {
    @Override
    public Document fetch(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();
    }
}
