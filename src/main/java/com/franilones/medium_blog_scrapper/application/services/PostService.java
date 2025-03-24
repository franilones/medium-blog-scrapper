package com.franilones.medium_blog_scrapper.application.services;

import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PortsScrapperOutputPort scrapperPort;

    public List<Post> getPostsByUsername(String username) {
        return scrapperPort.fetchPostsByUsername(username).stream()
                .sorted(Comparator.comparing(Post::getPublishDate).reversed())
                .toList();
    }
}
