package com.franilones.medium_blog_scrapper.application.services;

import com.franilones.medium_blog_scrapper.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostScrapperOutputPort scrapperPort;

    public List<Post> getLatestPosts(){
        return scrapperPort.fetchPosts().stream()
                .sorted(Comparator.comparing(Post::getPublishDate).reversed())
                .toList();
    }
}
