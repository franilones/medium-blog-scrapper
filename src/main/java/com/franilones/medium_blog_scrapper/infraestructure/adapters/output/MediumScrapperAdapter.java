package com.franilones.medium_blog_scrapper.infraestructure.adapters.output;

import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.DocumentFetcher;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MediumScrapperAdapter implements PortsScrapperOutputPort {

    private static final DateTimeFormatter MEDIUM_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private final DocumentFetcher documentFetcher;

    public MediumScrapperAdapter(DocumentFetcher documentFetcher) {
        this.documentFetcher = documentFetcher;
    }

    @Override
    public List<Post> fetchPostsByUsername(String username) {
        log.info("Iniciando scraping para usuario: {}", username);
        try {
            String url = "https://medium.com/@" + username;
            log.debug("Construyendo URL: {}", url);

            Document doc = documentFetcher.fetch(url);
            log.info("Documento HTML obtenido exitosamente");

            return parseHtml(doc);
        } catch (IOException e) {
            log.error("Error al obtener posts para usuario: {}", username, e);
            throw new RuntimeException("Error fetching posts for user: " + username, e);
        }
    }

    public List<Post> parseHtml(Document doc) {
        List<Element> articles = doc.select("article");

        List<Post> postMapped = articles.stream()
                .map(this::parsePost)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("Posts mapeados exitosamente: {}", postMapped.size());
        return postMapped;
    }

    private Post parsePost(Element article) {
        try {
            Post post = Post.builder()
                    .title(extractTitle(article))
                    .imageUrl(extractImageUrl(article))
                    .summary(extractSummary(article))
                    .claps(extractClaps(article))
                    .publishDate(extractPublishDate(article))
                    .build();

            log.debug("Post parseado: {}", post);
            return post;
        } catch (Exception e) {
            log.error("Error al parsear artículo: {}", article.html(), e);
            return null;
        }
    }

    private String extractTitle(Element article) {
        String title = article.select("h2").text();
        log.debug("Título extraído: {}", title);
        return title;
    }

    private String extractImageUrl(Element article) {
        Element img = article.select(".h.k > img").first();
        String imageUrl = img != null ? img.attr("src") : "";
        log.debug("URL de imagen extraída: {}", imageUrl);
        return imageUrl;
    }

    private String extractSummary(Element article) {
        Element summary = article.select("h3").first();
        String summaryText = summary != null ? summary.text() : "";
        log.debug("Resumen extraído: {}", summaryText);
        return summaryText;
    }

    private int extractClaps(Element article) {
        try {
            Element clapsElement = article.select(".ab.q.oy > svg + span").first();
            if (clapsElement == null) {
                log.debug("Elemento de claps no encontrado");
                return 0;
            }
            int claps = Integer.parseInt(clapsElement.text().trim());
            log.debug("Claps extraídos: {}", claps);
            return claps;
        } catch (NumberFormatException e) {
            log.error("Error al convertir claps a número: ",  e);
            return 0;
        }
    }

    private String extractPublishDate(Element article) {
        try {
            Element timeElement = article.select(".ab.q.af > .mj.ab + span").first();
            if (timeElement == null) {
                log.warn("Elemento de fecha no encontrado");
                return LocalDateTime.now().toString();
            }

            String date = timeElement.text();
            log.debug("Fecha cruda extraída: {}", date);

            return date;
        } catch (DateTimeParseException e) {
            log.error("Error al parsear fecha: ", e);
            return LocalDateTime.now().toString();
        }
    }
}