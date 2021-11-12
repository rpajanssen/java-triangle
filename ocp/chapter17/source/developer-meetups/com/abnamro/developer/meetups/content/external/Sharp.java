package com.abnamro.developer.meetups.content.external;

import com.abnamro.developer.meetups.interfaces.Content;

public class Sharp implements Content {
    @Override
    public String getTitle() {
        return "Sharp: Bank rules made fun";
    }

    @Override
    public String getDescription() {
        return "How to learn a developer about the real important stuff.";
    }
}
