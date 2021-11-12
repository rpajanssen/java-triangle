package com.abnamro.developer.meetups.content.external;

import com.abnamro.developer.feeding.fatty.Liquorice;
import com.abnamro.developer.meetups.content.internal.Gossip;
import com.abnamro.developer.meetups.interfaces.Content;

public class LongCoffeeMachineTalk implements Content {
    @Override
    public String getTitle() {
        return "Relaxing short break with some coffee";
    }

    @Override
    public String getDescription() {
        return "Coffee, small talk, some cake Indian sweets, some more coffee.";
    }

    @Override
    public void consume() {
        Content.super.consume();
        new Gossip().consume();
        Content.super.consume();
        new Liquorice().consume();
        Content.super.consume();
    }
}
