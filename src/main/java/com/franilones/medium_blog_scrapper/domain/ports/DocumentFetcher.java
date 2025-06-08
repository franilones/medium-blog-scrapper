package com.franilones.medium_blog_scrapper.domain.ports;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface DocumentFetcher {
    Document fetch(String url) throws IOException;
}
