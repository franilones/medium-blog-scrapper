package com.franilones.medium_blog_scrapper.infraestructure.adapters.output;

import com.franilones.medium_blog_scrapper.domain.exception.MediumScrapperException;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediumRomeScrapperAdapterTest {

    @Mock
    private SyndFeedInput feedInput;

    @Mock
    private XmlReader xmlReader;

    private MediumRomeScrapperAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = spy(new MediumRomeScrapperAdapter());
    }

    @Test
    void givenValidEntries_whenFetchPosts_thenReturnMappedPosts() throws Exception {
        SyndFeed mockFeed = createMockFeedWithEntries();

        doReturn(feedInput)
                .when(adapter).createFeedInput();
        doReturn(xmlReader)
                .when(adapter).createXmlReader(anyString());
        when(feedInput.build(xmlReader))
                .thenReturn(mockFeed);

        List<Post> posts = adapter.fetchPostsByUsername("testuser");

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0))
                .extracting(
                        Post::getTitle,
                        Post::getPostUrl,
                        Post::getSummary,
                        Post::getPublishDate,
                        Post::getImageUrl
                )
                .containsExactly(
                        "Test Post",
                        "https://test.com/post",
                        "Summary text",
                        new Date(0).toString(),
                        "image1.jpg"
                );
    }

    @Test
    void whenFeedHasMissingFields_thenReturnEmptyValues() throws Exception {
        SyndEntry entry = new SyndEntryImpl();
        SyndFeed mockFeed = new SyndFeedImpl();
        mockFeed.setEntries(List.of(entry));

        doReturn(feedInput)
                .when(adapter).createFeedInput();
        doReturn(xmlReader)
                .when(adapter).createXmlReader(anyString());
        when(feedInput.build(xmlReader))
                .thenReturn(mockFeed);

        List<Post> posts = adapter.fetchPostsByUsername("testuser");
        Post result = posts.get(0);

        assertThat(result.getTitle()).isNull();
        assertThat(result.getPostUrl()).isNull();
        assertThat(result.getSummary()).isEmpty();
        assertThat(result.getPublishDate()).isEmpty();
        assertThat(result.getImageUrl()).isEmpty();
    }

    @Test
    void whenIOExceptionOccurs_thenThrowScrapperException() throws Exception {
        doReturn(feedInput)
                .when(adapter).createFeedInput();
        doThrow(new IOException("Network error"))
                .when(adapter).createXmlReader(anyString());

        assertThrows(
                MediumScrapperException.class,
                () -> adapter.fetchPostsByUsername("testuser")
        );
    }

    private SyndFeed createMockFeedWithEntries() {
        SyndContent content = new SyndContentImpl();
        content.setValue("<div><img src='image1.jpg'/><p>Content</p></div>");

        SyndContent description = new SyndContentImpl();
        description.setValue("Summary text");

        SyndEntry entry1 = new SyndEntryImpl();
        entry1.setTitle("Test Post");
        entry1.setLink("https://test.com/post");
        entry1.setPublishedDate(new Date(0));
        entry1.setDescription(description);
        entry1.setContents(List.of(content));

        SyndEntry entry2 = new SyndEntryImpl();
        entry2.setDescription(description);

        SyndFeed feed = new SyndFeedImpl();
        feed.setEntries(List.of(entry1, entry2));
        return feed;
    }
}