package com.doctobot.notifier.notifier.email;

import com.doctobot.notifier.config.EmailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
@ConditionalOnProperty(name = "doctobot.notifier.mode", havingValue = "email")
@Log4j2
@RequiredArgsConstructor
public class EmailSender {

    private final EmailConfig emailConfig;

    public void sendEmails(String subject, String content, List<String> emails) {
        Properties properties = emailConfig.getSmtp().toProperties();

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getSender(), emailConfig.getPassword());
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfig.getSender()));
            Address[] addresses = getAddresses(emails);
            message.addRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException exception) {
            log.error(exception.getMessage());
        }
    }

    private Address[] getAddresses(List<String> emails) throws AddressException {
        int nbEmails = emails.size();
        List<Address> addresses = new ArrayList<>(nbEmails);
        for (String email : emails) {
            addresses.add(new InternetAddress(email));
        }
        return addresses.toArray(new Address[nbEmails]);
    }
}
