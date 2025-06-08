package com.franilones.medium_blog_scrapper.application.service;

import com.franilones.medium_blog_scrapper.application.services.PostService;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PortsScrapperOutputPort scrapperPort;

    @InjectMocks
    private PostService postService;

    @Test
    void givenThreePostsFromScrapper_whenGettingLatestPosts_thenReturnOriginalOrder() {
        // Given
        Post post1 = Post.builder().publishDate("2023-04-10T00:00:00").build();
        Post post2 = Post.builder().publishDate("2023-04-11T00:00:00").build();
        Post post3 = Post.builder().publishDate("2023-04-12T00:00:00").build();
        List<Post> mockPosts = List.of(post1, post2, post3);
        when(scrapperPort.fetchPostsByUsername("franilones")).thenReturn(mockPosts);

        // When
        List<Post> result = postService.getPostsByUsername("franilones");

        // Then: service returns exactly what scrapper returns
        assertThat(result).containsExactly(post1, post2, post3);
        verify(scrapperPort).fetchPostsByUsername("franilones");
    }

    @Test
    void givenCacheRefreshInvocation_whenRefreshingCache_thenReturnOriginalOrder() {
        // Given
        Post a = Post.builder().publishDate("2025-01-01T12:00:00").build();
        Post b = Post.builder().publishDate("2025-02-01T12:00:00").build();
        List<Post> posts = List.of(a, b);
        when(scrapperPort.fetchPostsByUsername("franilones")).thenReturn(posts);

        // When
        List<Post> refreshed = postService.refreshPostsCache("franilones");

        // Then: should return in original order
        assertThat(refreshed).containsExactly(a, b);
        verify(scrapperPort).fetchPostsByUsername("franilones");
    }
}
