package com.franilones.medium_blog_scrapper.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    public String title;
    public String postUrl;
    public String imageUrl;
    public String summary;
    public String publishDate;
}
