package com.franilones.medium_blog_scrapper.infraestructure.adapters.input.rest;

import com.franilones.medium_blog_scrapper.application.services.PostService;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.infraestructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void givenValidUsername_whenGetPosts_thenReturn200WithPosts() throws Exception {
        //  Given
        String username = "franilones";
        List<Post> mockPosts = List.of(
                Post.builder().title("Post 1").build(),
                Post.builder().title("Post 2").build()
        );
        when(postService.getPostsByUsername(username)).thenReturn(mockPosts);

        // When & Then
        mockMvc.perform(get("/api/posts/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Post 1"));

    }

}
