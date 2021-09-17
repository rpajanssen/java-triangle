package com.abnamro.developer.feeding.meals;

import com.abnamro.developer.feeding.interfaces.Experience;
import com.abnamro.developer.feeding.interfaces.Snack;

public class Lunch implements Snack {
    @Override
    public Experience eatIt() {
        System.out.println("Eating lunch...");
        return Experience.OK;
    }
}
