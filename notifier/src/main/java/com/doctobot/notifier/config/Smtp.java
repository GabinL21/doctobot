package com.doctobot.notifier.config;

import java.util.Properties;

public record Smtp(String host, int port, boolean sslEnabled, boolean authEnabled) {

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", sslEnabled);
        properties.put("mail.smtp.auth", authEnabled);
        return properties;
    }
}
