package com.abnamro.developer.meetups.content.external;

import com.abnamro.developer.meetups.interfaces.Content;

public class SolidCodingPrinciples implements Content {
    @Override
    public String getTitle() {
        return "SOLID";
    }

    @Override
    public String getDescription() {
        return "How to apply the SOLID coding principles";
    }
}
