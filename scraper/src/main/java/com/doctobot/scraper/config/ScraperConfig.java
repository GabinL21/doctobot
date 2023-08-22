package com.doctobot.scraper.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("doctobot.scraper")
@RequiredArgsConstructor
@Getter
public class ScraperConfig {

    private final boolean strict;
    private final String seleniumHubHost;
}
