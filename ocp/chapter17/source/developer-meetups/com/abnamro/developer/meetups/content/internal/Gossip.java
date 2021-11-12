package com.abnamro.developer.meetups.content.internal;

import com.abnamro.developer.meetups.interfaces.Content;

public class Gossip implements Content {

    @Override
    public String getTitle() {
        return "Did you know ....?";
    }

    @Override
    public String getDescription() {
        return "The tasty coffee machine talk :)";
    }
}
