package com.doctobot.feeder.service;

import com.doctobot.common.model.Link;
import com.doctobot.common.model.User;

import java.util.List;
import java.util.Map;

public interface LinkService {

    Map<User, List<Link>> getLinksByUser();
}
