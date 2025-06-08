package com.franilones.medium_blog_scrapper.infraestructure.adapters.output;

import com.franilones.medium_blog_scrapper.domain.exception.MediumScrapperException;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.DocumentFetcher;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediumJsoupScrapperAdapter implements PortsScrapperOutputPort {

    private final DocumentFetcher documentFetcher;

    @Override
    public List<Post> fetchPostsByUsername(String username) {
        log.info("Iniciando scraping para usuario: {}", username);
        try {
            String url = "https://medium.com/@" + username;

            Document doc = documentFetcher.fetch(url);

            return parseHtml(doc);
        } catch (IOException e) {
            log.error("Error obtaining post for the user: {}", username, e);
            throw new MediumScrapperException("Error fetching posts for user: " + username, e);
        }
    }

    public List<Post> parseHtml(Document doc) {
        List<Element> articles = doc.select("article");

        return articles.stream()
                .map(this::parsePost)
                .filter(Objects::nonNull)
                .toList();
    }

    private Post parsePost(Element article) {
        try {
            return Post.builder()
                    .title(extractTitle(article))
                    .postUrl(extractPostUrl(article))
                    .imageUrl(extractImageUrl(article))
                    .summary(extractSummary(article))
                    .publishDate(extractPublishDate(article))
                    .build();

        } catch (Exception e) {
            log.error("Error parsing the artcile: {}", article.html(), e);
            return null;
        }
    }

    private String extractPostUrl(Element article) {
        return article.select("div[data-href]").attr("data-href");
    }

    private String extractTitle(Element article) {
        return article.select("h2").text();
    }

    private String extractImageUrl(Element article) {
        Element img = article.select(" img").first();
        return img != null ? img.attr("src") : "";
    }

    private String extractSummary(Element article) {
        Element summary = article.select("h3").first();
        return summary != null ? summary.text() : "";
    }

    private String extractPublishDate(Element article) {
        try {
            Element timeElement = article.select("span:first-child").first();
            if (timeElement == null) {
                log.warn("Date not found in article: {}", article.html());
                return LocalDateTime.now().toString();
            }

            return timeElement.text();

        } catch (DateTimeParseException e) {
            log.error("Error parsing date: ", e);
            return LocalDateTime.now().toString();
        }
    }
}