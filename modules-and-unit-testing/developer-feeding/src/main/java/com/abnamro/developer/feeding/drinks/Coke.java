package com.abnamro.developer.feeding.drinks;

import com.abnamro.developer.feeding.interfaces.Drink;
import com.abnamro.developer.feeding.interfaces.Experience;

public class Coke implements Drink {
    @Override
    public Experience sip() {
        System.out.println("Sipping coke...");
        return Experience.MOREMORE;
    }

    @Override
    public Experience gulp() {
        System.out.println("Drinking coffee...");
        return Experience.DELICIOUS;
    }
}
