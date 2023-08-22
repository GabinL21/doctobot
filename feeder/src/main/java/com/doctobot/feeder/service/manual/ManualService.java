package com.doctobot.feeder.service.manual;

import com.doctobot.common.model.Link;
import com.doctobot.common.model.User;
import com.doctobot.feeder.config.ManualFeederConfig;
import com.doctobot.feeder.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "doctobot.feeder.mode", havingValue = "manual")
@Log4j2
@RequiredArgsConstructor
public class ManualService implements LinkService {

    public static final String DUMMY_ID = "0";

    private final ManualFeederConfig manualFeederConfig;

    @Override
    public Map<User, List<Link>> getLinksByUser() {
        User user = new User(DUMMY_ID, "user", manualFeederConfig.getEmails());
        List<Link> links = manualFeederConfig.getUrls().stream()
                .map(url -> new Link(DUMMY_ID, DUMMY_ID, url)).toList();
        return Map.of(user, links);
    }
}
