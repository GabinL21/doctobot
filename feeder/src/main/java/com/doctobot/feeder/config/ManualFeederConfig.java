package com.doctobot.feeder.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("doctobot.feeder.manual")
@ConditionalOnProperty(name = "doctobot.feeder.mode", havingValue = "manual")
@RequiredArgsConstructor
@Getter
public class ManualFeederConfig {

    private final List<String> emails;
    private final List<String> urls;
}
