package com.franilones.medium_blog_scrapper.application.services;

import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private static final String RESILIENCE_SERVICE = "mediumService";

    private final PortsScrapperOutputPort scrapperPort;

    @Cacheable(value = "postByUserName", key = "#username")
    @CircuitBreaker(name = RESILIENCE_SERVICE)
    @Retry(name = RESILIENCE_SERVICE)
    @RateLimiter(name = RESILIENCE_SERVICE)
    public List<Post> getPostsByUsername(String username) {
        return scrapperPort.fetchPostsByUsername(username);
    }

    @CachePut(value = "postByUserName", key = "#username")
    public List<Post> refreshPostsCache(String username) {
        log.info("\uD83D\uDD04 [PostService] Refreshing cache for the username: {}", username);
        return scrapperPort.fetchPostsByUsername(username);
    }
}
