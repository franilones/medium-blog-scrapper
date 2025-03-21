package com.franilones.medium_blog_scrapper.application.service;

import com.franilones.medium_blog_scrapper.application.services.PostService;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostScrapperOutputPort scrapperPort;

    @InjectMocks
    private PostService postService;

    @Test
    void givenThreePostsFromScrapper_whenGettingLatestPosts_thenReturnPostsSortedByDateDesc(){

       //  Given
        Post post1 = Post.builder().publishDate(LocalDateTime.now().minusDays(2)).build();
        Post post2 = Post.builder().publishDate(LocalDateTime.now().minusDays(1)).build();
        Post post3 = Post.builder().publishDate(LocalDateTime.now()).build();

        //  When
        when(scrapperPort.fetchPosts()).thenReturn(List.of(post1, post2, post3));

        List<Post> result = postService.getLatestPosts();

        //  Then
        assertAll(
                () -> assertEquals(3 , result.size(), "Deberían devolverse todos los posts"),
                () -> assertTrue(
                        result.get(0).getPublishDate().isAfter(result.get(1).getPublishDate()),
                        "El post más reciente debe estar primero"
                ),
                () -> assertTrue(
                        result.get(2).getPublishDate().isBefore(result.get(1).getPublishDate()),
                        "El post más antiguo debe estar último"
                )
        );
    }
}
