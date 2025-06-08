package com.franilones.medium_blog_scrapper.infraestructure.adapters.output;

import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.DocumentFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediumJsoupScrapperAdapterTest {

    @Mock
    private DocumentFetcher documentFetcher;

    @InjectMocks
    private MediumJsoupScrapperAdapter adapter;

    @Test
    void givenValidHtml_whenFetchPosts_thenReturnNonEmptyList() throws IOException {
        String html = """
                    <article class="m ba">
                        <div class="pw-post-title-container" data-href="test-post-url">
                            <h2>Test Title</h2>
                        </div>
                        <figure class="h k">
                            <img src="test-image.jpg">
                        </figure>
                        <h3>Test summary</h3>
                        <a>
                            <div>
                                <div class="n">
                                    <div class="ab q oy">
                                     <span>Sep 10, 2023</span>
                                    </div>
                                </div>
                            </div>
                        </a>
                        <div class="ab q af">
                            <div class="mj ab"></div>
                                        <svg></svg><span>150</span>
                        </div>
                    </article>
                """;
        Document doc = Jsoup.parse(html);
        when(documentFetcher.fetch(anyString())).thenReturn(doc);

        List<Post> posts = adapter.fetchPostsByUsername("fakeUser");
        assertNotNull(posts);
        assertFalse(posts.isEmpty());
        Post post = posts.get(0);
        assertAll(
                () -> assertEquals("Test Title", post.getTitle()),
                () -> assertEquals("test-post-url", post.getPostUrl()),
                () -> assertEquals("test-image.jpg", post.getImageUrl()),
                () -> assertEquals("Test summary", post.getSummary()),
                () -> assertEquals("Sep 10, 2023", post.getPublishDate())
        );
    }
}