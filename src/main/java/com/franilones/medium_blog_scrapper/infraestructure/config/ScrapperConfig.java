package com.franilones.medium_blog_scrapper.infraestructure.config;

import com.franilones.medium_blog_scrapper.domain.ports.output.PortsScrapperOutputPort;
import com.franilones.medium_blog_scrapper.infraestructure.adapters.output.MediumJsoupScrapperAdapter;
import com.franilones.medium_blog_scrapper.infraestructure.adapters.output.MediumRomeScrapperAdapter;
import com.franilones.medium_blog_scrapper.infraestructure.adapters.output.MediumScrapperFallbackAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ScrapperConfig {

    @Bean
    @Primary
    public PortsScrapperOutputPort portsScrapperOutputPort(MediumRomeScrapperAdapter romeAdapter, MediumJsoupScrapperAdapter jsoupAdapter) {

        return new MediumScrapperFallbackAdapter(romeAdapter, jsoupAdapter);
    }
}
