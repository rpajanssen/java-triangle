package com.abnamro.developer.meetups.content.external;

import com.abnamro.developer.meetups.interfaces.Content;

public class CleanCode implements Content {
    @Override
    public String getTitle() {
        return "Clean Code";
    }

    @Override
    public String getDescription() {
        return "How to write sustainable code.";
    }
}
