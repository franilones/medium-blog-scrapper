package com.franilones.medium_blog_scrapper.infraestructure.adapters.output;

import com.franilones.medium_blog_scrapper.domain.exception.MediumScrapperException;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class MediumRomeScrapperAdapter implements PortsScrapperOutputPort {

    @Override
    public List<Post> fetchPostsByUsername(String username) {
        try {
            String url = "https://medium.com/feed/@" + username;
            SyndFeedInput input = createFeedInput();
            XmlReader reader = createXmlReader(url);
            SyndFeed feed = input.build(reader);

            return feed.getEntries().stream()
                    .limit(5)
                    .map(this::toPost)
                    .toList();
        } catch (Exception e) {
            log.error("Error obtaining RSS from {}: {}", username, e.getMessage(), e);
            throw new MediumScrapperException("Error fetching posts from RSS", e);
        }
    }


    protected SyndFeedInput createFeedInput() {
        return new SyndFeedInput();
    }


    protected XmlReader createXmlReader(String url) throws IOException {
        return new XmlReader(new URL(url));
    }

    private Post toPost(SyndEntry entry) {
        String html = extractHtml(entry);
        String imageUrl = extractFirstImageSrc(html);
        String summary = extractSummary(entry);
        String dateIso = extractPublishDate(entry);

        return Post.builder()
                .title(entry.getTitle())
                .postUrl(entry.getLink())
                .summary(summary)
                .publishDate(dateIso)
                .imageUrl(imageUrl)
                .build();
    }

    private String extractHtml(SyndEntry entry) {
        return Optional.ofNullable(entry.getContents())
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0).getValue())
                .orElseGet(() -> Optional.ofNullable(entry.getDescription())
                        .map(SyndContent::getValue)
                        .orElse(""));
    }

    private String extractFirstImageSrc(String html) {
        return Optional.ofNullable(Jsoup.parse(html).selectFirst("img"))
                .map(img -> img.attr("src"))
                .orElse("");
    }

    private String extractSummary(SyndEntry entry) {
        return Optional.ofNullable(entry.getDescription())
                .map(SyndContent::getValue)
                .orElse("");
    }

    private String extractPublishDate(SyndEntry entry) {
        return Optional.ofNullable(entry.getPublishedDate())
                .map(Date::toString)
                .orElse("");
    }
}
