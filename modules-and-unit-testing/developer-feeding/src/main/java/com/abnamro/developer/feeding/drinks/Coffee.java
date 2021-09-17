package com.abnamro.developer.feeding.drinks;

import com.abnamro.developer.feeding.interfaces.Drink;
import com.abnamro.developer.feeding.interfaces.Experience;

public class Coffee implements Drink {
    @Override
    public Experience sip() {
        System.out.println("Sipping coffee...");
        return Experience.OK;
    }

    @Override
    public Experience gulp() {
        System.out.println("Drinking coffee...");
        return Experience.OK;
    }
}
