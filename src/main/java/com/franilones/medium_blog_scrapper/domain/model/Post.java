package com.franilones.medium_blog_scrapper.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Post {
    public String title;
    public String imageUrl;
    public String summary;
    public LocalDateTime publishDate;
    public int claps;
}
