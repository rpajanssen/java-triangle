package com.abnamro.developer.feeding.drinks;

import com.abnamro.developer.feeding.interfaces.Drink;
import com.abnamro.developer.feeding.interfaces.Experience;

public class RedBull implements Drink {
    @Override
    public Experience sip(String name) {
        System.out.println(name + "sipping RedBull...");
        return Experience.WHATWASTHAT;
    }

    @Override
    public Experience gulp(String name) {
        System.out.println(name + "drinking RedBull...");
        return Experience.YUCK;
    }
}
