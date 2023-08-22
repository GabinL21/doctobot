package com.doctobot.notifier.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("doctobot.notifier.email")
@ConditionalOnProperty(name = "doctobot.notifier.mode", havingValue = "email")
@RequiredArgsConstructor
@Getter
public class EmailConfig {

    private final String sender;
    private final String password;
    private final Smtp smtp;
}
