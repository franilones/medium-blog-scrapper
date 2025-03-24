package com.franilones.medium_blog_scrapper.infraestructure.adapters.input.rest;

import com.franilones.medium_blog_scrapper.application.services.PostService;
import com.franilones.medium_blog_scrapper.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{username}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username) {
        List<Post> posts = postService.getPostsByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
