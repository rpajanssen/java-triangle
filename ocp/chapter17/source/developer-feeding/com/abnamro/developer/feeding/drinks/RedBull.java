package com.abnamro.developer.feeding.drinks;

import com.abnamro.developer.feeding.interfaces.Drink;
import com.abnamro.developer.feeding.interfaces.Experience;

public class RedBull implements Drink {
    @Override
    public Experience sip() {
        System.out.println("Sipping RedBull...");
        return Experience.WHATWASTHAT;
    }

    @Override
    public Experience gulp() {
        System.out.println("Drinking RedBull...");
        return Experience.YUCK;
    }
}
