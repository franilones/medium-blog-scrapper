package com.franilones.medium_blog_scrapper.infraestructure.config;

import com.franilones.medium_blog_scrapper.infraestructure.adapters.input.web.CacheHeaderInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CacheHeaderInterceptor cacheHeaderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cacheHeaderInterceptor)
                .addPathPatterns("/api/posts/**");
    }
}