package com.franilones.medium_blog_scrapper.infraestructure.adapters.input.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class CacheHeaderInterceptor implements HandlerInterceptor {
    private final CacheManager cacheManager;

    public CacheHeaderInterceptor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        log.info("[CacheHeaderInterceptor] Intercepted URL={} ", request.getRequestURI());
        String username = request.getParameter("username");
        Cache cache = cacheManager.getCache("postsByUser");
        boolean hit = cache != null && cache.get(username) != null;
        response.addHeader("X-Cache", hit ? "HIT" : "MISS");
    }
}