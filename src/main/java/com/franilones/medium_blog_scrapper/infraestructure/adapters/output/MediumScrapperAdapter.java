package com.franilones.medium_blog_scrapper.infraestructure.adapters.output;

import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.DocumentFetcher;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MediumScrapperAdapter implements PortsScrapperOutputPort {

    private static final DateTimeFormatter MEDIUM_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private final DocumentFetcher documentFetcher;

    public MediumScrapperAdapter(DocumentFetcher documentFetcher) {
        this.documentFetcher = documentFetcher;
    }

    @Override
    public List<Post> fetchPostsByUsername(String username) {
        try{
            String url = "https://medium.com/@" + username;
            Document doc = documentFetcher.fetch(url);
            return parseHtml(doc);
        } catch (IOException e){
            throw new RuntimeException("Error fetching posts for user:  " + username, e);
        }
    }

    public List<Post> parseHtml(Document doc) {
        return doc.select(".m.ba").stream()
                .map(this::parsePost)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Post parsePost(Element article) {
        return Post.builder()
                .title(extractTitle(article))
                .imageUrl(extractImageUrl(article))
                .summary(extractSummary(article))
                .claps(extractClaps(article))
                .publishDate(extractPublishDate(article))
                .build();
    }



    private String extractTitle(Element article) {
        return article.select("h2").text();
    }

    private String extractImageUrl(Element article) {
        Element img = article.select(".h.k > img").first();
        return img != null ? img.attr("src") : "";
    }

    private String extractSummary(Element article) {
        Element summary = article.select("h3").first();
        return summary != null ? summary.text() : "";
    }

    private int extractClaps(Element article) {
        Element clapsElement = article.select("a > div:first-child .n > .n.o.nu > svg + span").first();
        return clapsElement != null ? Integer.parseInt(clapsElement.text()) : 0;
    }

    private LocalDateTime extractPublishDate(Element article) {
        Element time = article.select(".m.ba .n.o.be > .hg.n + span").first();
        if (time == null) return LocalDateTime.now();

        String datetime = time.attr("datetime");
        return LocalDateTime.parse(datetime, MEDIUM_DATE_TIME_FORMATTER);
    }
}
