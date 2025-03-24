package com.franilones.medium_blog_scrapper.domain.ports.output;

import com.franilones.medium_blog_scrapper.domain.model.Post;

import java.util.List;

public interface PortsScrapperOutputPort {
    List<Post> fetchPostsByUsername(String username);
}
