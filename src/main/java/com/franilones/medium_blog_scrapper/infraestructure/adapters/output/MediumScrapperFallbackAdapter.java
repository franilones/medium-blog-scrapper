package com.franilones.medium_blog_scrapper.infraestructure.adapters.output;

import com.franilones.medium_blog_scrapper.domain.model.Post;
import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MediumScrapperFallbackAdapter implements PortsScrapperOutputPort {

    private final MediumRomeScrapperAdapter romeAdapter;
    private final MediumJsoupScrapperAdapter jsoupAdapter;

    @Override
    public List<Post> fetchPostsByUsername(String username) {
         try{
             log.info("Trying to fetch posts with Rome for the username: {}", username);
             return romeAdapter.fetchPostsByUsername(username);
         }catch (Exception e){
             log.warn("Failed fetching posts with Rome for the username: {}", username, e);
             log.info("Trying to fetch posts with Jsoup for the username: {}", username);
             return jsoupAdapter.fetchPostsByUsername(username);
         }
    }
}
