package com.doctobot.common.message;

import com.doctobot.common.model.Link;
import com.doctobot.common.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class LinkMessage {

    private User user;
    private List<Link> links;
}
