package com.franilones.medium_blog_scrapper.application.service;

import com.franilones.medium_blog_scrapper.application.services.PostService;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PortsScrapperOutputPort scrapperPort;

    @InjectMocks
    private PostService postService;

    @Test
    void givenThreePostsFromScrapper_whenGettingLatestPosts_thenReturnPostsSortedByDateDesc() {

        // Given
        Post post1 = Post.builder().publishDate("2023-04-10T00:00:00").build();
        Post post2 = Post.builder().publishDate("2023-04-11T00:00:00").build();
        Post post3 = Post.builder().publishDate("2023-04-12T00:00:00").build();

        // Lista en el orden original que devuelve el scrapperPort
        List<Post> mockPosts = List.of(post1, post2, post3);

        // When
        String username = "franilones";
        when(scrapperPort.fetchPostsByUsername(username)).thenReturn(mockPosts);

        List<Post> result = postService.getPostsByUsername(username);

        // Then
        assertAll(
                () -> assertEquals(3, result.size(), "Deberían devolverse todos los posts"),
                () -> assertSame(post1, result.get(0), "Primer post debe ser post1"),
                () -> assertSame(post2, result.get(1), "Segundo post debe ser post2"),
                () -> assertSame(post3, result.get(2), "Tercer post debe ser post3")
        );
        verify(scrapperPort).fetchPostsByUsername(username);
    }
}
