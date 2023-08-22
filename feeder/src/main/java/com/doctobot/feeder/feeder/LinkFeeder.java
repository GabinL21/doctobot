package com.doctobot.feeder.feeder;

import com.doctobot.common.message.LinkMessage;
import com.doctobot.common.model.Link;
import com.doctobot.common.model.User;
import com.doctobot.feeder.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class LinkFeeder {

    private final RabbitTemplate rabbitTemplate;
    private final MessageConverter messageConverter;
    private final LinkService linkService;

    @Scheduled(cron = "0 * * ? * *")
    public void feed() {
        List<LinkMessage> linkMessages = getLinkMessages();
        for (LinkMessage linkMessage : linkMessages) {
            sendLinkMessage(linkMessage);
        }
    }

    protected void sendLinkMessage(LinkMessage linkMessage) {
        Message message = messageConverter.toMessage(linkMessage, new MessageProperties());
        rabbitTemplate.convertAndSend("doctobot.links", message);
        log.info("Sent message {}", message);
    }

    protected List<LinkMessage> getLinkMessages() {
        List<LinkMessage> linkMessages = new ArrayList<>();
        Map<User, List<Link>> linksByUser = linkService.getLinksByUser();
        for (Map.Entry<User, List<Link>> userLinks : linksByUser.entrySet()) {
            LinkMessage linkMessage = new LinkMessage(userLinks.getKey(), userLinks.getValue());
            linkMessages.add(linkMessage);
        }
        return linkMessages;
    }
}
