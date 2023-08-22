package com.doctobot.common.model;

import java.util.List;

public record User(String id, String name, List<String> emails) {
}
