package com.abnamro.developer.meetups.content.external;

import com.abnamro.developer.meetups.interfaces.Content;

public class Netflix implements Content {
    @Override
    public String getTitle() {
        return "Live Free or Die Hard";
    }

    @Override
    public String getDescription() {
        return "Cyber-terrorists hack into computers at the FBI, who had sent McClane to bring in computer hacker Matthew \"Matt\" Farrell (Justin Long) for questioning. Assassins hired by terrorist mastermind Thomas Gabriel (Timothy Olyphant) attempt to kill McClane and Farrell. Farrell tells McClane that the terrorists are actually in the middle of a \"fire sale\" — a crippling cyber-warfare attack on the national infrastructure: power, public utilities, traffic, and other computer-controlled systems. Although the terrorists capture Lucy and Farrell, McClane foils the criminals and saves the hostages.";
    }
}
