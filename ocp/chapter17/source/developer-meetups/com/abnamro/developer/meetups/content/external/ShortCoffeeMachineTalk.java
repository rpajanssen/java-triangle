package com.abnamro.developer.meetups.content.external;

import com.abnamro.developer.meetups.interfaces.Content;

public class ShortCoffeeMachineTalk implements Content {
    @Override
    public String getTitle() {
        return "Relaxing short break";
    }

    @Override
    public String getDescription() {
        return "Coffee, small talk, some cake Indian sweets, some more coffee.";
    }
}
