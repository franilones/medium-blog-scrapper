package com.franilones.medium_blog_scrapper.infraestructure.adapters.input;

import com.franilones.medium_blog_scrapper.application.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BlogFetchScheduler {
    private final PostService postService;

    @Scheduled(cron = "0 */10 * * * *")
    public void fetchPostsForFran() {
        String userName = "franolmosdev";
        log.info("⏰[Scheduler] Forzando refresco de cache para @{}", userName);
        postService.refreshPostsCache(userName);
        log.info("⏰[Scheduler] Cache updated for @{}", userName);
    }
}
