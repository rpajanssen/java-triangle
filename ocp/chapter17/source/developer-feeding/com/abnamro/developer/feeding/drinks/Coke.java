package com.abnamro.developer.feeding.drinks;

import com.abnamro.developer.feeding.interfaces.Drink;
import com.abnamro.developer.feeding.interfaces.Experience;

public class Coke implements Drink {
    @Override
    public Experience sip(String name) {
        System.out.println(name + " sipping coke...");
        return Experience.MOREMORE;
    }

    @Override
    public Experience gulp(String name) {
        System.out.println(name + " drinking coffee...");
        return Experience.DELICIOUS;
    }
}
